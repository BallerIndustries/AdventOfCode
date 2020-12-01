package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle1Test {
    val puzzleText = Puzzle1Test::class.java.getResource("/2020/puzzle1.txt").readText().replace("\r", "")
    val puzzle = Puzzle1()

    @Test
    fun `example part a`() {
        val puzzleText = "1721\n" +
                "979\n" +
                "366\n" +
                "299\n" +
                "675\n" +
                "1456"

        val result = puzzle.solveOne(puzzleText)
        assertEquals(514579, result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = "1721\n" +
                "979\n" +
                "366\n" +
                "299\n" +
                "675\n" +
                "1456"

        val result = puzzle.solveTwo(puzzleText)
        assertEquals(241861950, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(964875, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(158661360, result)
    }
}

class Puzzle1 {
    fun solveOne(puzzleText: String): Int {
        val ints: List<Int> = puzzleText.split("\n").map { it.toInt() }
        var i = 0
        var j = 1

        while (i < ints.size) {
            while (j < ints.size) {
                if (i != j && ints[i] + ints[j] == 2020) {
                    return ints[i] * ints[j]
                }
                j++
            }

            j=0;
            i++;
        }

        throw RuntimeException()
    }

    fun solveTwo(puzzleText: String): Int {
        val ints: List<Int> = puzzleText.split("\n").map { it.toInt() }
        var i = 0
        var j = 1
        var k = 2;

        while (i < ints.size) {
            j = 0
            while (j < ints.size) {
                k = 0
                while (k < ints.size) {
                    if (i != j && i != k && ints[i] + ints[j] + ints[k] == 2020) {
                        return ints[i] * ints[j] * ints[k]
                    }
                    k++
                }
                j++
            }

            i++;
        }

        throw RuntimeException()
    }
}

