package Year2018

import junit.framework.Assert
import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle15Test {
    val puzzleText = this::class.java.getResource(
            "/2018/puzzle15.txt").readText()
    val puzzle = Puzzle15()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("a", result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("b", result)
    }

    class Puzzle15 {
        fun solveOne(puzzleText: String): String {
            return ""
        }

        fun solveTwo(puzzleText: String): String {
            return ""
        }
    }
}