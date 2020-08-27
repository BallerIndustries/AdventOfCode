package year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle13Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle13.txt").readText()
    val puzzle = Puzzle13()

    @Test
    fun `example part a`() {
        val exampleText = """
            /->-\
            |   |  /----\
            | /-+--+-\  |
            | | |  | v  |
            \-+-/  \-+--/
              \------/
        """.trimIndent()

        val result = puzzle.solveOne(exampleText)
        assertEquals("7,3", result)
    }

    @Test
    fun `example part b`() {
        val exampleText = """
            />-<\
            |   |
            | /<+-\
            | | | v
            \>+</ |
              |   ^
              \<->/
        """.trimIndent()

        val result = puzzle.solveTwo(exampleText)
        assertEquals("6,4", result)
    }

    @Test
    fun `puzzle part a`() {
        // NOT 94,65
        // NOT 65,9
        // NOT 26,44
        val result = puzzle.solveOne(puzzleText)
        assertEquals("115,138", result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("0,98", result)
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
        data class Car(val position: Point, val direction: Direction, val turn: Turn, val collided: Boolean = false) {

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
                    Direction.LEFT -> this.position.copy(x = position.x - 1)
                    Direction.RIGHT -> this.position.copy(x = position.x + 1)
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
                } else if (tileAhead == '\\') {
                    when (this.direction) {
                        Direction.UP -> Direction.LEFT
                        Direction.DOWN -> Direction.RIGHT
                        Direction.LEFT -> Direction.UP
                        Direction.RIGHT -> Direction.DOWN
                    }
                } else {
                    throw RuntimeException("Hey! I only want to handle '/' or '\\' characters")
                }

                return this.copy(direction = nextDirection)
            }

            fun toChar(): Char {
                return when (this.direction) {
                    Direction.UP -> '^'
                    Direction.DOWN -> 'v'
                    Direction.LEFT -> '<'
                    Direction.RIGHT -> '>'
                }
            }
        }

        fun solveOne(puzzleText: String): String {
            val lines = puzzleText.split("\n")
            val maxHeight = lines.count()
            val maxWidth = lines.maxBy { it.length }!!.length

            // Create a 2D grid to chars
            val theGrid = parseTheGrid(maxWidth, maxHeight, lines)

            // Find the position and direction of the cars
            var carState = getInitialCarState(theGrid)

            val theGridWithoutCars = getGridWithoutCars(theGrid)

            while (true) {
                carState = sortCars(carState)

                for (index in 0 until carState.size) {
                    carState[index] = moveCarAhead(carState[index], theGridWithoutCars)

                    if (hasCollisions(carState)) {
                        return getCollision(carState)
                    }
                }
            }
        }

        private fun getGridWithoutCars(theGrid: Map<Point, Char>): Map<Point, Char> {
            return theGrid.map { (point, tile) ->
                val newChar = if (tile == '<' || tile == '>') '-'
                else if (tile == '^' || tile == 'v') '|'
                else tile

                point to newChar
            }.toMap()
        }

        private fun parseTheGrid(maxWidth: Int, maxHeight: Int, lines: List<String>): Map<Point, Char> {
            return (0 until maxWidth).map { x ->
                (0 until maxHeight).map { y ->

                    val point = Point(x, y)
                    val tile = if (y > lines.lastIndex || x > lines[y].lastIndex) ' ' else lines[y][x]

                    point to tile
                }
            }.flatten().toMap()
        }

        private fun getInitialCarState(theGrid: Map<Point, Char>): MutableList<Car> {
            return theGrid.mapNotNull { (point, tile) ->
                when (tile) {
                    '<' -> Car(point, Direction.LEFT, Turn.LEFT)
                    '^' -> Car(point, Direction.UP, Turn.LEFT)
                    '>' -> Car(point, Direction.RIGHT, Turn.LEFT)
                    'v' -> Car(point, Direction.DOWN, Turn.LEFT)
                    else -> null
                }
            }.toMutableList()
        }

        private fun getCollision(carState: List<Car>): String {
            val collisionPoint = carState
                .map { it.position }
                .groupBy { it }
                .map { it.key to it.value.count() }
                .maxBy { it.second }!!.first

            return "${collisionPoint.x},${collisionPoint.y}"
        }

        private fun outputGrid(carState: List<Car>, theGridWithoutCars: Map<Point, Char>) {
            val width = theGridWithoutCars.keys.maxBy { it.x }!!.x
            val height = theGridWithoutCars.keys.maxBy { it.y }!!.y

            val dog = (0..height).map { y ->
                (0..width).map { x ->

                    val point = Point(x, y)
                    val carAtThisPoint = carState.find { it.position == point }

                    if (carAtThisPoint != null) {
                        carAtThisPoint.toChar()
                    } else {
                        theGridWithoutCars[point]!!
                    }
                }.joinToString("")
            }.joinToString("\n")

            println(dog)
        }

        private fun hasCollisions(carState: List<Car>): Boolean {
            return carState.map { it.position }.toSet().size < carState.size
        }

        private fun moveCarsAhead(cars: List<Car>, theGridWithoutCars: Map<Point, Char>): List<Car> {
            val sortedCars = sortCars(cars)
            return sortedCars.map { car -> moveCarAhead(car, theGridWithoutCars) }
        }

        private fun moveCarAhead(car: Car, theGridWithoutCars: Map<Point, Char>): Car {
            val pointAhead = car.pointAhead()
            val tileAhead = theGridWithoutCars[pointAhead]!!

            return when (tileAhead) {
                '-', '|'    -> car.copy(position = pointAhead)
                '+'         -> car.copy(position = pointAhead).turn()
                '\\', '/'   -> car.copy(position = pointAhead).handleCorner(tileAhead)
                ' '         -> throw RuntimeException("Woah! Fell off the tracks!")
                else        -> throw RuntimeException("Woah! Met a character I have not seen before! character = $tileAhead")
            }
        }

        private fun sortCars(cars: List<Car>): MutableList<Car> {
            val sortedCars = cars.sortedWith(Comparator { a, b ->
                when {
                    a.position.y > b.position.y -> 1
                    a.position.y < b.position.y -> -1
                    a.position.x > b.position.x -> 1
                    a.position.x < b.position.x -> -1
                    else -> 0
                }
            })

            return sortedCars.toMutableList()
        }

        fun solveTwo(puzzleText: String): String {
            val lines = puzzleText.split("\n")
            val maxHeight = lines.count()
            val maxWidth = lines.maxBy { it.length }!!.length

            // Create a 2D grid to chars
            val theGrid = parseTheGrid(maxWidth, maxHeight, lines)

            // Find the position and direction of the cars
            var carState = getInitialCarState(theGrid)

            val theGridWithoutCars = getGridWithoutCars(theGrid)

            while (true) {
                carState = sortCars(carState)

                for (index in 0 until carState.size) {
                    carState[index] = moveCarAhead(carState[index], theGridWithoutCars)

                    if (hasCollisions(carState)) {
                        carState = markCollidedCars(carState)
                    }
                }

                // A tick has gone by. Filter out the cars that have crashed
                carState = carState.filter { !it.collided }.toMutableList()

                if (carState.size == 1) {
                    val onlyCar = carState[0]
                    return "${onlyCar.position.x},${onlyCar.position.y}"
                }
            }
        }

        private fun markCollidedCars(carState: MutableList<Car>): MutableList<Car> {
            val pointsWithCollisions = carState
                .groupBy { it.position }
                .filter { it.value.count() > 1 }
                .map { it.key }
                .toSet()

            return carState.map { car ->
                if (pointsWithCollisions.contains(car.position)) car.copy(collided = true) else car
            }.toMutableList()
        }
    }
}

