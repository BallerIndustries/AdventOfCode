package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle13Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle13.txt").readText()
    val puzzle = Puzzle13()

    @Test
    fun `example a`() {
        val exmaple = """
            /->-\
            |   |  /----\
            | /-+--+-\  |
            | | |  | v  |
            \-+-/  \-+--/
              \------/
        """.trimIndent()

        puzzle.solveOne(exmaple)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("a", result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("b", result)
    }

    class Puzzle13 {
        enum class Direction {
            UP, RIGHT, DOWN, LEFT;

            fun left(): Direction {
                return when (this) {
                    UP -> LEFT
                    DOWN -> RIGHT
                    RIGHT -> UP
                    LEFT -> DOWN
                }
            }

            fun right(): Direction {
                return when (this) {
                    UP -> RIGHT
                    DOWN -> LEFT
                    RIGHT -> DOWN
                    LEFT -> UP
                }
            }

            fun straight(): Direction {
                return this
            }
        }

        enum class Turn {
            LEFT, STRAIGHT, RIGHT;

            fun next(): Turn {
                return when (this) {
                    LEFT -> STRAIGHT
                    STRAIGHT -> RIGHT
                    RIGHT -> LEFT
                }
            }
        }

        data class Point(val x: Int, val y: Int)
        data class Car(val position: Point, val direction: Direction, val turn: Turn) {

            fun turn(): Car {
                val nextDirection = when (turn) {
                    Turn.LEFT -> this.direction.left()
                    Turn.STRAIGHT -> this.direction.straight()
                    Turn.RIGHT -> this.direction.right()
                }

                val nextTurnToMake = turn.next()
                return this.copy(direction = nextDirection, turn = nextTurnToMake)
            }

            fun pointAhead(): Point {
                return when (direction) {
                    Direction.UP -> this.position.copy(y = position.y - 1)
                    Direction.DOWN -> this.position.copy(y = position.y + 1)
                    Direction.LEFT -> this.position.copy(y = position.x - 1)
                    Direction.RIGHT -> this.position.copy(y = position.x + 1)
                }
            }

            fun handleCorner(tileAhead: Char): Car {
                val nextDirection = if (tileAhead == '/') {
                    when (this.direction) {
                        Direction.UP -> Direction.RIGHT
                        Direction.DOWN -> Direction.LEFT
                        Direction.LEFT -> Direction.DOWN
                        Direction.RIGHT -> Direction.UP
                    }
                }
                else if (tileAhead == '\\') {
                    when (this.direction) {
                        Direction.UP -> Direction.LEFT
                        Direction.DOWN -> Direction.RIGHT
                        Direction.LEFT -> Direction.UP
                        Direction.RIGHT -> Direction.DOWN
                    }
                }
                else {
                    throw RuntimeException("Hey! I only want to handle '/' or '\\' characters")
                }

                return this.copy(direction = nextDirection)
            }
        }

        fun solveOne(puzzleText: String): String {
            val lines = puzzleText.split("\n")
            val maxHeight = lines.count()
            val maxWidth = lines.maxBy { it.length }!!.length

            // Create a 2D grid to chars
            println("aaa")

            val theGrid =
            (0 until maxWidth).map { x ->
                (0 until maxHeight).map { y ->
                    val point = Point(x, y)
                    val char = if (y < lines.size && x < lines[y].length) lines[y][x] else ' '

                    point to char
                }
            }.flatten().toMap()

            println()

            // Find the position and direction of the cars
//            val cars =
//            (0 until maxHeight).flatMap { y ->
//                (0 until maxWidth).mapNotNull { x ->

            val cars = theGrid.mapNotNull { entry ->
                val point = entry.key
                val tile = entry.value

                if (tile == '<') {
                    Car(point, Direction.LEFT, Turn.LEFT)
                } else if (tile == '^') {
                    Car(point, Direction.UP, Turn.LEFT)
                } else if (tile == '>') {
                    Car(point, Direction.RIGHT, Turn.LEFT)
                } else if (tile == 'v') {
                    Car(point, Direction.DOWN, Turn.LEFT)
                } else null
            }

            println("Hello")
//
//            val theGridWithoutCars = (0 until maxWidth).flatMap { x ->
//                (0 until maxHeight).map { y ->
//                    val char = theGrid[x][y]
//                    val point = Point(x, y)
//
//
//                    val newChar = if (char == '<' || char == '>') '-'
//                    else if (char == '^' || char == 'v') '|'
//                    else char
//
//                    point to newChar
//                }
//            }.toMap()
//
//            var carStates = cars.map { car ->
//                val pointAhead = car.pointAhead()
//                val tileAhead = theGridWithoutCars[pointAhead]!!
//
//                if (tileAhead == '-' || tileAhead == '|') {
//                    car.copy(position = pointAhead)
//                }
//                else if (tileAhead == '+') {
//                    car.copy(position = pointAhead).turn()
//                }
//                else if (tileAhead == '\\' || tileAhead == '/') {
//                    car.handleCorner(tileAhead)
//                }
//                else if (tileAhead == ' ') {
//                    throw RuntimeException("Woah! Fell off the tracks!")
//                }
//                else {
//                    throw RuntimeException("Woah! Met a character I have not seen before! character = $tileAhead")
//                }
//            }
//
//
//
//


            return ""
        }

        fun solveTwo(puzzleText: String): String {
            return ""
        }


    }
}

