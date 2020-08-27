package Year2017

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle14Test {
    val puzzle = Puzzle14()
    val puzzleText = this::class.java.getResource("/2017/puzzle14.txt").readText().replace("\r", "")
    val exampleText = "flqrgnkx"

    @Test
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(8108, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(8316, result)
    }

    @Test
    fun `example part b`() {
        val result = puzzle.solveTwo(exampleText)
        assertEquals(1242, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(1074, result)
    }
}

class Puzzle14 {
    val puzzle10 = Puzzle10()
    val hexToBinary = mapOf(
        '0' to "0000",
        '1' to "0001",
        '2' to "0010",
        '3' to "0011",
        '4' to "0100",
        '5' to "0101",
        '6' to "0110",
        '7' to "0111",
        '8' to "1000",
        '9' to "1001",
        'a' to "1010",
        'b' to "1011",
        'c' to "1100",
        'd' to "1101",
        'e' to "1110",
        'f' to "1111"
    )

    data class Point(val x: Int, val y: Int) {
        fun up() = this.copy(y = this.y - 1)
        fun down() = this.copy(y = this.y + 1)
        fun right() = this.copy(x = this.x + 1)
        fun left() = this.copy(x = this.x - 1)

        fun neighbours(): List<Point> {
            return listOf(up(), down(), left(), right())
        }
    }

    fun solveOne(puzzleText: String): Int {
        val grid = createGrid(puzzleText)
        return grid.values.count { it == '#' }
    }

    private fun createGrid(puzzleText: String): Map<Point, Char> {
        return (0..127).flatMap { y ->
            val hash = puzzle10.solveTwo("$puzzleText-$y")
            val binary = hash.map { hexToBinary[it]!! }
                .joinToString("")
                .mapIndexed { x, char ->
                    val newChar = if (char == '1') '#' else '.'
                    Point(x, y) to newChar
                }

            binary
        }.toMap()
    }

    fun solveTwo(puzzleText: String): Int {
        val grid = createGrid(puzzleText)

        // Go through all the '#' characters and find their regions, a list of all points in the region
        return grid.filter { it.value == '#' }
            .map { getRegion(grid, it.key) }
            .distinct()
            .count()
    }

    private fun getRegion(grid: Map<Point, Char>, point: Point): Set<Point> {
        val visited = mutableSetOf<Point>()
        val toProcess = mutableListOf(point)
        val region = mutableSetOf(point)

        while (toProcess.isNotEmpty()) {
            val current = toProcess.removeAt(0)
            visited.add(current)

            if (grid[current] == '#') {
                region.add(current)
                toProcess.addAll(current.neighbours().filter { grid[it] == '#' && !visited.contains(it) })
            }
        }

        return region
    }
}