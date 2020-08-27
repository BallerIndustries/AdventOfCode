package Year2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle13Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle13.txt").readText().replace("\r", "")
    val puzzle = Puzzle13()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(318, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(16309, result)
    }
}

class Puzzle13 {
    data class Point(val x: Int, val y: Int)

    enum class Tile(val value: Int, val char: Char) {
        EMPTY(0, ' '), WALL(1, 'X'), BLOCK(2, '#'), HORIZONTAL_PADDLE(3, 'P'), BALL(4, 'B')
    }

    fun solveOne(puzzleText: String): Int {
        val vm = IntCodeVirtualMachine()
        val program = puzzleText.split(",").map { it.toLong() } + puzzleText.map { 0L }
        val state = State(program, userInput = listOf())
        val result = vm.runProgram(state)

        val (_, map) = parseOutput(result)
        return map.values.count { it == Tile.BLOCK }
    }


    var score = -1

    private fun parseOutput(state: State): Pair<State, Map<Point, Tile>> {
        val (commandsFromZaza, newState) = state.popOffOutput()
        var index = 0

        val map = mutableMapOf<Point, Tile>()

        while (index < commandsFromZaza.size) {
            val x = commandsFromZaza[index]
            val y = commandsFromZaza[index + 1]
            val scoreOrTileId = commandsFromZaza[index + 2].toInt()

            if (x == -1L && y == 0L) {
                println("score = $scoreOrTileId")
                score = scoreOrTileId
                index += 3
                continue
            }

            val tile = Tile.values().find { it.value == scoreOrTileId }!!

            val point = Point(x.toInt(), y.toInt())
            map[point] = tile
            index += 3
        }

        return newState to map
    }

    fun solveTwo(puzzleText: String): Int {
        val vm = IntCodeVirtualMachine()
        val program = puzzleText.split(",").map { it.toLong() } + (0 until puzzleText.length * 2).map { 0L }
        var state = State(program, userInput = listOf(0))
        state = vm.runProgram(state.writeToIndex(0, 2))

        val tmp = parseOutput(state)
        state = tmp.first
        val pixelBuffer = tmp.second.toMutableMap()

        while (!state.isHalted) {
            val ballPosition = pixelBuffer.entries.find { it.value == Tile.BALL }!!.key
            val paddlePosition = pixelBuffer.entries.find { it.value == Tile.HORIZONTAL_PADDLE }!!.key.x

            val userInput = when {
                (ballPosition.x < paddlePosition) -> -1L
                (ballPosition.x > paddlePosition) -> 1L
                else -> 0L
            }

            state = vm.runProgram(state.addUserInput(userInput))
            val tmp = parseOutput(state)
            state = tmp.first
            mergePixelBuffer(pixelBuffer, tmp.second)
        }

        return score
    }

    private fun mergePixelBuffer(pixelBuffer: MutableMap<Point, Tile>, deltas: Map<Point, Tile>) = pixelBuffer.putAll(deltas)

    private fun renderScreen(map: Map<Point, Tile>): String {
        val maxX = map.keys.maxBy { it.x }!!.x
        val maxY = map.keys.maxBy { it.y }!!.y

        return (0..maxY).map { y ->
            (0..maxX).map { x ->
                map[Point(x, y)]?.char ?: throw RuntimeException("Unable to find pixel at point = ($x, $y)")
            }.joinToString("")
        }.joinToString("\n")
    }
}

