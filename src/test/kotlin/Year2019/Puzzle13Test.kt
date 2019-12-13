package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

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
        assertEquals("a", result)
    }
}

class Puzzle13 {
    data class Point(val x: Int, val y: Int)

    enum class Tile(val value: Int, val char: Char) {
        EMPTY(0, ' '), WALL(1, 'X'), BLOCK(2, '#'), HORIZONTAL_PADDLE(3, 'P'), BALL(4, 'B')
    }

    fun solveOne(puzzleText: String): Int {
        return 2
//        val vm = IntCodeVirtualMachine()
//        val program = puzzleText.split(",").map { it.toLong() } + puzzleText.map { 0L }
//        val state = State(program, userInput = listOf())
//        val result = vm.runProgram(state)
//
//        val map = readOutputIntoPixelBuffer(result)
//        return map.values.count { it == Tile.BLOCK }
    }

    val outputs = mutableListOf<List<Long>>()

    private fun parseOutput(state: State): Pair<State, Map<Point, Tile>> {
        val (commandsFromZaza, newState) = state.popOffOutput()
        outputs.add(commandsFromZaza)
        var index = 0

        val map = mutableMapOf<Point, Tile>()

        while (index < commandsFromZaza.size) {
            val x = commandsFromZaza[index]
            val y = commandsFromZaza[index + 1]
            val scoreOrTileId = commandsFromZaza[index + 2].toInt()

            if (x == -1L && y == 0L) {
                println("score = $scoreOrTileId")
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

    fun solveTwo(puzzleText: String): String {
        val vm = IntCodeVirtualMachine()
        val program = puzzleText.split(",").map { it.toLong() } + (0 until puzzleText.length * 2).map { 0L }
        var state = State(program, userInput = listOf(0))
        state = vm.runProgram(state.writeToIndex(0, 2))

        val tmp = parseOutput(state)
        state = tmp.first
        val pixelBuffer = tmp.second.toMutableMap()

        val text = renderScreen(pixelBuffer)
        println(text)
        println()
        var previousBallPosition = -1

        while (state.isHalted == false) {
//            Thread.sleep(500)

            val ballPosition = pixelBuffer.entries.find { it.value == Tile.BALL }!!.key
            val paddlePosition = pixelBuffer.entries.find { it.value == Tile.HORIZONTAL_PADDLE }!!.key.x

//            println("ball $ballPosition paddle $paddlePosition")

            val userInput = when {
                (ballPosition.x < paddlePosition) -> -1L
                (ballPosition.x > paddlePosition) -> 1L
//                (ballPosition.x == paddlePosition && previousBallPosition < paddlePosition) -> -1L
//                (ballPosition.x == paddlePosition && previousBallPosition > paddlePosition) -> 1L
                else -> 0L
            }

            state = vm.runProgram(state.addUserInput(userInput))
            val tmp = parseOutput(state)
            state = tmp.first
            mergePixelBuffer(pixelBuffer, tmp.second)

//            val text = renderScreen(pixelBuffer)
//            println(text)
//            println()

            previousBallPosition = ballPosition.x
        }

        throw NotImplementedError()
    }

    private fun mergePixelBuffer(pixelBuffer: MutableMap<Point, Tile>, deltas: Map<Point, Tile>) {
        deltas.forEach { (key, tile) ->

            if (tile == Tile.BALL) {
                val previousBall = pixelBuffer.entries.find { it.value == Tile.BALL }?.key

                if (previousBall != null) {
                    pixelBuffer.remove(previousBall)
                }
            }

            pixelBuffer[key] = tile
        }
    }

    private fun renderScreen(map: Map<Point, Tile>): String {
        val maxX = map.keys.maxBy { it.x }!!.x
        val maxY = map.keys.maxBy { it.y }!!.y

        val text = (0..maxY).map { y ->
            (0..maxX).map { x ->
                map[Point(x, y)]?.char ?: ' '
            }.joinToString("")
        }.joinToString("\n")
        return text
    }
}

