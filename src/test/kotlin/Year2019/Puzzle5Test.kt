package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle5Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle5.txt").readText().replace("\r", "")
    val puzzle = Puzzle5()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(555, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(566, result)
    }
}

class Puzzle5 {
    fun solveOne(puzzleText: String): String {
        throw NotImplementedError()
    }

    fun solveTwo(puzzleText: String): String {
        throw NotImplementedError()
    }
}

