package Year2015

import junit.framework.Assert
import junit.framework.Assert.*
import org.junit.Test

class Puzzle1Test {

    val puzzle = Puzzle1()

    @Test
    fun `horse dog horse`() {
        val input = "(())"
        val floor = puzzle.getFloor(input)
        assertEquals(floor, 0)
    }

    @Test
    fun `sad a a a`() {
        assertEquals(puzzle.getFloor("((("), 3)
    }

    @Test
    fun `puzzle part a`() {
        val text = this::class.java.getResource("/2015/puzzle1.txt").readText()
        assertEquals(puzzle.getFloor(text), 138)
    }

    @Test
    fun `puzzle part b`() {
        val text = this::class.java.getResource("/2015/puzzle1.txt").readText()
        assertEquals(puzzle.getCharacterWhenHitMinusOne(text), 1772)
    }
}

class Puzzle1 {
    fun getFloor(input: String): Int {
        return input.count { it == '(' } - input.count { it == ')'}
    }

    fun getCharacterWhenHitMinusOne(text: String): Int {
        var floor = 0

        text.forEachIndexed { index, c ->
            if (c == '(') floor++
            else if (c == ')') floor--

            if (floor == -1) return index + 1
        }

        throw RuntimeException("Never got to floor -1")
    }

}
