package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle21Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle21.txt").readText().replace("\r", "")
    val puzzle = Puzzle21()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(964875, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(158661360, result)
    }

    @Test
    fun `example part a`() {
        val puzzleText = ""
        val result = puzzle.solveOne(puzzleText)
        assertEquals(514579, result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = ""
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(241861950, result)
    }
}

class Puzzle21 {
    fun solveOne(puzzleText: String): Int {
        return 1
    }

    fun solveTwo(puzzleText: String): Int {
        return 1
    }
}

