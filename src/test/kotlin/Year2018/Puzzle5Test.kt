package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle5Test {
    val puzzleText = this::class.java.getResource(
            "/2018/puzzle5.txt").readText()
    val puzzle = Puzzle5()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(11108, result.length)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(5094, result)
    }

    @Test
    fun `collapse test`() {
        val collapsed = puzzle.collapse("aA")
        assertEquals(collapsed, "")
    }

    @Test
    fun example() {
        val collapsed = puzzle.solveOne("dabAcCaCBAcCcaDA")
        assertEquals("dabCBAcaDA", collapsed)
    }
}

class Puzzle5 {
    fun solveOne(puzzleText: String): String {
        var previous = puzzleText
        var current = collapse(puzzleText)

        while (previous.length != current.length) {
            previous = current
            current = collapse(current)
        }

        return current
    }

    fun solveTwo(puzzleText: String): Int {
        val dog = ('a'..'z').map { char ->

            val newText = puzzleText
                    .replace(char.toString(), "")
                    .replace(char.toUpperCase().toString(), "")

            char to solveOne(newText).length
        }

        return dog.minBy { it.second }!!.second
    }

    fun collapse(jur: String): String {
        var buffer = jur

        ('a'..'z').forEach {
            val pattern = "$it${it.toUpperCase()}"
            val reversedPattern = pattern.reversed()
            buffer = buffer.replace(Regex(pattern), "")
            buffer = buffer.replace(Regex(reversedPattern), "")
        }

        return buffer
    }
}