package Year2017

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle17Test {
    val puzzle = Puzzle17()
    val puzzleText = this::class.java.getResource("/2017/puzzle17.txt").readText().replace("\r", "")
    val exampleText = "3"

    @Test
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(638, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(1914, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(3129487, result)
    }
}

class Puzzle17 {
    fun solveOne(puzzleText: String): Int {
        val numberOfSteps = puzzleText.toInt()
        var currentPosition = 0
        val buffer = mutableListOf(0)

        (1 .. 2017).forEach { numberToInsert ->
            val newIndex = (currentPosition + numberOfSteps) % buffer.size

            if (newIndex == buffer.lastIndex) {
                buffer.add(numberToInsert)
                currentPosition = buffer.lastIndex
            }
            else {
                buffer.add(newIndex + 1, numberToInsert)
                currentPosition = newIndex + 1
            }
        }

        val index = buffer.indexOf(2017)
        return buffer[index + 1]
    }

    fun solveTwo(puzzleText: String): Int {
        val numberOfSteps = puzzleText.toInt()
        var currentPosition = 0
        val buffer = mutableListOf(0)

        (1 .. 50000000).forEach { numberToInsert ->

            if (numberToInsert % 10000 == 0) println(numberToInsert)






            val newIndex = (currentPosition + numberOfSteps) % buffer.size

            if (newIndex == buffer.lastIndex) {
                buffer.add(numberToInsert)
                currentPosition = buffer.lastIndex
            }
            else {
                buffer.add(newIndex + 1, numberToInsert)
                currentPosition = newIndex + 1
            }
        }

        val index = buffer.indexOf(0)
        return buffer[index + 1]
    }
}