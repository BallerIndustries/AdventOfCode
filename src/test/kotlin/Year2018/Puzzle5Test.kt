package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle5Test {
    val puzzleText = this::class.java.getResource(
            "/2018/puzzle5.txt").readText()
    val puzzle = Puzzle5()

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

class Puzzle5 {
    fun solveOne(puzzleText: String): Int {
        return 1
    }

    fun solveTwo(puzzleText: String): Int {
        return 2
    }
}
