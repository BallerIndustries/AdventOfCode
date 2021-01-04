package Year2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle24Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle24.txt").readText().replace("\r", "")
    val puzzle = Puzzle24()

    @Test
    fun `example part a`() {
        val puzzleText = ".....\n" +
                ".....\n" +
                ".....\n" +
                "#....\n" +
                ".#..."
        val result = puzzle.solveOne(puzzleText)
        assertEquals(2129920L, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(20751345, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("a", result)
    }
}

class Puzzle24 {
    fun solveOne(puzzleText: String): Long {
        var grid = parseGrid2D(puzzleText)
        val grids = mutableSetOf<Map<Point, Char>>()

        while (grid !in grids) {
            grids.add(grid)
            grid = iterate(grid)
        }

        val width = grid.keys.maxBy { it.x }!!.x + 1

        return grid.entries.fold(0L) { acc, (point, char) ->

            if (char == '#') {
                val power = (point.y * width) + point.x
                val value = Math.pow(2.0, power.toDouble()).toLong()

                acc + value
            }
            else {
                acc
            }
        }


        throw NotImplementedError()
    }

    private fun iterate(grid: Map<Point, Char>): Map<Point, Char> {
        return grid.entries.associate { (point, char) ->
            val adjacentBugCount = point.neighbors().count { grid[it] == '#' }

            if (char == '#' && adjacentBugCount != 1) {
                point to '.'
            }
            else if (char == '.' && (adjacentBugCount == 1 || adjacentBugCount == 2)) {
                point to '#'
            }
            else {
                point to char
            }
        }
    }

    fun solveTwo(puzzleText: String): String {
        throw NotImplementedError()
    }

    data class Point(val x: Int, val y: Int) {
        fun neighbors(): List<Point> {
            return listOf(
                this.copy(x = x - 1),
                this.copy(x = x + 1),
                this.copy(y = y - 1),
                this.copy(y = y + 1),
            )
        }
    }

    private fun parseGrid2D(puzzleText: String): Map<Point, Char> {
        val lines = puzzleText.split("\n")
        val height = lines.count()
        val width = lines.first().count()

        val grid = (0 until width).flatMap { x ->
            (0 until height).map { y ->

                val p = Point(x, y)
                val c = lines[y][x]

                p to c
            }
        }.toMap()
        return grid
    }
}

