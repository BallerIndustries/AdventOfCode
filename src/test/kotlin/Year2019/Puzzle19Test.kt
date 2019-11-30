package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle19Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle19.txt").readText().replace("\r", "")
    val puzzle = Puzzle19()

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

class Puzzle19 {
    fun solveOne(puzzleText: String): String {
        throw NotImplementedError()
    }

    fun solveTwo(puzzleText: String): String {
        throw NotImplementedError()
    }
}

