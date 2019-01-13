package Year2017

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle10Test {
    val puzzle = Puzzle10()
    val puzzleText = this::class.java.getResource("/2017/puzzle10.txt").readText().replace("\r", "")

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(37230, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(1206, result)
    }
}

class Puzzle10 {
    fun solveOne(puzzleText: String): Int {
        val lengths = puzzleText.split(",").map { it.toInt() }
        val listOfNumbers = (0 .. 255).map { it }.toMutableList()
        var currentPosition = 0
        var skipSize = 0

        lengths.forEach { length ->
            // Reverse the order of the sublist from currentPosition to currentPosition + length
            reverseTheJerks(listOfNumbers, currentPosition, length)

            // Move currentPosition forward by that length plus the skipSize
            currentPosition = (currentPosition + length + skipSize) % listOfNumbers.size

            // Increase the skip size by one
            skipSize++
        }

        return listOfNumbers[0] * listOfNumbers[1]
    }

    private fun swap(list: MutableList<Int>, indexA: Int, indexB: Int) {
        val tmp = list[indexA]
        list[indexA] = list[indexB]
        list[indexB] = tmp
    }

    private fun incrementIndex(listSize: Int, index: Int): Int {
        return if (index == listSize - 1) 0 else index + 1
    }

    private fun decrementIndex(listSize: Int, index: Int): Int {
        return if (index == 0) listSize - 1 else index - 1
    }

    private fun reverseTheJerks(listOfNumbers: MutableList<Int>, currentPosition: Int, length: Int) {
        var startIndex = currentPosition
        val simpleEndIndex = (currentPosition + length) - 1
        var endIndex = if (simpleEndIndex < listOfNumbers.size) {
            simpleEndIndex
        }
        else {
            startIndex + length - listOfNumbers.size - 1
        }

        var ops = 0

        while (ops < length / 2) {
            swap(listOfNumbers, startIndex, endIndex)

            startIndex = incrementIndex(listOfNumbers.size, startIndex)
            endIndex = decrementIndex(listOfNumbers.size, endIndex)
            ops++
        }
    }

    fun solveTwo(puzzleText: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}