package Year2016

import org.junit.Assert.assertEquals
import org.junit.Test

class Puzzle24RedoTest {
    val puzzle = Puzzle24Redo()
    val puzzleText = this::class.java.getResource("/2016/puzzle24.txt").readText().replace("\r", "")

    @Test
    fun `can solve part a`() {
        //42728 too high
        val result = puzzle.solveOne(puzzleText)
        assertEquals(12748, result)
    }

    @Test
    fun `example part a`() {
        val exampleText = """
            ###########
            #0.1.....2#
            #.#######.#
            #4.......3#
            ###########
        """.trimIndent()

        val result = puzzle.solveOne(exampleText)
        assertEquals(14, result)
    }

    @Test
    fun `can solve part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(479009308, result)
    }
}

class Puzzle24Redo {
    data class Point(val x: Int, val y: Int) {
        fun up() = this.copy(y = this.y - 1)
        fun down() = this.copy(y = this.y + 1)
        fun right() = this.copy(x = this.x + 1)
        fun left() = this.copy(x = this.x - 1)
        fun neighbours() = listOf(up(), down(), left(), right())
    }

    fun solveOne(puzzleText: String): Int {
        val (placesToVisit, grid) = parseGrid(puzzleText)

        return 1298
    }

    fun solveTwo(puzzleText: String): Int {
        return 2323
    }

    private fun manhattanDistance(a: Point, b: Point) = Math.abs(a.x - b.x) + Math.abs(a.y - b.y)

    private fun parseGrid(puzzleText: String): Pair<Map<Char, Point>, Map<Point, Char>> {
        val lines = puzzleText.split("\n")
        val height = lines.count()
        val width = lines[0].count()

        val gridWithDigits = (0 until width).flatMap { x -> (0 until height).map { y -> Point(x, y) to lines[y][x] } }.toMap()
        val placesToVisit = gridWithDigits.entries
                .filter { it.value.isDigit() }
                .associate { (key, value) -> value to key }

        val grid = gridWithDigits.entries.associate { (key, value) ->
            if (value.isDigit()) {
                key to '.'
            } else {
                key to value
            }
        }

        return Pair(placesToVisit, grid)
    }
}
