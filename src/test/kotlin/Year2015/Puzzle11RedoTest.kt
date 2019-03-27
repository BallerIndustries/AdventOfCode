package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test
import kotlin.random.Random

class Puzzle11RedoTest {
    val puzzle = PuzzleRedo11()
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
    fun `password after a should be b`() {
        val nextPassword = puzzle.nextPassword("a")
        assertEquals("b", nextPassword)
    }

    @Test
    fun `password after z should be ba`() {
        val nextPassword = puzzle.nextPassword("z")
        assertEquals("ba", nextPassword)
    }


    @Test
    fun `password after ba should be bb`() {
        val nextPassword = puzzle.nextPassword("ba")
        assertEquals("bb", nextPassword)
    }

    @Test
    fun `password a should equal integer 1`() {
        assertEquals(0, puzzle.passwordToInt("a"))
    }

    @Test
    fun `integer 1 should equal password a`() {
        assertEquals("a", puzzle.intToPassword(0))
    }

    @Test
    fun `integer 2 should equal password b`() {
        assertEquals("b", puzzle.intToPassword(1))
    }

    @Test
    fun `integer 25 should equal password z`() {
        assertEquals("z", puzzle.intToPassword(25))
    }

    @Test
    fun `integer 26 should equal password ba`() {
        assertEquals("ba", puzzle.intToPassword(26))
    }

    @Test
    fun `ba should equal integer 26`() {
        assertEquals(26, puzzle.passwordToInt("ba"))
    }

    @Test
    fun `can round trip 100 random integers`() {
        val random = Random(0)

        (0 until 100000).forEach {
            val randomInteger = random.nextInt(0, Int.MAX_VALUE)
            val roundTheWorld = puzzle.passwordToInt(puzzle.intToPassword(randomInteger))
            assertEquals(randomInteger, roundTheWorld)
        }
    }
}

class PuzzleRedo11 {
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

    fun nextPassword(password: String): String {
        return intToPassword(passwordToInt(password) + 1)
    }

    val dog: Map<Char, Char> = mapOf('0' to 'a', '1' to 'b', '2' to 'c', '3' to 'd', '4' to 'e', '5' to 'f', '6' to 'g', '7' to 'h', '8' to 'i', '9' to 'j', 'a' to 'k', 'b' to 'l', 'c' to 'm', 'd' to 'n', 'e' to 'o', 'f' to 'p', 'g' to 'q', 'h' to 'r', 'i' to 's', 'j' to 't', 'k' to 'u', 'l' to 'v', 'm' to 'w', 'n' to 'x', 'o' to 'y', 'p' to 'z')
    val invertedDog: Map<Char, Char> = dog.entries.map { (key, value) -> value to key }.toMap()

    fun passwordToInt(password: String): Int {
        return password.map { invertedDog[it]!! }.joinToString("").toInt(26)
    }

    fun intToPassword(number: Int): String {
        return number.toString(26).map { dog[it]!! }.joinToString("")
    }

    fun solveOne(puzzleText: String): String {
        val initialPasswordAsInteger = puzzleText.toInt(26)

        (initialPasswordAsInteger until Int.MAX_VALUE).forEach { number ->

            val password = intToPassword(number)

            if (hasNoIOOrL(password) && hasTwoNonOverlappingPairs(password) && hasThreeCharStraight(password)) {
                return password
            }
        }

        throw RuntimeException("I can't even!")
    }

    fun solveTwo(puzzleText: String): Int {


        return 1281928
    }
}