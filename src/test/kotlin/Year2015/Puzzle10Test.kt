package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle10Test {
    val puzzle = Puzzle10()
    val puzzleText = this::class.java.getResource("/2015/puzzle10.txt").readText().replace("\r", "")
    val exampleText = "1"

    @Test
    fun `puzzle part a`() {
        // 82350 is too low
        // 107312 is too low
        val result = puzzle.solveOne(puzzleText)
        assertEquals(252594, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(3579328, result)
    }

    @Test
    fun `look and say one iteration`() {
        val result = puzzle.looksay("1")
        assertEquals("11", result)
    }

    @Test
    fun `look and say two iterations`() {
        val result = puzzle.looksay("11")
        assertEquals("21", result)
    }

    @Test
    fun `look and say 3 iterations`() {
        val result = puzzle.looksay("21")
        assertEquals("1211", result)
    }

    @Test
    fun `look and say 4 iterations`() {
        val result = puzzle.looksay("1211")
        assertEquals("111221", result)
    }

    @Test
    fun `look and say 5 iterations`() {
        val result = puzzle.looksay("111221")
        assertEquals("312211", result)
    }

    @Test
    fun `look and say 6 iterations`() {
        val result = puzzle.looksay("312211")
        assertEquals("13112221", result)
    }
}

class Puzzle10 {

    fun looksay(s: String): String {
        val out = StringBuilder()
        var i = 0
        while (i < s.length) {
            var count = 1
            val c = s[i]
            while (i + 1 < s.length && s[i + 1] == c) {
                ++i
                ++count
            }
            out.append(count)
            out.append(c)
            i++
        }
        return out.toString()
    }

    fun solveOne(puzzleText: String): Int {
        var current = puzzleText
        (1 .. 40).forEach { current = looksay(current) }
        return current.length
    }

    fun solveTwo(puzzleText: String): Int {
        var current = puzzleText
        (1 .. 50).forEach { current = looksay(current) }
        return current.length
    }
}