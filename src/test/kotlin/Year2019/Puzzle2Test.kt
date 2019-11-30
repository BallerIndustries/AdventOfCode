package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle2Test {
    val puzzleText = Puzzle2Test::class.java.getResource("/2019/puzzle2.txt").readText().replace("\r", "")
    val puzzle = Puzzle2()

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

class Puzzle2 {
    fun solveOne(puzzleText: String): String {
        throw NotImplementedError()
    }

    fun solveTwo(puzzleText: String): String {
        throw NotImplementedError()
    }
}

