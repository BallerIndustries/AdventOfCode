package year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle18Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle18.txt").readText().replace("\r", "")
    val puzzle = Puzzle18()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(518028, result)
    }

    @Test
    fun `part b 603 solve should be same as part a 603 solve`() {
        val partAResult = puzzle.solveOne(puzzleText, 603)
        val partBResult = puzzle.solveTwo(puzzleText, 603)

        assertEquals(partAResult, partBResult)
    }

    @Test
    fun `part b 1000 solve should be same as part a 1000 solve`() {
        val partAResult = puzzle.solveOne(puzzleText, 1000)
        val partBResult = puzzle.solveTwo(puzzleText, 1000)

        assertEquals(partAResult, partBResult)
    }

    @Test
    fun `puzzle part b`() {

        // NOT 149872, too low
        // NOT 213312, too high
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(213057, result)
    }

    class Puzzle18 {


        fun solveTwo(puzzleText: String, theFun: Int = 1000000000): Int {
            var state = parseInitialState(puzzleText)
            val boardStateToCycle = mutableMapOf<String, Int>()

            (1 .. 700).forEach { cycleNum ->
                val text = stateText(state)

                if (boardStateToCycle.containsKey(text)) {
                    val firstSeen = boardStateToCycle[text]!!
                    val delta = cycleNum - firstSeen
                    println("A CYCLE HAS BEEN FOUND! cycleNumber = $cycleNum firstSeen = $firstSeen delta = $delta")
                }
                else {
                    boardStateToCycle[text] = cycleNum
                }

                state = nextState(state)
            }

            // Now we have a map of cycle -> boardState
            val firstCycle = 575

            val normalisedCycleToBoardState = boardStateToCycle.mapNotNull { (state, cycle) ->

                if (cycle in firstCycle..602) cycle - firstCycle to state else null

            }.toMap()

            val index = (theFun - firstCycle + 1) % normalisedCycleToBoardState.count()

            val cycleToScore = normalisedCycleToBoardState
                .map { (key, value) -> key to value.count { it == '|' } * value.count { it == '#' } }
                .toMap()


            println(cycleToScore)

            return cycleToScore[index]!!
        }

        fun solveOne(puzzleText: String, cycles: Int = 10): Int {
            var state = parseInitialState(puzzleText)
            (1 .. cycles).forEach { state = nextState(state) }

            return state.values.count { it == '|' } * state.values.count { it == '#' }
        }

        private fun stateText(state: Map<Point, Char>): String {
            val dog = (0 until 50).map { y ->
                (0 until 50).map { x ->

                    val point = Point(x, y)
                    state[point]
                }.joinToString("")
            }.joinToString("\n")

            return dog
        }

        private fun nextState(state: Map<Point, Char>): Map<Point, Char> {
            val dog = (0 until 50).flatMap { x ->
                (0 until 50).map { y ->
                    val thisPoint = Point(x, y)
                    val thisChar = state[thisPoint]!!
                    val adjacent = thisPoint.getAdjacent().mapNotNull { state[it] }

                    val numAdjacentTrees = adjacent.count { it == '|' }
                    val numAdjacentLumberyards = adjacent.count { it == '#' }

                    val nextChar = when {
                        thisChar == '.' && numAdjacentTrees >= 3 -> '|'
                        thisChar == '.' && numAdjacentTrees < 3 -> '.'
                        thisChar == '|' && numAdjacentLumberyards >= 3 -> '#'
                        thisChar == '|' && numAdjacentLumberyards < 3 -> '|'
                        thisChar == '#' && numAdjacentTrees >= 1 && numAdjacentLumberyards >= 1 -> '#'
                        thisChar == '#' && numAdjacentTrees == 0 || numAdjacentLumberyards == 0 -> '.'
                        else -> throw RuntimeException("Fuck!")
                    }

                    thisPoint to nextChar
                }
            }.toMap()

            return dog
        }

        private fun parseInitialState(puzzleText: String): Map<Point, Char> {
            val lines = puzzleText.split("\n")
            val width = lines[0].length
            val height = lines.size

            val state = (0 until width).flatMap { x ->
                (0 until height).map { y ->

                    val point = Point(x, y)
                    val char = lines[y][x]

                    point to char
                }
            }.toMap()
            return state
        }


        data class Point(val x: Int, val y: Int) {
            fun getAdjacent(): List<Point> {
                return listOf(
                    this.copy(x - 1, y),                // LEFT
                    this.copy(x - 1, y - 1),         // TOP LEFT
                    this.copy(x, y - 1),                // TOP
                    this.copy(x + 1, y - 1),         // TOP RIGHT
                    this.copy(x + 1, y),                // RIGHT
                    this.copy(x + 1, y + 1),         // BOTTOM RIGHT
                    this.copy(x, y + 1),                // BOTTOM
                    this.copy(x - 1, y + 1)         // BOTTOM LEFT
                )
            }
        }
    }
}