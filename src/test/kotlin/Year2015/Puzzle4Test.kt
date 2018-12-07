package Year2015

import junit.framework.Assert.assertEquals
import org.apache.commons.codec.digest.DigestUtils
import org.junit.Test

class Puzzle4Test {
    val puzzleText = this::class.java.getResource(
            "/2015/puzzle4.txt").readText().replace("\r", "")
    val puzzle = Puzzle4()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(282749, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(9962624, result)
    }
}


class Puzzle4 {
    val utils = DigestUtils("MD5")

    fun solveOne(puzzleText: String): Int {
        var number = 0

        while (true) {
            val hash = utils.digestAsHex(puzzleText + number)

            if (hash.startsWith("00000")) {
                return number
            }

            number++
        }

        throw RuntimeException("Super weird")
    }

    fun solveTwo(puzzleText: String): Int {
        var number = 0

        while (true) {
            val hash = utils.digestAsHex(puzzleText + number)

            if (hash.startsWith("000000")) {
                return number
            }

            number++
        }

        throw RuntimeException("Super weird")
    }
}