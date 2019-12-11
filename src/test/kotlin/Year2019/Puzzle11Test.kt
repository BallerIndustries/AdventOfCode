package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle11Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle11.txt").readText().replace("\r", "")
    val puzzle = Puzzle11()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(1564, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("a", result)
    }
}

class Puzzle11 {

    data class Point(val x: Int, val y: Int) {
        fun moveForward(direction: Direction): Point {
            return when (direction) {
                Direction.UP -> this.copy(y = this.y - 1)
                Direction.DOWN -> this.copy(y = this.y + 1)
                Direction.RIGHT -> this.copy(x = this.x + 1)
                Direction.LEFT -> this.copy(x = this.x - 1)
            }
        }
    }

    enum class Tile(val value: Long) {
        BLACK(0L), WHITE(1L);
    }

    enum class Direction {
        UP, DOWN, LEFT, RIGHT;

        fun turnLeft(): Direction {
            return when (this) {
                UP -> LEFT
                RIGHT -> UP
                DOWN -> RIGHT
                LEFT -> DOWN
            }
        }

        fun turnRight(): Direction {
            return when (this) {
                UP -> RIGHT
                RIGHT -> DOWN
                DOWN -> LEFT
                LEFT -> UP
            }
        }
    }

    fun solveOne(puzzleText: String): Int {
        val split = puzzleText.split(",")
        val program = split.map { it.toLong() } + split.map { 0L } + split.map { 0L }
        var currentState = State(program, userInput = listOf(0))

        val virtualMachine = IntCodeVirtualMachine()
        val grid = mutableMapOf<Point, Tile>()
        var currentPosition = Point(0, 0)
        var currentDirection = Direction.UP

        while (true) {
            currentState = virtualMachine.runProgram(currentState)

            if (currentState.isHalted) {
                break
            }

            val tmp: Pair<List<Long>, State> = currentState.popOffOutput()
            val (colour, direction) = tmp.first
            currentState = tmp.second

            // Set the current tile colour
            val tile = when (colour) {
                0L -> Tile.BLACK
                1L -> Tile.WHITE
                else -> throw RuntimeException()
            }

            grid[currentPosition] = tile

            // Turn left or right
            currentDirection = when(direction) {
                0L -> currentDirection.turnLeft()
                1L -> currentDirection.turnRight()
                else -> throw RuntimeException()
            }

            currentPosition = currentPosition.moveForward(currentDirection)

            // Set the current tile thingy
            val currentTileNumber: Long = grid[currentPosition]?.value ?: 0L

//            val tileCode = when (grid[currentPosition]) {
//                Tile.BLACK -> 1L
//                else -> 0L
//            }

            currentState = currentState.addUserInput(currentTileNumber)
        }


        return grid.count()

        //throw NotImplementedError()
    }

    fun solveTwo(puzzleText: String): String {
        throw NotImplementedError()
    }
}

