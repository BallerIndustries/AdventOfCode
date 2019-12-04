package Year2019

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class Puzzle4Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle4.txt").readText().replace("\r", "")
    val puzzle = Puzzle4()

    @Test
    fun aaa() {
        assertTrue(puzzle.isPartTwoCompliant(112233))
        assertFalse(puzzle.isPartTwoCompliant(123444))
        assertTrue(puzzle.isPartTwoCompliant(111122))
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(1099, result)
    }

    @Test
    fun `puzzle part b`() {
        // NOT 929
        // NOT 965
        // NOT 1058
        // NOT 1099
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(710, result)
    }
}

class Puzzle4 {
    fun solveOne(puzzleText: String): Int {
        val (from, to) = puzzleText.split("-").map { it.toInt() }
        return (from..to).count(this::isPartOneCompliant)
    }

    private fun isPartOneCompliant(number: Int) =
            twoAdjacentNumbersTheSame(number) && leftToRightDigitsNeverDecrease(number)

    private fun leftToRightDigitsNeverDecrease(number: Int): Boolean {
        val text = number.toString()

        text.forEachIndexed { index, char ->
            if (index != text.lastIndex) {
                val nextChar = text[index+1]

                if (nextChar < char) {
                    return false
                }
            }
        }

        return true
    }

    private fun hasAGroupOfTwo(number: Int): Boolean {
        val text = number.toString()
        val indexes = findIndexesOfTwo(text)

        return indexes.any { index ->
            val indexAheadOfTheTwo = index + 2
            val indexBehindTheTwo = index - 1

            val thePairChar = text.getOrNull(index)!!
            val charBehind = text.getOrNull(indexBehindTheTwo)
            val charAhead = text.getOrNull(indexAheadOfTheTwo)

            thePairChar != charBehind && thePairChar != charAhead
        }
    }

    private fun findIndexesOfTwo(text: String): List<Int> {
        val jur = mutableListOf<Int>()

        text.forEachIndexed { i, _ ->
            if (i != text.lastIndex) {
                if (text[i] == text[i+1]) jur.add(i)
            }
        }

        return jur
    }

    private fun twoAdjacentNumbersTheSame(number: Int): Boolean {
        val text = number.toString()

        text.forEachIndexed { index, char ->
            if (index != text.lastIndex && text[index] == text[index + 1]) {
                return true
            }
        }

        return false
    }

    fun solveTwo(puzzleText: String): Int {
        val (from, to) = puzzleText.split("-").map { it.toInt() }
        return (from..to).count(this::isPartTwoCompliant)
    }

    fun isPartTwoCompliant(number: Int) = hasAGroupOfTwo(number) && leftToRightDigitsNeverDecrease(number)
}

