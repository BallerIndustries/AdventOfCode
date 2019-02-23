package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test

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
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(10291029, result)
    }

    @Test
    fun `puzzle part a`() {
        // 539 too low
        val result = puzzle.solveOne(puzzleText)
        assertEquals(18965440, result)
    }

    @Test
    fun `puzzle part b`() {
        // 103 too low
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(18862900, result)
    }
}

class Puzzle18 {
    data class Point(val x: Int, val y: Int) {
        fun up() = this.copy(y = this.y - 1)
        fun down() = this.copy(y = this.y + 1)
        fun left() = this.copy(y = this.x - 1)
        fun right() = this.copy(y = this.x + 1)

        fun upLeft() = up().left()
        fun upRight() = up().right()
        fun downLeft() = down().left()
        fun downRight() = down().right()

        fun neighbours() = listOf(up(), down(), left(), right(), upLeft(), upRight(), downLeft(), downRight())
    }

    fun solveOne(puzzleText: String): Int {
        val lines = puzzleText.split("\n")
        val height = lines.size
        val width = lines[0].length

        var dog = (0 until width).flatMap { x ->
            (0 until height).map { y ->
                Point(x, y) to lines[y][x]
            }
        }.toMap()

        (0 until 100).forEach {
            println(gridToString(dog))
            println()

            dog = dog.entries.map { (point, char) ->

                val neighborsOnGrid = point.neighbours().filter { dog[it] != null }
                val neighborsOn = neighborsOnGrid.count { dog[it] == '#' }
                val newChar = runGameOfLife(char, neighborsOn)

                point to newChar
            }.toMap()


        }

        return dog.values.count { it == '#' }
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
        return 393939
    }
}