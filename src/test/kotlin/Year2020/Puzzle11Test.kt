package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle11Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle11.txt").readText().replace("\r", "")
    val puzzle = Puzzle11()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(2368, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(2124, result)
    }

    @Test
    fun `example part a`() {
        val puzzleText = "L.LL.LL.LL\n" +
                "LLLLLLL.LL\n" +
                "L.L.L..L..\n" +
                "LLLL.LL.LL\n" +
                "L.LL.LL.LL\n" +
                "L.LLLLL.LL\n" +
                "..L.L.....\n" +
                "LLLLLLLLLL\n" +
                "L.LLLLLL.L\n" +
                "L.LLLLL.LL"
        val result = puzzle.solveOne(puzzleText)
        assertEquals(37, result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = "L.LL.LL.LL\n" +
                "LLLLLLL.LL\n" +
                "L.L.L..L..\n" +
                "LLLL.LL.LL\n" +
                "L.LL.LL.LL\n" +
                "L.LLLLL.LL\n" +
                "..L.L.....\n" +
                "LLLLLLLLLL\n" +
                "L.LLLLLL.L\n" +
                "L.LLLLL.LL"
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(26, result)
    }
}

class Puzzle11 {
    fun solveOne(puzzleText: String): Int {


        var prevGrid = parseGrid(puzzleText)
        printGrid(prevGrid)

        // . - floor
        // L - empty
        // # - occupied

        var currentGrid: Map<Point, Char> = iterate(prevGrid)
        printGrid(currentGrid)
        var iterations = 1

        while (prevGrid != currentGrid) {
            prevGrid = currentGrid
            currentGrid = iterate(prevGrid)
            printGrid(currentGrid)
            iterations++
        }

        return currentGrid.values.count { it == '#' }
    }

    private fun iterate(grid: Map<Point, Char>): Map<Point, Char> {
        return grid.entries.associate { (point, char) ->
            val nextChar: Char
            val adjacentOccupiedCount = point.neighbours().count { grid[it] == '#' }

            if (char == 'L' && adjacentOccupiedCount == 0) {
                nextChar = '#'
            }
            else if (char == '#' && adjacentOccupiedCount >= 4) {
                nextChar = 'L'
            }
            else {
                nextChar = char
            }

            point to nextChar
        }
    }

    private fun iterateTwo(grid: Map<Point, Char>): Map<Point, Char> {
        return grid.entries.associate { (point, char) ->
            val nextChar: Char
            val visibleOccupiedSeats = countVisibleOccupiedSeats(point, grid)

            if (char == 'L' && visibleOccupiedSeats == 0) {
                nextChar = '#'
            }
            else if (char == '#' && visibleOccupiedSeats >= 5) {
                nextChar = 'L'
            }
            else {
                nextChar = char
            }

            point to nextChar
        }
    }

    private fun countVisibleOccupiedSeats(point: Point, grid: Map<Point, Char>): Int {
        fun traverse(grid: Map<Point, Char>, start: Point, speed: Point): Char? {
            var pos = Point(start.x + speed.x, start.y + speed.y)

            while (grid[pos] == '.') {
                pos = Point(pos.x + speed.x, pos.y + speed.y)
            }

            return grid[pos]
        }

        return listOfNotNull(
            traverse(grid, point, Point(0, -1)), // UP
            traverse(grid, point, Point(-1, -1)), // UP LEFT
            traverse(grid, point, Point(+1, -1)), // UP RIGHT
            traverse(grid, point, Point(0, +1)), // DOWN
            traverse(grid, point, Point(-1, +1)), // DOWN LEFT
            traverse(grid, point, Point(+1, +1)), // DOWN RIGHT
            traverse(grid, point, Point(-1, 0)), // LEFT
            traverse(grid, point, Point(+1, 0)) // RIGHT
        ).count { it == '#' }
    }

    fun solveTwo(puzzleText: String): Int {
        var prevGrid = parseGrid(puzzleText)
        printGrid(prevGrid)

        // . - floor
        // L - empty
        // # - occupied

        var currentGrid: Map<Point, Char> = iterateTwo(prevGrid)
        printGrid(currentGrid)
        var iterations = 1

        while (prevGrid != currentGrid) {
            prevGrid = currentGrid
            currentGrid = iterateTwo(prevGrid)
            printGrid(currentGrid)
            iterations++
        }

        return currentGrid.values.count { it == '#' }
    }

    private fun printGrid(grid: Map<Point, Char>) {
//        val maxX = grid.keys.maxBy { it.x }!!.x
//        val maxY = grid.keys.maxBy { it.y }!!.y
//
//        val aaa = (0 .. maxY).map { y ->
//            (0 .. maxX).map { x ->
//                grid[Point(x, y)]!!
//            }.joinToString("")
//        }.joinToString("\n")

//        println(aaa)
//        println()
    }

    private fun parseGrid(puzzleText: String): Map<Point, Char> {
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
        fun up() = this.copy(y = this.y - 1)
        fun down() = this.copy(y = this.y + 1)
        fun left() = this.copy(x = this.x - 1)
        fun right() = this.copy(x = this.x + 1)

        fun upLeft() = up().left()
        fun upRight() = up().right()
        fun downLeft() = down().left()
        fun downRight() = down().right()

        fun neighbours() = listOf(up(), down(), left(), right(), upLeft(), upRight(), downLeft(), downRight())
    }
}

