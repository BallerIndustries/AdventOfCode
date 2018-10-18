package Year2016

import org.junit.Assert.assertEquals
import org.junit.Test

class Puzzle6Test {
    val puzzle = Puzzle6()

    @Test
    fun `this junk should error correct into easter`() {
        val input =
                "eedadn\n" +
                "drvtee\n" +
                "eandsr\n" +
                "raavrd\n" +
                "atevrs\n" +
                "tsrnev\n" +
                "sdttsa\n" +
                "rasrtv\n" +
                "nssdts\n" +
                "ntnada\n" +
                "svetve\n" +
                "tesnvt\n" +
                "vntsnd\n" +
                "vrdear\n" +
                "dvrsen\n" +
                "enarar"

        val message = puzzle.errorCorrect(input)
        assertEquals(message, "easter")
    }

    @Test
    fun `puzzle part a`() {
        val input = Puzzle1Test::class.java.getResource("/2016/puzzle6.txt").readText().replace("\r\n","\n")
        val result = puzzle.errorCorrect(input)
        assertEquals(result, "tkspfjcc")
    }

    @Test
    fun `puzzle part b`() {
        val input = Puzzle1Test::class.java.getResource("/2016/puzzle6.txt").readText().replace("\r\n","\n")
        val result = puzzle.errorCorrectTwo(input)
        assertEquals(result, "xrlmbypn")
    }
}

