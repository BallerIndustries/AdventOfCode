package Year2016

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle9Test {

    val puzzle = Puzzle9()

    @Test
    fun `text with no markers decompresses to itself`() {
        val compressed = "ADVENT"
        val decompressed = puzzle.decompress(compressed)
        assertEquals(decompressed, "ADVENT")
    }

    @Test
    fun `text with a 1x5 marker`() {
        val compressed = "A(1x5)BC"
        val decompressed = puzzle.decompress(compressed)
        assertEquals(decompressed, "ABBBBBC")
    }

    @Test
    fun `can decompress a single character marker`() {
        val decompressed = Marker("a", 4).toString()
        assertEquals(decompressed, "aaaa")
    }

    @Test
    fun `can decompress a three character marker`() {
        val decompressed = Marker("abc", 4).toString()
        assertEquals(decompressed, "abcabcabcabc")
    }

    @Test
    fun `can split A(1x5)BC into markers and plain text`() {
        val output = puzzle.split("A(1x5)BC")
        assertEquals(output, listOf("A", Marker("B", 5), "C"))
    }

    @Test
    fun `can split (3x3)XYZ into markers and plain text`() {
        val output = puzzle.split("(3x3)XYZ")
        assertEquals(output, listOf(Marker("XYZ", 3)))
    }

    @Test
    fun `can split A(2x2)BCD(2x2)EFG into markers and plain text`() {
        val output = puzzle.split("A(2x2)BCD(2x2)EFG")
        assertEquals(output, listOf("A", Marker("BC", 2), "D", Marker("EF", 2), "G"))
    }

    @Test
    fun `can split (6x1)(1x3)A into markers and plain text`() {
        val output = puzzle.split("(6x1)(1x3)A")
        assertEquals(output, listOf(Marker("(1x3)A", 1)))
    }

    @Test
    fun `can split X(8x2)(3x3)ABCY into markers and plain text`() {
        val output = puzzle.split("X(8x2)(3x3)ABCY")
        assertEquals(output, listOf("X", Marker("(3x3)ABC", 2), "Y"))
    }

    @Test
    fun `puzzle part a`() {
        val compresed = Puzzle1Test::class.java.getResource("/2016/puzzle9.txt").readText()
        val decompressed = puzzle.decompress(compresed)
        assertEquals(decompressed.length, 138735)
    }

    @Test
    fun `puzzle part b`() {
        val compresed = Puzzle1Test::class.java.getResource("/2016/puzzle9.txt").readText()
        val decompressed = puzzle.decompressVersion2(compresed)
        assertEquals(decompressed, 11125026826)
    }

    @Test
    fun `can get weightings for compressedText`() {
        val compressedText = "X(8x2)(3x3)ABCY"
        val weightings = puzzle.calculateWeightings(compressedText)
        assertEquals(weightings, "111111222226661".map { it.toString().toLong() })

        val asdas = puzzle.jur(compressedText, weightings)
        assertEquals(asdas, 20)
    }
}

