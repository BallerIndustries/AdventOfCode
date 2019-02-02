package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle8Test {
    val puzzle = Puzzle8()
    val puzzleText = this::class.java.getResource(
            "/2015/puzzle8.txt").readText().replace("\r", "")

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(589999, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(18838115, result)
    }

    @Test
    fun `length of empty string`() {
        assertEquals(0, puzzle.lengthWithoutSpecial("\"\""))
    }

    @Test
    fun `length of abc`() {
        assertEquals(3, puzzle.lengthWithoutSpecial("\"abc\""))
    }
    @Test

    fun `length of aaa slash aaa`() {
        assertEquals(7, puzzle.lengthWithoutSpecial("\"aaa\\\"aaa\""))
    }
}

class Puzzle8 {

    fun solveOne(puzzleText: String): Int {
        puzzleText.split("\n").forEach { line ->

            val originalLength = line.length
            val jur = lengthWithoutSpecial(line)








        }

        return 0
    }

    fun lengthWithoutSpecial(line: String): Int {
        val stripped = line.substring(1, line.length - 1)
        var buffer = ""

        var index = 1


        return stripped.replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .length
    }

    fun solveTwo(puzzleText: String): Int {
       return 0
    }
}