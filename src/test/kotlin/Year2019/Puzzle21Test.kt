package Year2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle21Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle21.txt").readText().replace("\r", "")
    val puzzle = Puzzle21()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("a", result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("a", result)
    }
}

class Puzzle21 {
    fun solveOne(puzzleText: String): String {
        throw NotImplementedError()
    }

    fun solveTwo(puzzleText: String): String {
        throw NotImplementedError()
    }
}

