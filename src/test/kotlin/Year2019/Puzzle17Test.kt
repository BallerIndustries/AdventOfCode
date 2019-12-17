package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle17Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle17.txt").readText().replace("\r", "")
    val puzzle = Puzzle17()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("a", result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("a", result)
    }
}

class Puzzle17 {
    data class Point(val x: Int, val y: Int) {
        fun up() = this.copy(y = this.y - 1)
        fun down() = this.copy(y = this.y + 1)
        fun right() = this.copy(x = this.x + 1)
        fun left() = this.copy(x = this.x - 1)
        fun neighbours() = listOf(up(), down(), left(), right())
    }

    fun solveOne(puzzleText: String): Int {
        val program = puzzleText.split(",").map { it.toLong() } + puzzleText.map { 0L }
        val state = State(program, userInput = listOf())
        val vm = IntCodeVirtualMachine()

        val result = vm.runProgram(state)

        val text = result.outputList.map { it.toChar() }.joinToString("")
        println(text)


        val jur = text.split("\n")
        val grid = mutableMapOf<Point, Char>()

        jur.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                grid[Point(x, y)] = char
            }
        }

        val cheeses = grid.entries.filter {
            it.value == '#'
        }.filter { (point, value) ->
            point.neighbours().all {
                grid[it] == '#'
            }
        }.map { (point, _) ->
            point.x * point.y
        }.sum()


        return cheeses

        // Locate all scaffold intersections; for each, its alignment parameter is the distance between its
        // left edge and the left edge of the view multiplied by the distance between its top edge and the top
        // edge of the view. Here, the intersections from the above image are marked O:


    }

    fun solveTwo(puzzleText: String): String {
        throw NotImplementedError()
    }
}

