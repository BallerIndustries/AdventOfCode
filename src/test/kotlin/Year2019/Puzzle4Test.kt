package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle4Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle4.txt").readText().replace("\r", "")
    val puzzle = Puzzle4()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(454, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(566, result)
    }
}

class Puzzle4 {
    fun solveOne(puzzleText: String): String {
        throw NotImplementedError()
    }

    fun solveTwo(puzzleText: String): String {
        throw NotImplementedError()
    }
}

