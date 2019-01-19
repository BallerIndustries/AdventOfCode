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
    fun `example part a 70 iterations`() {
        val result = puzzle.solveOne(exampleText, 70)
        assertEquals(41, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        // 4999 to low
        assertEquals(0, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(7620, result)
    }
}

class Puzzle22 {
    enum class Direction { UP, DOWN, LEFT, RIGHT;

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
    }

    data class Point(val x: Int, val y: Int) {
        fun move(direction: Direction): Point {
            return when (direction) {
                Direction.UP -> this.copy(y - 1)
                Direction.DOWN -> this.copy(y + 1)
                Direction.LEFT -> this.copy(x - 1)
                Direction.RIGHT -> this.copy(x + 1)
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

    fun solveTwo(puzzleText: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
