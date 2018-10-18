package Year2016

import org.junit.Assert.assertEquals
import org.junit.Test

class Puzzle2Test {
    val puzzleTwo = Puzzle2()

    @Test
    fun `first example`() {
        val input = "ULL\nRRDDD\nLURDL\nUUUUD"
        val result = puzzleTwo.decipher(input)
        assertEquals(result, "1985")
    }

    @Test
    fun `puzzle part a`() {
        val input = Puzzle1Test::class.java.getResource("/2016/puzzle2.txt").readText().replace("\r\n","\n")
        val result = puzzleTwo.decipher(input)
        assertEquals(result, "47978")
    }

    @Test
    fun `second example`() {
        val input = "ULL\nRRDDD\nLURDL\nUUUUD"
        val result = puzzleTwo.decipherWithCrazyPinpad(input)
        assertEquals(result, "5DB3")
    }

    @Test
    fun `puzzle part b`() {
        val input = Puzzle1Test::class.java.getResource("/2016/puzzle2.txt").readText().replace("\r\n","\n")
        val result = puzzleTwo.decipherWithCrazyPinpad(input)
        assertEquals(result, "659AD")
    }
}