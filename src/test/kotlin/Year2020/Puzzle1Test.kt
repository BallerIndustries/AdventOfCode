package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle1Test {
    val puzzleText = Puzzle1Test::class.java.getResource("/2020/puzzle1.txt").readText().replace("\r", "")
    val puzzle = Puzzle1()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(3404722, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(5104215, result)
    }
}

class Puzzle1 {
    fun solveOne(puzzleText: String): Int {
        return 1337
    }

    fun solveTwo(puzzleText: String): Int {
        return 420
    }
}

