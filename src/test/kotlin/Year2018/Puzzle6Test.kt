package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle6Test {
    val puzzleText = this::class.java.getResource(
            "/2018/puzzle6.txt").readText()
    val puzzle = Puzzle6()

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

class Puzzle6 {
    fun solveOne(puzzleText: String): Int {
        return 1
    }

    fun solveTwo(puzzleText: String): Int {
        return 2
    }
}
