package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test

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
    fun `password a should equal integer 1`() {
        assertEquals(1, puzzle.passwordToInt("a"))
    }

    @Test
    fun `integer 1 should equal password a`() {
        assertEquals("a", puzzle.intToPassword(1))
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
        return "sdfds"
    }

    val crazyMap: Map<Char, Int> = mapOf('a' to 1, 'b' to 2, 'c' to 3, 'd' to 4, 'e' to 5, 'f' to 6, 'g' to 7, 'h' to 8, 'i' to 9, 'j' to 10, 'k' to 11, 'l' to 12, 'm' to 13, 'n' to 14, 'o' to 15, 'p' to 16, 'q' to 17, 'r' to 18, 's' to 19, 't' to 20, 'u' to 21, 'v' to 22, 'w' to 23, 'x' to 24, 'y' to 25, 'z' to 26)
    val invertedCrazyMap: Map<Int, Char> = crazyMap.entries.map { (key, value) -> value to key }.toMap()

    fun passwordToInt(password: String): Int {
        return password.toInt(26)
    }

    fun intToPassword(number: Int): String {
        return number.toString(26)/*.map { char ->  }*/
    }

    fun solveOne(puzzleText: String): String {
//        val dog = 25.toString(26)
//        println("dog = $dog")



        val cra = (0 .. 100).associate { it to it.toString(26) }
        println(cra)


        return "saduihas"
    }

    fun solveTwo(puzzleText: String): Int {


        return 1281928
    }
}