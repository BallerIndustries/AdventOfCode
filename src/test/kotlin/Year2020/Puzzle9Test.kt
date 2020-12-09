package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle9Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle9.txt").readText().replace("\r", "")
    val puzzle = Puzzle9()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(18272118, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(2186361, result)
    }

    @Test
    fun `example part a`() {
        val puzzleText = "35\n" +
                "20\n" +
                "15\n" +
                "25\n" +
                "47\n" +
                "40\n" +
                "62\n" +
                "55\n" +
                "65\n" +
                "95\n" +
                "102\n" +
                "117\n" +
                "150\n" +
                "182\n" +
                "127\n" +
                "219\n" +
                "299\n" +
                "277\n" +
                "309\n" +
                "576"
        val result = puzzle.solveOne(puzzleText, 5)
        assertEquals(127, result)

    }

    @Test
    fun `example part b`() {
        val puzzleText = "35\n" +
                "20\n" +
                "15\n" +
                "25\n" +
                "47\n" +
                "40\n" +
                "62\n" +
                "55\n" +
                "65\n" +
                "95\n" +
                "102\n" +
                "117\n" +
                "150\n" +
                "182\n" +
                "127\n" +
                "219\n" +
                "299\n" +
                "277\n" +
                "309\n" +
                "576"
        val result = puzzle.solveTwo(puzzleText, 5)
        assertEquals(62, result)
    }
}

class Puzzle9 {
    fun solveOne(puzzleText: String, preamble: Int = 25): Long {
        val numbers = puzzleText.split("\n").map { it.toLong() }

        (preamble until numbers.size).forEach { index ->
            val number = numbers[index]

            if (!isValid(numbers, number, index, preamble)) {
                return number
            }
        }

        throw RuntimeException()
    }

    private fun isValid(numbers: List<Long>, number: Long, index: Int, preamble: Int = 25): Boolean {
        val startIndex = index - preamble
        val endIndex = index

        val listy = numbers.subList(startIndex, endIndex).sorted()

        if (listy.size != preamble) {
            throw RuntimeException()
        }

        var i = 0

        while (i < listy.size) {
            var j = i + 1

            while (j < listy.size) {
                if (listy[i] + listy[j] == number) {
                    return true
                }

                j++
            }

            i++
        }

        return false
    }

    fun solveTwo(puzzleText: String, preamble: Int = 25): Long {
        val numbers = puzzleText.split("\n").map { it.toLong() }
        val partOneNumber = solveOne(puzzleText, preamble)

        (0 until numbers.size).forEach { index ->
            val result: Pair<Int, Int>? = canAddUpTo(numbers, partOneNumber, index)

            if (result != null) {
                val aaaa = numbers.subList(result.first, result.second).sorted()
                return aaaa.first() + aaaa.last()

                //return numbers[result.first] + numbers[result.second]
            }
        }

        throw java.lang.RuntimeException()
    }

    private fun canAddUpTo(numbers: List<Long>, partOneNumber: Long, index: Int): Pair<Int, Int>? {
        var i = index
        var total = 0L

        while (i < numbers.size) {
            total += numbers[i]

            if (total == partOneNumber) {
                return index to i
            }
            else if (total > partOneNumber) {
                return null
            }

            i++
        }

        return null
    }
}

