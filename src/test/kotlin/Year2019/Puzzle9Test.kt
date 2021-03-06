package Year2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle9Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle9.txt").readText().replace("\r", "")
    val puzzle = Puzzle9()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(2457252183L, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(70634L, result)
    }
}

class Puzzle9 {
    fun solveOne(puzzleText: String): Long? {
        val split = puzzleText.split(",")
        val program = split.map { it.toLong() } + (0 until split.size * 2).map { 0L }
        val state = State(program, userInput = listOf(1))
        val virtualMachine = IntCodeVirtualMachine()
        val result = virtualMachine.runProgram(state)
        return result.outputList.first()
    }

    fun solveTwo(puzzleText: String): Long? {
        val split = puzzleText.split(",")
        val program = split.map { it.toLong() } + (0 until split.size * 2).map { 0L }
        val state = State(program, userInput = listOf(2))
        val virtualMachine = IntCodeVirtualMachine()
        val result = virtualMachine.runProgram(state)
        return result.outputList.first()
    }
}