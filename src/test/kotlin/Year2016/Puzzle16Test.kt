package Year2016

import org.junit.Assert.assertEquals
import org.junit.Test

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
    }

    @Test
    fun `checksum for generated data that fills a disk of length 20 with an initial state of 10000`() {
        val actual = puzzle.solveOne("10000", 20)
        val expected = "01100"
        assertEquals(expected, actual)
    }

    @Test
    fun `generated data that fills a disk of length 20 with an initial state of 10000`() {
        val actual = puzzle.generateData("10000", 20)
        val expected = "10000011110010000111110"
        assertEquals(expected, actual)
    }
}

class Puzzle16 {
    fun solveOne(puzzleText: String, length: Int = 272): String {
        var generatedData = puzzleText
        generatedData = generateData(generatedData, length)

        return calculateChecksum(generatedData.substring(0, length))
    }

    fun generateData(generatedData: String, length: Int): String {
        var generatedData1 = generatedData
        val dog = mapOf('0' to '1', '1' to '0')

        while (generatedData1.length < length) {
            generatedData1 = generatedData1 + "0" + generatedData1.reversed().map { dog[it]!! }.joinToString("")
        }
        return generatedData1
    }

    fun calculateChecksum(randomData: String): String {
        var crazyData = randomData
        var buffer = ""

        while (true) {
            for (index in 0 .. crazyData.lastIndex step 2) {
                val l = crazyData[index]
                val r = crazyData[index + 1]

                if (l == r) buffer += "1" else buffer += "0"
            }

            if (buffer.length % 2 == 1) {
                return buffer
            }

            crazyData = buffer
            buffer = ""
        }
    }

    fun solveTwo(puzzleText: String): String {
        return solveOne(puzzleText, 35651584)
    }
}
