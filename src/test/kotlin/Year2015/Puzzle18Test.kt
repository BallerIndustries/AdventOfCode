package Year2015

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle18Test {
    val puzzle = Puzzle18()
    val puzzleText = this::class.java.getResource("/2015/puzzle18.txt").readText().replace("\r", "")
    val exampleText = """
        .#.#.#
        ...##.
        #....#
        ..#...
        #.#..#
        ####..
    """.trimIndent()

    @Test
    fun `top left of origin is`() {
        val result = Puzzle18.Point(0, 0).upLeft()
        assertEquals(Puzzle18.Point(-1, -1), result)
    }

    @Test
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(4, result)
    }

    @Test
    fun `puzzle part a`() {
        // 539 too low
        val result = puzzle.solveOne(puzzleText)
        assertEquals(768, result)
    }

    @Test
    fun `puzzle part b`() {
        // 103 too low
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(781, result)
    }
}

class Puzzle18 {
    data class Point(val x: Int, val y: Int) {
        fun up() = this.copy(y = this.y - 1)
        fun down() = this.copy(y = this.y + 1)
        fun left() = this.copy(x = this.x - 1)
        fun right() = this.copy(x = this.x + 1)

        fun upLeft(): Point = up().left()
        fun upRight() = up().right()
        fun downLeft() = down().left()
        fun downRight() = down().right()

        fun neighbours() = listOf(up(), down(), left(), right(), upLeft(), upRight(), downLeft(), downRight())
    }

    fun solveOne(puzzleText: String): Int {
        var grid = parseInput(puzzleText)

        (0 until 100).forEach {
            val newDog = grid.entries.map { (point, char) ->
                val neighborsOnGrid = point.neighbours().filter { grid[it] != null }
                val neighborsOn = neighborsOnGrid.count { grid[it] == '#' }
                val newChar = runGameOfLife(char, neighborsOn)

                point to newChar
            }.toMap()

            grid = newDog
        }

        return grid.values.count { it == '#' }
    }

    private fun parseInput(puzzleText: String): Map<Point, Char> {
        val lines = puzzleText.split("\n")
        val height = lines.size
        val width = lines[0].length

        var dog = (0 until width).flatMap { x ->
            (0 until height).map { y ->
                Point(x, y) to lines[y][x]
            }
        }.toMap()
        return dog
    }

    private fun gridToString(dog: Map<Point, Char>): String {
        val width = dog.keys.maxBy { it.x }!!.x
        val height = dog.keys.maxBy { it.y }!!.y

        val octopus = (0 .. height).map { y ->
            (0 .. width).map { x ->
                val point = Point(x, y)
                dog[point]!!
            }.joinToString("")
        }

        return octopus.joinToString("\n")
    }

    private fun runGameOfLife(char: Char, neighborsOn: Int): Char {
        if (char == '#') {
            return if (neighborsOn == 2 || neighborsOn == 3) '#' else { '.' }
        }
        else if (char == '.') {
            return if (neighborsOn == 3) '#' else { '.' }
        }
        else {
            throw RuntimeException("asdoiajd")
        }
    }

    fun solveTwo(puzzleText: String): Int {
        var grid = parseInput(puzzleText)
        val maxX = grid.keys.maxBy { it.x }!!.x
        val maxY = grid.keys.maxBy { it.y }!!.y
        val corners = setOf(Point(0, 0), Point(0, maxY), Point(maxX, 0), Point(maxX, maxY))

        grid = grid.entries.map { (point, char) ->
            val newChar = if (corners.contains(point)) '#' else char
            point to newChar
        }.toMap()


        (0 until 100).forEach {
            grid = grid.entries.map { (point, char) ->
                if (corners.contains(point)) {
                    point to '#'
                }
                else {
                    val neighborsOnGrid = point.neighbours().filter { grid[it] != null }
                    val neighborsOn = neighborsOnGrid.count { grid[it] == '#' }
                    val newChar = runGameOfLife(char, neighborsOn)

                    point to newChar
                }
            }.toMap()
        }

        return grid.values.count { it == '#' }
    }
}