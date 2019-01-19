package Year2017

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle25Test {
    val puzzle = Puzzle25()
    val puzzleText = this::class.java.getResource("/2017/puzzle25.txt").readText().replace("\r", "")

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(633, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(0, result)
    }
}

class Puzzle25 {
    enum class Direction {
        LEFT, RIGHT;

        companion object {
            fun parse(text: String): Direction {
                return when (text) {
                    "left." -> LEFT
                    "right." -> RIGHT
                    else -> throw RuntimeException("asdad")
                }
            }
        }
    }

    data class State(val name: String, val zeroWrite: Int, val zeroDirection: Direction, val zeroNextState: String, val oneWrite: Int, val oneDirection: Direction, val oneNextState: String) {
        companion object {
            fun parse(text: String): State {
                val dog = text.split("\n").map { it.split(" ") }
                val name = dog[0].last().replace(":", "")

                val zeroWrite = dog[2].last().replace(".", "").toInt()
                val zeroDirection = Direction.parse(dog[3].last())
                val zeroNextState = dog[4].last().replace(".", "")

                val oneWrite = dog[6].last().replace(".", "").toInt()
                val oneDirection = Direction.parse(dog[7].last())
                val oneNextState = dog[8].last().replace(".", "")

                return State(name, zeroWrite, zeroDirection, zeroNextState, oneWrite, oneDirection, oneNextState)
            }
        }
    }

    fun solveOne(puzzleText: String): Int {
        val lines = puzzleText.split("\n")
        val initialState = lines[0].split(" ").last().replace(".", "")
        val steps = lines[1].split(" ")[5].toInt()

        val chunkyDoodles = puzzleText.split("\n\n").let { it.subList(1, it.count()) }
        val states = chunkyDoodles.map { State.parse(it) }.associate { it.name to it }
        var currentStateName = initialState
        var currentX = 0

        val paper = mutableMapOf<Int, Int>()

        (0 until steps).forEach { opNum ->
            val numberAtPos = paper[currentX] ?: 0
            val currentState = states[currentStateName]!!

            val nextDirection = if (numberAtPos == 0) {
                paper[currentX] = currentState.zeroWrite
                currentStateName = currentState.zeroNextState
                currentState.zeroDirection
            }
            else if (numberAtPos == 1) {
                paper[currentX] = currentState.oneWrite
                currentStateName = currentState.oneNextState
                currentState.oneDirection
            }
            else {
                throw RuntimeException("sdofijsdf")
            }

            currentX = if (nextDirection == Direction.LEFT) currentX - 1 else currentX + 1
        }

        return paper.values.count { it == 1 }
    }

    fun solveTwo(puzzleText: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}