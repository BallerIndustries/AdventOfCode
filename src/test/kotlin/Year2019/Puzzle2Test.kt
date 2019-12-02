package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.lang.RuntimeException

class Puzzle2Test {
    val puzzleText = Puzzle2Test::class.java.getResource("/2019/puzzle2.txt").readText().replace("\r", "")
    val puzzle = Puzzle2()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(3267740, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(7870, result)
    }
}

class Puzzle2 {
    data class State(val list: List<Long>, val programCounter: Int = 0, val isHalted: Boolean = false) {
        fun writeToIndex(index: Int, value: Long): State {
            val newList = this.list.mapIndexed { i, it -> if (i == index) value else it }
            return this.copy(list = newList)
        }

        fun halt(): State {
            return this.copy(isHalted =  true)
        }

        fun incrementProgramCounter(amount: Int): State {
            return this.copy(programCounter = this.programCounter + amount)
        }
    }

    interface Instruction {
        fun execute(state: State): State
    }

    data class AddInstruction(val indexA: Int, val indexB: Int, val destinationIndex: Int): Instruction {
        override fun execute(state: State): State {
            val result = state.list[indexA] + state.list[indexB]
            return state.writeToIndex(destinationIndex, result)
        }
    }

    data class MultiplyInstruction(val indexA: Int, val indexB: Int, val destinationIndex: Int): Instruction {
        override fun execute(state: State): State {
            val result = state.list[indexA] * state.list[indexB]
            return state.writeToIndex(destinationIndex, result)
        }
    }

    class HaltInstruction(): Instruction {
        override fun execute(state: State): State {
            return state.halt()
        }
    }

    fun solveOne(puzzleText: String, noun: Long = 12, verb: Long = 2): Long {
        val list = puzzleText.split(",").map { it.toLong() }
        var state = State(list, 0, false).writeToIndex(1, noun).writeToIndex(2, verb)

        while (!state.isHalted) {
            val instruction = when(state.list[state.programCounter]) {
                1L -> {
                    val indexA = state.list[state.programCounter + 1].toInt()
                    val indexB = state.list[state.programCounter + 2].toInt()
                    val destinationIndex = state.list[state.programCounter + 3].toInt()
                    AddInstruction(indexA, indexB, destinationIndex)
                }
                2L -> {
                    val indexA = state.list[state.programCounter + 1].toInt()
                    val indexB = state.list[state.programCounter + 2].toInt()
                    val destinationIndex = state.list[state.programCounter + 3].toInt()
                    MultiplyInstruction(indexA, indexB, destinationIndex)
                }
                99L -> HaltInstruction()
                else -> throw RuntimeException("osidhfosidhjf")
            }

            state = instruction.execute(state).incrementProgramCounter(4)
        }

        return state.list.first()
    }

    fun solveTwo(puzzleText: String): Int {
        (0..99).forEach { noun ->
            (0..99).forEach { verb ->
                val result = solveOne(puzzleText, noun.toLong(), verb.toLong())

                if (result == 19690720L) {
                    return 100 * noun + verb
                }
            }
        }

        throw RuntimeException("sdofijsd")
    }
}

