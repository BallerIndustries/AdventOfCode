package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle5Test {
    val puzzleText = Puzzle5Test::class.java.getResource("/2019/puzzle5.txt").readText().replace("\r", "")
    val puzzle = Puzzle5()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(12896948L, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(7704130L, result)
    }
}

class Puzzle5 {
    private fun runProgram(puzzleText: String, userInput: List<Long>): Long? {
        val list = puzzleText.split(",").map { it.toLong() }
        val state = State(list, 0, false, userInput = userInput)
        val virtualMachine = IntCodeVirtualMachine()

        val result = virtualMachine.runProgram(state)
        return result.outputList.last()
    }

    fun solveOne(puzzleText: String): Long? {
        return runProgram(puzzleText, listOf(1))
    }

    fun solveTwo(puzzleText: String): Long? {
        return runProgram(puzzleText, listOf(5))
    }
}