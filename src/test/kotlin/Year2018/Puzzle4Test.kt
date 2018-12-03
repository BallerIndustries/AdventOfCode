package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle4Test {
    val puzzleText = this::class.java.getResource(
            "/2018/puzzle4.txt").readText()
    val puzzle = Puzzle4()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(0, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(0, result)
    }
}

class Puzzle4 {
    fun solveOne(puzzleText: String): Int {
        return 1
    }

    fun solveTwo(puzzleText: String): Int {
        return 2
    }
}
