package Year2015

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle8Test {
    val puzzle = Puzzle8()
    val puzzleText = this::class.java.getResource(
            "/2015/puzzle8.txt").readText().replace("\r", "")

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(1371, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(2117, result)
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

    @Test
    fun `length of something crazy`() {
        assertEquals(1, puzzle.lengthWithoutSpecial("\"\\x27\""))
    }
}

class Puzzle8 {

    fun solveOne(puzzleText: String): Int {
        return puzzleText.split("\n").sumBy { line ->
            val originalLength = line.length
            val lengthWithoutSpecial = lengthWithoutSpecial(line)

            originalLength - lengthWithoutSpecial
        }
    }

    fun lengthWithoutSpecial(line: String): Int {
        val stripped = line.substring(1, line.length - 1)

        val dog = stripped.replace("\\\"", "\"")
                .replace("\\\\", "\\")


        return replaceHexNotation(dog).length
    }

    fun encode(line: String): String {
        var buffer = ""

        line.forEach { char ->
            when (char) {
                '"' -> buffer += "\\\""
                '\\' -> buffer += "\\\\"
                else -> buffer += char
            }
        }

        return "\"$buffer\""
    }

    fun replaceHexNotation(text: String): String {
        val marker = text.map { true }.toMutableList()
        var i = 3

        while ( i < text.length) {
            if (text[i-3] == '\\' && text[i-2] == 'x' && isHex(text[i-1]) && isHex(text[i])) {
                marker[i-3] = false
                marker[i-2] = true
                marker[i-1] = false
                marker[i] = false
                i += 4
            }
            else {
                i++
            }
        }

        val dog = text.filterIndexed { index, char ->
            marker[index]
        }

        return dog
    }

    private fun isHex(c: Char): Boolean {
        return ('a'..'f').contains(c) || ('0'..'9').contains(c)
    }

    fun solveTwo(puzzleText: String): Int {
        return puzzleText.split("\n").sumBy { line ->
            val originalLength = line.length
            val encodedLength = encode(line).length

            encodedLength - originalLength
        }
    }
}