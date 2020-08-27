package Year2016

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle16Test {
    val puzzle = Puzzle16()
    val puzzleText = this::class.java.getResource("/2016/puzzle16.txt").readText().replace("\r", "")

    @Test
    fun `can solve part a`() {
        val checksum: String = puzzle.solveOne(puzzleText)
        assertEquals("10010110010011110", checksum)
    }

    @Test
    fun `can solve part b`() {
        val checksum: String = puzzle.solveTwo(puzzleText)
        assertEquals("01101011101100011", checksum)
    }

    @Test
    fun `checksum for generated data that fills a disk of length 20 with an initial state of 10000`() {
        val actual = puzzle.solveOne("10000", 20)
        val expected = "01100"
        assertEquals(expected, actual)
    }

    @Test
    fun `next gen generated data that fills a disk of length 20 with an initial state of 10000`() {
        val actual = puzzle.generateDataNextGen("10000", 20)
        val expected = "10000011110010000111"
        assertEquals(expected, actual)
    }
}

class Puzzle16 {
    fun solveOne(puzzleText: String, length: Int = 272): String {
        var generatedData = puzzleText
        generatedData = generateDataNextGen(generatedData, length)

        return calculateChecksum(generatedData)
    }

    fun generateDataNextGen(intitialData: String, length: Int): String {
        val dog = mapOf('0' to '1', '1' to '0')
        val buffer = StringBuilder(length)
        buffer.append(intitialData)

        while (buffer.length < length) {
            val newBuffer = StringBuilder(buffer.count())
            var index = buffer.lastIndex

            while (index >= 0) {
                newBuffer.append(dog[buffer.get(index)])
                index--
            }

            buffer.append('0')
            buffer.append(newBuffer)
        }

        return buffer.toString().substring(0, length)
    }

    fun calculateChecksum(randomData: String): String {
        var crazyData = randomData
        val buffer = StringBuilder(randomData.length)

        while (true) {
            for (index in 0 .. crazyData.lastIndex step 2) {
                val l = crazyData[index]
                val r = crazyData[index + 1]

                if (l == r) buffer.append('1') else buffer.append('0')
            }

            if (buffer.length % 2 == 1) {
                return buffer.toString()
            }

            crazyData = buffer.toString()
            buffer.clear()
        }
    }

    fun solveTwo(puzzleText: String): String {
        return solveOne(puzzleText, 35_651_584)
    }
}
