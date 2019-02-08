package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle12Test {
    val puzzle = Puzzle12()
    val puzzleText = this::class.java.getResource("/2015/puzzle12.txt").readText().replace("\r", "")
    val exampleText = "1"

    @Test
    fun `puzzle part a`() {
        //214938 is too high
        //203051 is too high

        val result = puzzle.solveOne(puzzleText)
        assertEquals(123, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(3579328, result)
    }
}

class Puzzle12 {


    fun solveOne(puzzleText: String): Int {
        val boolMap = puzzleText.map { false }.toMutableList()

        puzzleText.forEachIndexed { index, char ->
            if (char == '-' || char.isDigit()) {
                boolMap[index] = true
            }
        }



//        val regex = Regex("\\d+")
//        val positiveNumberStrings = regex.findAll(puzzleText).map { it.value/*.substring(1)*/ }.toList()
//

//        val negativeRegex = Regex("-\\d+")
//        val negativeNumberStrings = negativeRegex.findAll(puzzleText).map { it.value }.toList()
//
//        return positiveNumberStrings.sumBy { it.toInt() } /*+ negativeNumberStrings.sumBy { it.toInt() }*/
    }

    fun solveTwo(puzzleText: String): Int {
        return 100
    }
}