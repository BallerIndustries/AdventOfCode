package Year2018

import junit.framework.Assert
import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle9Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle9.txt").readText().replace("\r", "")
    val puzzle = Puzzle9()

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

    class Puzzle9 {
        fun solveOne(puzzleText: String): String {
            return ""
        }

        fun solveTwo(puzzleText: String): String {
            return ""
        }
    }
}