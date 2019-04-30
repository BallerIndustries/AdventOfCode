package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.lang.RuntimeException

class Puzzle24Test {
    val puzzle = Puzzle24()
    val puzzleText = this::class.java.getResource("/2015/puzzle24.txt").readText().replace("\r", "")
    val exampleText = "".trimIndent()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(255, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(334, result)
    }
}

class Puzzle24 {

    fun solveOne(puzzleText: String): Int {
        val weights = puzzleText.split("\n").map { it.toInt() }
        val compartmentWeight = weights.sum() / 3



        return 222
    }

    fun solveTwo(puzzleText: String): Int {
        return 444
    }
}