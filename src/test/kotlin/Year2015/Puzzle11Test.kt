package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle11Test {
    val puzzle = Puzzle11()
    val puzzleText = this::class.java.getResource("/2015/puzzle11.txt").readText().replace("\r", "")
    val exampleText = "1"

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(252594, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(3579328, result)
    }

    @Test
    fun `a should match 1`() {
        val result = puzzle.passwordToInt("a")
        assertEquals(1, result)
    }

    @Test
    fun `a 1 should match a`() {
        val result = puzzle.intToPassword(1)
        assertEquals("a", result)
    }

    @Test
    fun `b should match 1`() {
        val result = puzzle.passwordToInt("b")
        assertEquals(2, result)
    }

    @Test
    fun `aa should match 27`() {
        val result = puzzle.passwordToInt("aa")
        assertEquals(27, result)
    }

    @Test
    fun `aaa should match 26*26`() {
        val result = puzzle.passwordToInt("aaa")
        assertEquals(26*26 + 26 + 1, result)
    }

    @Test
    fun `aaaa should match 26*26`() {
        val result = puzzle.passwordToInt("aaaa")
        assertEquals(26*26*26 + 26*26 + 26 + 1, result)
    }

    @Test
    fun `27 should match aa`() {
        val result = puzzle.intToPassword(27)
        assertEquals("aa", result)
    }

    @Test
    fun `1 should match a`() {
        val result = puzzle.intToPassword(1)
        assertEquals("a", result)
    }

    @Test
    fun `4 should match d`() {
        val result = puzzle.intToPassword(4)
        assertEquals("d", result)
    }
}

class Puzzle11 {
    fun hasThreeCharStraight(text: String): Boolean {
        (2 until text.length).forEach { index ->
            val c1 = text[index-2]
            val c2 = text[index-1]
            val c3 = text[index]

            if ((c2 - c1 == 1) && (c3 - c2 == 1)) {
                return true
            }
        }

        return false
    }

    fun hasNoIOOrL(text: String) = !text.contains('i') && !text.contains('o') && !text.contains('l')

    fun hasTwoNonOverlappingPairs(text: String): Boolean {
        val pairs = mutableSetOf<String>()

        (1 until text.length).forEach { index ->
            val c1 = text[index-1]
            val c2 = text[index]

            if (c1 == c2) pairs.add("$c1$c2")
            if (pairs.size == 2) return true
        }

        return false
    }


    fun intToPassword(value: Int): String {
        var cutOff = 25
        var currentValue = value
        val jur = ('a'..'z').toList()
        var buffer = ""

        while (currentValue > 0) {
            val index = (currentValue % cutOff) - 1
            val char = jur[index]
            buffer = char + buffer
            currentValue -= index
            cutOff *= 26
        }

        return buffer
    }

    fun passwordToInt(password: String): Int {
        var multiplier = 1
        var buffer = 0

        (0 .. password.lastIndex).reversed().forEach { index ->
            val char = password[index]
            val cheese = (char.toInt() - 96) * multiplier
            buffer += cheese
            multiplier *= 26
        }

        return buffer
    }

    fun solveOne(puzzleText: String): String {
        var currentPassword = puzzleText

        while (true) {
            println(currentPassword)

            if (hasThreeCharStraight(currentPassword) && hasNoIOOrL(currentPassword) && hasTwoNonOverlappingPairs(currentPassword)) {
                return currentPassword
            }

            val passwordAsInt = passwordToInt(currentPassword)
            currentPassword = intToPassword(passwordAsInt + 1)
        }
    }

    fun solveTwo(puzzleText: String): Int {
        return 1281928
    }
}