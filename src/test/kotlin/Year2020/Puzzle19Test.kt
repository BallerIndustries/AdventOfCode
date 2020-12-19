package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle19Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle19.txt").readText().replace("\r", "")
    val puzzle = Puzzle19()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(964875, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(158661360, result)
    }

    @Test
    fun `example part a`() {
        val puzzleText = ""
        val result = puzzle.solveOne(puzzleText)
        assertEquals(514579, result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = ""
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(241861950, result)
    }
}

class Puzzle19 {
    data class SubRule(val names: List<String>)

    data class Rule(val name: Int, val subRules: List<SubRule>)

    fun solveOne(puzzleText: String): Int {
        val (rulesText, messagesText) = puzzleText.split("\n\n")

        val messages = messagesText.split("\n")
        val rules = parseRules(rulesText)

        messages.count { message ->
            matchesRuleZero(rules, message)
        }

        

        return 1
    }

    private fun matchesRuleZero(rules: Map<Int, Rule>, message: String): Boolean {
        TODO("Not yet implemented")
    }

    private fun parseRules(rulesText: String): Map<Int, Rule> {
        val rules = rulesText.split("\n").map { line ->
            val (name, jur) = line.split(": ")

            val subRules: List<SubRule> = jur.split(" | ").map { subRuleText ->
                SubRule(subRuleText.split(" "))
            }

            Rule(name.toInt(), subRules)
        }

        return rules.associateBy { it.name }
    }

    fun solveTwo(puzzleText: String): Int {
        return 1
    }
}

