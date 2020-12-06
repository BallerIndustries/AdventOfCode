package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle6Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle6.txt").readText().replace("\r", "")
    val puzzle = Puzzle6()

    @Test
    fun `example part b`() {
        val puzzleText = "abc\n" +
                "\n" +
                "a\n" +
                "b\n" +
                "c\n" +
                "\n" +
                "ab\n" +
                "ac\n" +
                "\n" +
                "a\n" +
                "a\n" +
                "a\n" +
                "a\n" +
                "\n" +
                "b"
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(6, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(6748, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(3445, result)
    }
}

class Puzzle6 {
    fun solveOne(puzzleText: String): Int {
        val groups = puzzleText.split("\n\n")

        return groups.sumBy { group ->
            val setto = mutableSetOf<Char>()

            val flatGroup = group.replace("\n", "")

            flatGroup.forEach { char: Char ->
                setto.add(char)
            }


            setto.size
        }
    }

    fun solveTwo(puzzleText: String): Int {
        val groups = puzzleText.split("\n\n")

        return groups.sumBy { group ->
            val peopleInGroup = group.count { it == '\n' } + 1
            val charToCount = mutableMapOf<Char, Int>()

            group.replace("\n", "").forEach {
                charToCount[it] = (charToCount[it] ?: 0) + 1
            }

            val result = charToCount.count { it.value == peopleInGroup }
            println("peopleInGroup = ${peopleInGroup}")
            println("charToCount = ${charToCount}")
            println("result = ${result}")
            println()
            result
        }
    }
}

