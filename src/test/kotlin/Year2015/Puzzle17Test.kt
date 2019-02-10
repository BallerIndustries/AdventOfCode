package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle17Test {
    val puzzle = Puzzle17()
    val puzzleText = this::class.java.getResource("/2015/puzzle17.txt").readText().replace("\r", "")
    val exampleText = """
    """.trimIndent()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(18965440, result)
    }

    @Test
    fun `puzzle part b`() {
        // 103 too low
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(17862900, result)
    }
}

class Puzzle17 {

    fun solveOne(puzzleText: String): Int {
        return 2222
    }



    fun solveTwo(puzzleText: String): Int {
        return 100
    }
}