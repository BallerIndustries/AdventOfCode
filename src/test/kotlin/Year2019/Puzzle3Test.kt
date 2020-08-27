package Year2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle3Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle3.txt").readText().replace("\r", "")
    val puzzle = Puzzle3()

    @Test
    fun `example a`() {
        val text = "R8,U5,L5,D3\nU7,R6,D4,L4"
        val result = puzzle.solveOne(text)
        assertEquals(6, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(8015, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(163676, result)
    }
}


class Puzzle3 {

    data class Point(val x: Int, val y: Int) {
        fun manhattanDistance(other: Point): Int {
            return Math.abs(this.x - other.x) + Math.abs(this.y - other.y)
        }
    }

    fun solveOne(puzzleText: String): Int? {
        val (wire1Text, wire2Text) = puzzleText.split("\n")
        val wire1Commands = parseCommands(wire1Text)
        val wire2Commands = parseCommands(wire2Text)

        val grid = mutableMapOf<Point, Map<Int, Int>>()

        markInWire(grid, wire1Commands, 1)
        markInWire(grid, wire2Commands, 2)

        val zaza = grid.entries.filter { it.value.size >= 2 && it.key != Point(0, 0) }.map { it.key.manhattanDistance(Point(0, 0)) }
        return zaza.min()!!
    }

    private fun markInWire(grid: MutableMap<Point, Map<Int, Int>>, commands: List<Pair<Char, Int>>, wireNumber: Int) {
        var currentPoint = Point(0, 0)
        var steps = 0

        commands.forEach { command ->
            val tmp = processCommand(grid, command, currentPoint, wireNumber, steps)
            currentPoint = tmp.first
            steps = tmp.second
        }
    }

    private fun parseCommands(puzzleText: String): List<Pair<Char, Int>> {
        val commands = puzzleText.replace("\n", "").split(",").map {
            it[0] to it.substring(1).toInt()
        }
        return commands
    }

    private fun processCommand(grid: MutableMap<Point, Map<Int, Int>>, command: Pair<Char, Int>, currentPoint: Point, wireNumber: Int, steps: Int): Pair<Point, Int> {
        val point = when (command.first) {
            'U' -> moveVertical(grid, currentPoint, -command.second, wireNumber, steps)
            'D' -> moveVertical(grid, currentPoint, command.second, wireNumber, steps)
            'L' -> moveHorizontal(grid, currentPoint, -command.second, wireNumber, steps)
            'R' -> moveHorizontal(grid, currentPoint, command.second, wireNumber, steps)
            else -> throw RuntimeException("sdiofhdisf")
        }

        return point to (steps + command.second)
    }

    private fun moveVertical(grid: MutableMap<Point, Map<Int, Int>>, startPoint: Point, amount: Int, wireNumber: Int, steps: Int): Point {
        var newPoint = Point(0, 0)

        (0..Math.abs(amount)).forEach {
            val newY = if (amount > 0) startPoint.y + it else startPoint.y - it
            newPoint = Point(startPoint.x, newY)
            val existingEntry = (grid.get(newPoint) ?: mapOf()).toMutableMap()

            if (!existingEntry.containsKey(wireNumber)) {
                existingEntry[wireNumber] = steps + it
            }

            grid[newPoint] = existingEntry
        }

        return newPoint
    }

    fun moveHorizontal(grid: MutableMap<Point, Map<Int, Int>>, startPoint: Point, amount: Int, wireNumber: Int, steps: Int): Point {
        var newPoint = Point(0, 0)

        (0..Math.abs(amount)).forEach {
            val newX = if (amount > 0) startPoint.x + it else startPoint.x - it
            newPoint = Point(newX, startPoint.y)
            val existingEntry = (grid.get(newPoint) ?: mapOf()).toMutableMap()

            if (!existingEntry.containsKey(wireNumber)) {
                existingEntry[wireNumber] = steps + it
            }

            grid[newPoint] = existingEntry
        }

        return newPoint
    }

    fun solveTwo(puzzleText: String): Int {
        val (wire1Text, wire2Text) = puzzleText.split("\n")
        val wire1Commands = parseCommands(wire1Text)
        val wire2Commands = parseCommands(wire2Text)

        val grid = mutableMapOf<Point, Map<Int, Int>>()

        markInWire(grid, wire1Commands, 1)
        markInWire(grid, wire2Commands, 2)

        val zaza = grid.entries.filter { it.value.size >= 2 && it.key != Point(0, 0) }.map { it.value.values.sum() }
        return zaza.min()!!
    }
}

