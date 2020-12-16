package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle16Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle16.txt").readText().replace("\r", "")
    val puzzle = Puzzle16()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(32842, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(2628667251989, result)
    }

    @Test
    fun `example part a`() {
        val puzzleText = "class: 1-3 or 5-7\n" +
                "row: 6-11 or 33-44\n" +
                "seat: 13-40 or 45-50\n" +
                "\n" +
                "your ticket:\n" +
                "7,1,14\n" +
                "\n" +
                "nearby tickets:\n" +
                "7,3,47\n" +
                "40,4,50\n" +
                "55,2,20\n" +
                "38,6,12"
        val result = puzzle.solveOne(puzzleText)
        assertEquals(71, result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = "class: 0-1 or 4-19\n" +
                "row: 0-5 or 8-19\n" +
                "seat: 0-13 or 16-19\n" +
                "\n" +
                "your ticket:\n" +
                "11,12,13\n" +
                "\n" +
                "nearby tickets:\n" +
                "3,9,18\n" +
                "15,1,5\n" +
                "5,14,9"

        val expected = mapOf(
            "class" to 12,
            "row" to 11,
            "seat" to 13
        )

        val result = puzzle.resolveTicket(puzzleText)
        assertEquals(expected, result)
    }
}

class Puzzle16 {
    data class Rule(val name: String, val range1: IntRange, val range2: IntRange) {
        fun validValue(value: Int): Boolean {
            return value in range1 || value in range2
        }

        fun invalidValue(value: Int) = !validValue(value)
    }

    fun solveOne(puzzleText: String): Int {
        val (rulesText, _, nearbyTicketsText) = puzzleText.split("\n\n")
        val rules = parseRules(rulesText)
        val split = nearbyTicketsText.split("\n")
        val nearbyTickets = split.subList(1, split.size).map { it.split(",").map { it.toInt() } }

        return nearbyTickets.sumBy { nearbyTicket ->
            nearbyTicket.sumBy { value ->
                if (rules.all { rule -> rule.invalidValue(value) }) {
                    value
                }
                else {
                    0
                }
            }
        }
    }

    private fun parseRules(rulesText: String): List<Rule> {
        return rulesText.split("\n").map { text ->
            val name = text.split(":")[0]
            val (range1, range2) = text.split(": ")[1].split(" or ").map {
                val (start, end) = it.split("-").map { it.toInt() }
                IntRange(start, end)
            }

            Rule(name, range1, range2)
        }
    }

    fun solveTwo(puzzleText: String): Long {
        val ticket = resolveTicket(puzzleText)
        return ticket
            .filter { it.key.startsWith("departure") }
            .values
            .fold(1L) { acc, i -> acc * i }
    }

    fun resolveTicket(puzzleText: String): Map<String, Int> {
        val (rulesText, yourTicketText, nearbyTicketsText) = puzzleText.split("\n\n")
        val rules = parseRules(rulesText)
        val split = nearbyTicketsText.split("\n")
        val nearbyTickets = split.subList(1, split.size).map { it.split(",").map { it.toInt() } }
        val yourTicket = yourTicketText.split("\n")[1].split(",").map { it.toInt() }
        val validNearbyTickets = nearbyTickets.filter { nearbyTicket -> ticketIsValid(nearbyTicket, rules) }
        val ruleToIndex = mutableMapOf<String, Int>()

        while (ruleToIndex.size < rules.size) {
            (0 .. yourTicket.lastIndex).forEach { index ->

                val rulesForIndex = validNearbyTickets.map { validNearbyTicket ->
                    matchingRulesForTicketIndex(index, validNearbyTicket, rules)
                }

                val common: Set<String> = rulesForIndex.reduce { acc, set -> acc.intersect(set) } - ruleToIndex.keys.toSet()

                if (common.size == 1) {
                    ruleToIndex[common.first()] = index
                }
            }
        }

        return rules.associate { rule ->
            val index = ruleToIndex[rule.name]!!
            rule.name to yourTicket[index]
        }
    }

    private fun matchingRulesForTicketIndex(index: Int, ticket: List<Int>, rules: List<Rule>): Set<String> {
        return rules.filter { rule ->
            val value = ticket[index]
            rule.validValue(value)
        }.map { it.name }.toSet()
    }

    private fun ticketIsValid(ticket: List<Int>, rules: List<Rule>): Boolean {
        return ticket.all { value -> rules.any { rule -> rule.validValue(value) } }
    }
}

