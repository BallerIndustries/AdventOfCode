package Year2015

import junit.framework.Assert.*
import org.junit.Test

class Puzzle5Test {
    val puzzleText = this::class.java.getResource(
            "/2015/puzzle5.txt").readText().replace("\r", "")
    val puzzle = Puzzle5()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(238, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(69 , result)
    }

    @Test
    fun `aaa is nice`() {
        assertTrue(puzzle.isNice("aaa"))
    }

    @Test
    fun `ugknbfddgicrmopn is nice`() {
        assertTrue(puzzle.isNice("ugknbfddgicrmopn"))
    }

    @Test
    fun `jchzalrnumimnmhp is not nice`() {
        assertFalse(puzzle.isNice("jchzalrnumimnmhp"))
    }

    @Test
    fun `haegwjzuvuyypxyu is not nice`() {
        assertFalse(puzzle.isNice("haegwjzuvuyypxyu"))
    }

    @Test
    fun `dvszwmarrgswjxmb is not nice`() {
        assertFalse(puzzle.isNice("dvszwmarrgswjxmb"))
    }


}


class Puzzle5 {
    fun solveOne(puzzleText: String): Int {
        return puzzleText.split("\n").count(this::isNice)
    }

    fun isNice(line: String): Boolean {
        val vowels = setOf('a', 'e', 'i', 'o', 'u')
        val horribleMeanStrings= listOf("ab", "cd", "pq", "sy")


        val hasThreeVowels = line.filter { vowels.contains(it) }.count() >= 3
        val hasRepeatedCharacter = hasRepeatedCharacter(line)
        val noHorribleStrings = !line.contains("ab") && !line.contains("cd") &&
                !line.contains("pq") && !line.contains("xy")

        return hasThreeVowels && hasRepeatedCharacter && noHorribleStrings
    }

    fun hasRepeatedCharacter(text: String): Boolean {
        (1 until text.length).forEach { index ->
            val prevIndex = index - 1

            if (text[index] == text[prevIndex]) return true
        }

        return false
    }

    fun solveTwo(puzzleText: String): Int {
        return puzzleText.split("\n").count { line ->
            val hasOverlappingPair = hasOverlappingPair(line)
            val hasRepeatingLetterWithOneLetterGap = hasRepeatingLetterWithOneLetterGap(line)

            hasOverlappingPair && hasRepeatingLetterWithOneLetterGap
        }
    }

    private fun hasRepeatingLetterWithOneLetterGap(line: String): Boolean {
        (2 until line.length).forEach { index ->
            if (line[index] == line[index - 2]) return true
        }

        return false
    }

    private fun hasOverlappingPair(line: String): Boolean {
        (1 until line.length).forEach { index ->

            val prevIndex = index - 1
            val pair = line[prevIndex].toString() + line[index].toString()

            val stringAfterThisShit = line.substring(index + 1)

            if (stringAfterThisShit.contains(pair)) {
                return true
            }
        }

        return false
    }
}