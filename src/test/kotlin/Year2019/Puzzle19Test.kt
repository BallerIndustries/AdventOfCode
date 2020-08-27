package Year2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle19Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle19.txt").readText().replace("\r", "")
    val puzzle = Puzzle19()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(112, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("a", result)
    }
}

class Puzzle19 {
    data class Point(val x: Int, val y: Int) {
        fun manhattanDistance(other: Point): Int {
            return Math.abs(this.x - other.x) + Math.abs(this.y - other.y)
        }
    }

    fun solveOne(puzzleText: String): Int {
        val grid = gridMeAGrid(puzzleText)
        return grid.values.count { it == '#' }
    }

    private fun gridMeAGrid(puzzleText: String): Map<Point, Char> {
        val program = puzzleText.split(",").map { it.toLong() } + puzzleText.map { 0L }
        val baseState = State(program, userInput = listOf())
        val virtualMachine = IntCodeVirtualMachine()

        val grid = (0L until 50L).flatMap { x ->
            (0L until 50L).map { y ->
                val char = horseDogCheese(baseState, x, y, virtualMachine)

                Point(x.toInt(), y.toInt()) to char
            }
        }.toMap()
        return grid
    }

    private fun horseDogCheese(baseState: State, x: Long, y: Long, virtualMachine: IntCodeVirtualMachine): Char {
        val state = baseState.addUserInput(x).addUserInput(y)
        val result = virtualMachine.runProgram(state)
        val aaa = result.outputList.first().toInt()

        val char = when (aaa) {
            0 -> '.'
            1 -> '#'
            else -> throw RuntimeException()
        }
        return char
    }

    fun solveTwo(puzzleText: String): Int {
        val grid = gridMeAGrid(puzzleText)

        renderGrid(grid)

        val origin = Point(0, 0)
        val nearestPoints = grid.entries
            .filter { it.value == '#' }
            .map { it.key }
            .sortedBy { origin.manhattanDistance(it) }

        val octopus = nearestPoints.first { point ->
            val topRight = point.copy(x = point.x + 9)
            val bottomLeft = point.copy(y = point.y + 9)
            val bottomRight = point.copy(x = point.x + 9, y = point.y + 9)
            grid[topRight] == '#' && grid[bottomLeft] == '#' && grid[bottomRight] == '#'
        }

        return octopus.x * 10_000 + octopus.y
    }

    private fun renderGrid(grid: Map<Point, Char>) {
        val maxX = grid.keys.maxBy { it.x }!!.x
        val maxY = grid.keys.maxBy { it.y }!!.y
        val minX = grid.keys.minBy { it.x }!!.x
        val minY = grid.keys.minBy { it.y }!!.y

        val buffer = (minY .. maxY).map { y ->
            (minX .. maxX).map { x ->
                grid[Point(x, y)] ?: '?'
            }.joinToString("")
        }.joinToString("\n")

        println(buffer)
    }
}

