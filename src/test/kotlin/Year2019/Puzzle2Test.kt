package Year2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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
    fun solveOne(puzzleText: String, noun: Long = 12, verb: Long = 2): Long {
        val list = puzzleText.split(",").map { it.toLong() }
        val state = State(list, 0, false, userInput = listOf()).writeToIndex(1, noun).writeToIndex(2, verb)
        val virtualMachine = IntCodeVirtualMachine()
        return virtualMachine.runProgram(state).list.first()
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

