package Year2016

import org.apache.commons.codec.digest.DigestUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle5Test {
    val utils = DigestUtils("MD5")
    val puzzle = Puzzle5()

    @Test
    fun `md5 abc3231929 starts with five zeros`() {
        val result = utils.digestAsHex("abc3231929").substring(0, 5)
        assertEquals(result, "00000")
    }

    @Test
    fun `doorcode for doorId = abc`() {
        val doorCode = puzzle.getDoorCode("abc")
        assertEquals(doorCode, "18f47a30")
    }

    @Test
    fun `puzzle part a`() {
        val doorCode = puzzle.getDoorCode("wtnhxymk")
        assertEquals(doorCode, "2414bc77")
    }

    @Test
    fun `positional index doorCode for abc`() {
        val doorCode = puzzle.getPositionalIndexDoorCode("abc")
        assertEquals(doorCode, "05ace8e3")
    }

    @Test
    fun `puzzle part b`() {
        val doorCode = puzzle.getPositionalIndexDoorCode("wtnhxymk")
        assertEquals(doorCode, "437e60fc")
    }
}

