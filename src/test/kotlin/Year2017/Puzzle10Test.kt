package Year2017

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

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
        assertEquals("70b856a24d586194331398c7fcfa0aaf", result)
    }

    @Test
    fun `example 1`() {
        val result = puzzle.solveTwo("")
        assertEquals("a2582a3a0e66e6e86e3812dcb672a272", result)
    }

    @Test
    fun `example 2`() {
        val result = puzzle.solveTwo("AoC 2017")
        assertEquals("33efeb34ea91902bb2f59c9920caa6cd", result)
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

    fun solveTwo(puzzleText: String): String {
        val lengths = puzzleText.map { it.toByte() } + listOf<Byte>(17, 31, 73, 47, 23)
        val listOfNumbers = (0 .. 255).map { it }.toMutableList()
        var currentPosition = 0
        var skipSize = 0

        (0 until 64).forEach {
            lengths.forEach { length ->
                // Reverse the order of the sublist from currentPosition to currentPosition + length
                reverseTheJerks(listOfNumbers, currentPosition, length.toInt())

                // Move currentPosition forward by that length plus the skipSize
                currentPosition = (currentPosition + length + skipSize) % listOfNumbers.size

                // Increase the skip size by one
                skipSize++
            }
        }

        // Create dense hash
        val chunkyList = listOfNumbers.chunked(16).map { blockOfSixteen ->
            (blockOfSixteen[0] xor blockOfSixteen[1] xor blockOfSixteen[2] xor blockOfSixteen[3] xor blockOfSixteen[4] xor blockOfSixteen[5] xor blockOfSixteen[6] xor blockOfSixteen[7] xor blockOfSixteen[8] xor blockOfSixteen[9] xor blockOfSixteen[10] xor blockOfSixteen[11] xor blockOfSixteen[12] xor blockOfSixteen[13] xor blockOfSixteen[14] xor blockOfSixteen[15]).toByte()
        }

        return chunkyList.map { String.format("%02X", it) }.joinToString("").toLowerCase()
    }
}