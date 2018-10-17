package Year2016

import org.junit.Assert.assertEquals
import org.junit.Test

class Puzzle3Test {

    val puzzle = Puzzle3()

    @Test
    fun `a list with one impossible triangle`() {
        val input = "5 10 25"
        val count = puzzle.countValidTriangles(input)
        assertEquals(count, 0)
    }

    @Test
    fun `puzzle part a`() {
        val input = Puzzle1Test::class.java.getResource("/2016/puzzle3.txt").readText()
        val result = puzzle.countValidTriangles(input)
        assertEquals(result, 862)
    }

    @Test
    fun `a vertically grouped list of three with one impossible triangle`() {
        val input = "5 5 5\n10 6 7\n25 7 9"
        val result = puzzle.countValidTrianglesVertically(input)
        assertEquals(result, 2)
    }

    @Test
    fun `puzzle part b`() {
        val input = Puzzle1Test::class.java.getResource("/2016/puzzle3.txt").readText()
        val result = puzzle.countValidTrianglesVertically(input)
        assertEquals(result, 1577)
    }
}

