package Year2016

import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test

class Puzzle4Test {
    val puzzle = Puzzle4()

    @Test
    fun `real room a`() {
        val input = "aaaaa-bbb-z-y-x-123[abxyz]"
        assertTrue(puzzle.isRealRoom(input))
    }

    @Test
    fun `real room b`() {
        val input = "a-b-c-d-e-f-g-h-987[abcde]"
        assertTrue(puzzle.isRealRoom(input))
    }

    @Test
    fun `real room c`() {
        val input = "not-a-real-room-404[oarel]"
        assertTrue(puzzle.isRealRoom(input))
    }

    @Test
    fun `not a real room`() {
        val input = "totally-real-room-200[decoy]"
        assertFalse(puzzle.isRealRoom(input))
    }

    @Test
    fun `puzzle part a`() {
        val input = Puzzle1Test::class.java.getResource("/2016/puzzle4.txt").readText().replace("\r","")
        val result = puzzle.sumRealRoomNumbers(input)
        Assert.assertEquals(result, 158835)
    }

    @Test
    fun `decrypt aaa`() {
        assertEquals(puzzle.decrypt("aaa-2[a]").substring(0 ,3), "ccc")
    }

    @Test
    fun `decrypt zzz`() {
        assertEquals(puzzle.decrypt("zzz-2[a]").substring(0 ,3), "bbb")
    }

    @Test
    fun `puzzle part b`() {
        val input = Puzzle1Test::class.java.getResource("/2016/puzzle4.txt").readText()
        val result = puzzle.decryptRealRoomNames(input)
        println(result)
    }
}

