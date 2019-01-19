package Year2017

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.lang.IndexOutOfBoundsException

class Puzzle22Test {
    val puzzle = Puzzle22()
    val puzzleText = this::class.java.getResource("/2017/puzzle22.txt").readText().replace("\r", "")

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(6722, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(7620, result)
    }
}

class Puzzle22 {
    fun solveOne(puzzleText: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun solveTwo(puzzleText: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}