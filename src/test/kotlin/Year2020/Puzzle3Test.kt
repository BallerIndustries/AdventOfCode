package Year2020

import Year2017.Puzzle22
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle3Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle3.txt").readText().replace("\r", "")
    val puzzle = Puzzle3()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(171, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(1206576000, result)
    }

    @Test
    fun `example part a`() {
        val puzzleText = "..##.......\n" +
                "#...#...#..\n" +
                ".#....#..#.\n" +
                "..#.#...#.#\n" +
                ".#...##..#.\n" +
                "..#.##.....\n" +
                ".#.#.#....#\n" +
                ".#........#\n" +
                "#.##...#...\n" +
                "#...##....#\n" +
                ".#..#...#.#"
        val result = puzzle.solveOne(puzzleText)
        assertEquals(7, result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = "..##.......\n" +
                "#...#...#..\n" +
                ".#....#..#.\n" +
                "..#.#...#.#\n" +
                ".#...##..#.\n" +
                "..#.##.....\n" +
                ".#.#.#....#\n" +
                ".#........#\n" +
                "#.##...#...\n" +
                "#...##....#\n" +
                ".#..#...#.#"
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(336, result)
    }
}

class Puzzle3 {
    fun solveOne(puzzleText: String, xDelta: Int = 3, yDelta: Int = 1): Int {
        val grid = parseGrid(puzzleText)
        val maxY = grid.keys.maxBy { it.y }!!.y
        val maxX = grid.keys.maxBy { it.x }!!.x

        var pos = Point(0, 0)
        var treeCount = 0

        while (pos.y <= maxY) {
            if (grid[pos] == '#') {
                treeCount++
            }

            pos = pos.copy(x = pos.x + xDelta, y = pos.y + yDelta)

            if (pos.x > maxX) {
                pos = pos.copy(x = pos.x - maxX - 1)
            }
        }

        return treeCount
    }

    fun solveTwo(puzzleText: String): Int {
        return solveOne(puzzleText, 1, 1) *
        solveOne(puzzleText, 3, 1) *
        solveOne(puzzleText, 5, 1) *
        solveOne(puzzleText, 7, 1) *
        solveOne(puzzleText, 1, 2)
    }

    private fun parseGrid(puzzleText: String): Map<Point, Char?> {
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

    data class Point(val x: Int, val y: Int) {
        fun move(direction: Puzzle22.Direction): Point {
            return when (direction) {
                Puzzle22.Direction.UP -> this.copy(y = y - 1)
                Puzzle22.Direction.DOWN -> this.copy(y = y + 1)
                Puzzle22.Direction.LEFT -> this.copy(x = x - 1)
                Puzzle22.Direction.RIGHT -> this.copy(x = x + 1)
            }
        }
    }

}

