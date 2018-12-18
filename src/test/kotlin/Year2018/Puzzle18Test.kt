package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle18Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle18.txt").readText().replace("\r", "")
    val puzzle = Puzzle18()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("a", result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("b", result)
    }

    class Puzzle18 {


        fun solveTwo(puzzleText: String): Int {
            var state = parseInitialState(puzzleText)
            val boardStateToCycle = mutableMapOf<String, Int>()

            (0 until 1000).forEach { cycleNum ->
                val text = stateText(state)

                if (boardStateToCycle.containsKey(text)) {
                    val firstSeen = boardStateToCycle[text]!!
                    val delta = cycleNum - firstSeen
                    println("A CYCLE HAS BEEN FOUND! firstSeen = $firstSeen delta = $delta")
                }
                else {
                    boardStateToCycle[text] = cycleNum
                }

                state = nextState(state)
            }

            // Now we have a map of cycle -> boardState
            val firstCylce = boardStateToCycle.values.min()!!

            val normalisedCycleToBoardState = boardStateToCycle.map { (state, cycle) ->
                cycle - firstCylce to state
            }.toMap()

            val theFun = 1000000000

            val hopefully = (theFun - firstCylce) % boardStateToCycle.count()

            val hugeNumberState = normalisedCycleToBoardState[hopefully]!!

            return hugeNumberState.count { it == '|' } * hugeNumberState.count { it == '#' }
        }

        fun solveOne(puzzleText: String): Int {
            var state = parseInitialState(puzzleText)
            val set = mutableMapOf<String, Int>()

            val max = 1000000000

            (0 until max ).forEach { cycleNum ->
                if (cycleNum % 100000 == 0) {
                    println("${cycleNum.toDouble() / max.toDouble()} %")
                }

                val text = stateText(state)

                if (set.containsKey(text)) {
                    val firstSeen = set[text]!!
                    val delta = cycleNum - firstSeen
                    println("A CYCLE HAS BEEN FOUND! firstSeen = $firstSeen delta = $delta")
                }
                else {
                    set[text] = cycleNum
                }

                state = nextState(state)
            }

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