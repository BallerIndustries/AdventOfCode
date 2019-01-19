package Year2017

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle22Test {
    val puzzle = Puzzle22()
    val puzzleText = this::class.java.getResource("/2017/puzzle22.txt").readText().replace("\r", "")
    val exampleText = """
        ..#
        #..
        ...
    """.trimIndent()

    @Test
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(5587, result)
    }

    @Test
    fun `example part b 100 iterations`() {
        val result = puzzle.solveTwo(exampleText, 100)
        assertEquals(26, result)
    }

    @Test
    fun `example part b`() {
        val result = puzzle.solveTwo(exampleText)
        assertEquals(2511944, result)
    }

    @Test
    fun `example part a 70 iterations`() {
        val result = puzzle.solveOne(exampleText, 70)
        assertEquals(41, result)
    }

    @Test
    fun `puzzle part a`() {
        // 4999 to low
        val result = puzzle.solveOne(puzzleText)
        assertEquals(0, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(7620, result)
    }
}

class Puzzle22 {
    enum class NodeState(val char: Char?) {
        CLEAN(null), WEAKENED('W'), INFECTED('#'), FLAGGED('F');

        companion object {
            fun from(char: Char?): NodeState {
                val match = NodeState.values().find { it.char == char }!!
                return match
            }
        }

        fun becomeVirused(): NodeState {
            return when (this) {
                CLEAN -> WEAKENED
                WEAKENED -> INFECTED
                INFECTED -> FLAGGED
                FLAGGED -> CLEAN
            }
        }
    }

    enum class Direction {
        UP, DOWN, LEFT, RIGHT;

        fun left(): Direction {
            return when(this) {
                UP -> LEFT
                DOWN -> RIGHT
                LEFT -> DOWN
                RIGHT -> UP
            }
        }

        fun right(): Direction {
            return when(this) {
                UP -> RIGHT
                DOWN -> LEFT
                LEFT -> UP
                RIGHT -> DOWN
            }
        }

        fun reverse(): Puzzle22.Direction {
            return when(this) {
                UP -> DOWN
                DOWN -> UP
                LEFT -> RIGHT
                RIGHT -> LEFT
            }
        }
    }

    data class Point(val x: Int, val y: Int) {
        fun move(direction: Direction): Point {
            return when (direction) {
                Direction.UP -> this.copy(y = y - 1)
                Direction.DOWN -> this.copy(y = y + 1)
                Direction.LEFT -> this.copy(x = x - 1)
                Direction.RIGHT -> this.copy(x = x + 1)
            }
        }
    }

    fun solveOne(puzzleText: String, iterations: Int = 10000): Int {
        val grid = parseGrid(puzzleText).toMutableMap()
        val width = grid.keys.maxBy { it.x }!!.x
        val height = grid.keys.maxBy { it.y }!!.y
        
        var currentPoint = Point(width / 2, height / 2)
        var direction = Direction.UP
        var infectionsCaused = 0

        (0 until iterations).forEach {
            direction = if (grid[currentPoint] == '#') direction.right() else direction.left()

            // toggle whether char is or is not infected
            val currentPointData = grid[currentPoint]
            val newPointData = if (currentPointData == '#') null else '#'

            if (newPointData == '#') {
                infectionsCaused++
            }

            grid[currentPoint] = newPointData
            currentPoint = currentPoint.move(direction)
        }

        return infectionsCaused
    }

    private fun printState(grid: Map<Point, Char?>, currentPoint: Point): String {
        val maxX = grid.keys.maxBy { it.x }!!.x
        val maxY = grid.keys.maxBy { it.y }!!.y

        val width = (maxX * 2) - 1
        val height = (maxY * 2) - 1

        val spaghetti = (-3 until height + 3).map { y ->
            (-3 until width + 3).map { x ->

                val point = Point(x, y)
                val char = grid[point] ?: '.'

                val str = if (point == currentPoint) "[$char]" else " $char "
                str
            }.joinToString("")
        }.joinToString("\n")

        return spaghetti
    }

    private fun parseGrid(puzzleText: String): Map<Point, Char?> {
        val lines = puzzleText.split("\n")
        val height = lines.count()
        val width = lines.first().count()

        val grid = (0 until width).flatMap { x ->
            (0 until height).map { y ->

                val p = Point(x, y)
                val c = lines[y][x]

                if (c == '#') p to c else p to null
            }
        }.toMap()
        return grid
    }

    fun solveTwo(puzzleText: String, iterations: Int = 10000000): Int {
        val grid = parseGrid(puzzleText).toMutableMap()
        val width = grid.keys.maxBy { it.x }!!.x
        val height = grid.keys.maxBy { it.y }!!.y

        var currentPoint = Point(width / 2, height / 2)
        var direction = Direction.UP
        var infectionCount = 0

        (0 until iterations).forEach {

            // Write some code Angus!
            val currentState = grid[currentPoint]
            direction = when (currentState) {
                null -> direction.left()
                'W' -> direction
                '#' -> direction.right()
                'F' -> direction.reverse()
                else -> throw RuntimeException("Oh no!")
            }

            // toggle whether char is or is not infected
            val currentPointData = grid[currentPoint]
            val currentPointState = NodeState.from(currentPointData)

            val newPointData = currentPointState.becomeVirused()

            if (newPointData == NodeState.INFECTED) {
                infectionCount++
            }

            grid[currentPoint] = newPointData.char
            currentPoint = currentPoint.move(direction)
        }

        return infectionCount
    }
}
