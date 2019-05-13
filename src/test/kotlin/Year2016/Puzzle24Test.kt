package Year2016

import org.junit.Assert.assertEquals
import org.junit.Test

class Puzzle24Test {
    val puzzle = Puzzle24()
    val puzzleText = this::class.java.getResource("/2016/puzzle24.txt").readText().replace("\r", "")

    @Test
    fun `can solve part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(12748, result)
    }

    @Test
    fun `can solve part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(479009308, result)
    }
}

class Puzzle24 {
    data class Point(val x: Int, val y: Int)

    fun solveOne(puzzleText: String): Int {
        val (placesToVisit, grid) = parseGrid(puzzleText)
        println(placesToVisit)
        println(grid)
        return 2398729
    }

    private fun parseGrid(puzzleText: String): Pair<List<Int>, Map<Point, Char>> {
        val lines = puzzleText.split("\n")
        val height = lines.count()
        val width = lines[0].count()

        val gridWithDigits = (0 until width).flatMap { x ->
            (0 until height).map { y ->
                Point(x, y) to lines[y][x]
            }
        }.toMap()


        val placesToVisit = gridWithDigits.values.filter { it.isDigit() }.map { it.toString().toInt() }
        val grid = gridWithDigits.entries.associate { (key, value) ->
            if (value.isDigit()) {
                key to '.'
            } else {
                key to value
            }
        }
        return Pair(placesToVisit, grid)
    }

    fun solveTwo(puzzleText: String): Int {
        return 2323
    }
}
