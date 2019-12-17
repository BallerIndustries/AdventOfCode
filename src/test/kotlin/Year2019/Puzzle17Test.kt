package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle17Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle17.txt").readText().replace("\r", "")
    val puzzle = Puzzle17()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(7404, result)
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
        val grid = getMeAGrid(puzzleText)

        return grid.entries.filter {
            it.value == '#'
        }.filter { (point, value) ->
            point.neighbours().all {
                grid[it] == '#'
            }
        }.sumBy { (point, _) ->
            point.x * point.y
        }

        // Locate all scaffold intersections; for each, its alignment parameter is the distance between its
        // left edge and the left edge of the view multiplied by the distance between its top edge and the top
        // edge of the view. Here, the intersections from the above image are marked O:
    }

    private fun getMeAGrid(puzzleText: String): Map<Point, Char> {
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
        return grid
    }

    fun solveTwo(puzzleText: String): String {

        val grid = getMeAGrid(puzzleText)
        val widths = getHorizontalWidths(grid)
        val heights = getVerticalHeights(grid)


        println(widths)

        throw NotImplementedError()
    }

    private fun getVerticalHeights(grid: Map<Point, Char>) {
        val mutableGrid = grid.toMutableMap()
        val scaffoldPoints = grid.filter { it.value == '#' || it.value == '^' }.map { it.key }

        val heights = scaffoldPoints.map { startingPoint ->
            val i = moveAndCount(startingPoint, mutableGrid) { it.down() } + moveAndCount(startingPoint, mutableGrid) { it.up() }
            i
        }.filter {
            it > 1
        }.map {
            it - 1
        }
    }

    private fun getHorizontalWidths(grid: Map<Point, Char>): List<Int> {
        // Get the widths of all horizontal lines
        val mutableGrid = grid.toMutableMap()
        val scaffoldPoints = grid.filter { it.value == '#' || it.value == '^' }.map { it.key }

        val widths = scaffoldPoints.map { startingPoint ->
            moveAndCount(startingPoint, mutableGrid) { it.right() } + moveAndCount(startingPoint, mutableGrid) { it.left() }
        }.filter {
            it > 1
        }.map {
            it - 1
        }
        return widths
    }

    private fun moveAndCount(startingPoint: Point, mutableGrid: MutableMap<Point, Char>, stepper: (Point) -> Point): Int {
        var currentPoint = startingPoint
        var width = 0

        // Mark all to the right as '.'
        while (mutableGrid[currentPoint] == '#') {
            mutableGrid[currentPoint] = '.'
            currentPoint = stepper(currentPoint)
            width++
        }

        return width
    }

    fun isEmptyOrNull(char: Char?): Boolean {
        return char == null || char == '.'
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

