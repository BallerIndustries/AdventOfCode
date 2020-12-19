package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle19Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle19.txt").readText().replace("\r", "")
    val puzzle = Puzzle19()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(144, result)
    }

    @Test
    fun `puzzle part b`() {
        // 352 too high
        // 267 too high
        // 176 not right
        // 175 not right
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(260, result)
    }

    @Test
    fun `example part a`() {
        val puzzleText = "0: 4 1 5\n" +
                "1: 2 3 | 3 2\n" +
                "2: 4 4 | 5 5\n" +
                "3: 4 5 | 5 4\n" +
                "4: \"a\"\n" +
                "5: \"b\"\n" +
                "\n" +
                "ababbb\n" +
                "bababa\n" +
                "abbbab\n" +
                "aaabbb\n" +
                "aaaabbb"

        val result = puzzle.solveOne(puzzleText)
        assertEquals(2, result)
    }

    val exampleTwo = "42: 9 14 | 10 1\n" +
            "9: 14 27 | 1 26\n" +
            "10: 23 14 | 28 1\n" +
            "1: \"a\"\n" +
            "11: 42 31\n" +
            "5: 1 14 | 15 1\n" +
            "19: 14 1 | 14 14\n" +
            "12: 24 14 | 19 1\n" +
            "16: 15 1 | 14 14\n" +
            "31: 14 17 | 1 13\n" +
            "6: 14 14 | 1 14\n" +
            "2: 1 24 | 14 4\n" +
            "0: 8 11\n" +
            "13: 14 3 | 1 12\n" +
            "15: 1 | 14\n" +
            "17: 14 2 | 1 7\n" +
            "23: 25 1 | 22 14\n" +
            "28: 16 1\n" +
            "4: 1 1\n" +
            "20: 14 14 | 1 15\n" +
            "3: 5 14 | 16 1\n" +
            "27: 1 6 | 14 18\n" +
            "14: \"b\"\n" +
            "21: 14 1 | 1 14\n" +
            "25: 1 1 | 1 14\n" +
            "22: 14 14\n" +
            "8: 42\n" +
            "26: 14 22 | 1 20\n" +
            "18: 15 15\n" +
            "7: 14 5 | 1 21\n" +
            "24: 14 1\n" +
            "\n" +
            "abbbbbabbbaaaababbaabbbbabababbbabbbbbbabaaaa\n" +
            "bbabbbbaabaabba\n" +
            "babbbbaabbbbbabbbbbbaabaaabaaa\n" +
            "aaabbbbbbaaaabaababaabababbabaaabbababababaaa\n" +
            "bbbbbbbaaaabbbbaaabbabaaa\n" +
            "bbbababbbbaaaaaaaabbababaaababaabab\n" +
            "ababaaaaaabaaab\n" +
            "ababaaaaabbbaba\n" +
            "baabbaaaabbaaaababbaababb\n" +
            "abbbbabbbbaaaababbbbbbaaaababb\n" +
            "aaaaabbaabaaaaababaa\n" +
            "aaaabbaaaabbaaa\n" +
            "aaaabbaabbaaaaaaabbbabbbaaabbaabaaa\n" +
            "babaaabbbaaabaababbaabababaaab\n" +
            "aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba"

    @Test
    fun `example part b`() {
        val result = puzzle.solveTwo(exampleTwo)
        assertEquals(12, result)
    }

    @Test
    fun `combos for exampleTwo`() {
        val rulesText = exampleTwo.split("\n\n")[0]
        val rules = puzzle.parseRules(rulesText)

        with(puzzle.combos(rules, rules[42]!!, mutableMapOf())) {
            println("combos for 42 - $this")
        }

        with(puzzle.combos(rules, rules[31]!!, mutableMapOf())) {
            println("combos for 31 - $this")
        }
    }

    @Test
    fun `combos for puzzleText`() {
        val rulesText = puzzleText.split("\n\n")[0]
        val rules = puzzle.parseRules(rulesText)

        with(puzzle.combos(rules, rules[42]!!, mutableMapOf())) {
            println("combos for 42 - $this")
        }

        with(puzzle.combos(rules, rules[31]!!, mutableMapOf())) {
            println("combos for 31 - $this")
        }
    }

    @Test
    fun `text matches rule 5`() {
        val rules = puzzle.parseRules("0: 4 1 5\n" +
                "1: 2 3 | 3 2\n" +
                "2: 4 4 | 5 5\n" +
                "3: 4 5 | 5 4\n" +
                "4: \"a\"\n" +
                "5: \"b\"")
        assertEquals(2, puzzle.textMatchesRule(rules, rules[3]!!, "ab", 0))
    }

    @Test
    fun `text matches rule 6`() {
        val rules = puzzle.parseRules("0: 4 1 5\n" +
                "1: 2 3 | 3 2\n" +
                "2: 4 4 | 5 5\n" +
                "3: 4 5 | 5 4\n" +
                "4: \"a\"\n" +
                "5: \"b\"")
        assertEquals(2, puzzle.textMatchesRule(rules, rules[3]!!, "ba", 0))
    }

    @Test
    fun `text matches rule 1`() {
        val rules = puzzle.parseRules("0: 4 1 5\n" +
                "1: 2 3 | 3 2\n" +
                "2: 4 4 | 5 5\n" +
                "3: 4 5 | 5 4\n" +
                "4: \"a\"\n" +
                "5: \"b\"")
        assertEquals(1, puzzle.textMatchesRule(rules, rules[4]!!, "a", 0))
    }

    @Test
    fun `text matches rule 2`() {
        val rules = puzzle.parseRules("0: 4 1 5\n" +
                "1: 2 3 | 3 2\n" +
                "2: 4 4 | 5 5\n" +
                "3: 4 5 | 5 4\n" +
                "4: \"a\"\n" +
                "5: \"b\"")
        assertEquals(1, puzzle.textMatchesRule(rules, rules[5]!!, "b", 0))
    }

    @Test
    fun `text does not match rule 3`() {
        val rules = puzzle.parseRules("0: 4 1 5\n" +
                "1: 2 3 | 3 2\n" +
                "2: 4 4 | 5 5\n" +
                "3: 4 5 | 5 4\n" +
                "4: \"a\"\n" +
                "5: \"b\"")
        assertEquals(1, puzzle.textMatchesRule(rules, rules[5]!!, "bb", 0))
    }

    @Test
    fun `text does not match rule 4`() {
        val rules = puzzle.parseRules("0: 4 1 5\n" +
                "1: 2 3 | 3 2\n" +
                "2: 4 4 | 5 5\n" +
                "3: 4 5 | 5 4\n" +
                "4: \"a\"\n" +
                "5: \"b\"")
        assertEquals(1, puzzle.textMatchesRule(rules, rules[4]!!, "aa", 0))
    }

    @Test
    fun `text matches rule 7`() {
        val rules = puzzle.parseRules("0: 4 1 5\n" +
                "1: 2 3 | 3 2\n" +
                "2: 4 4 | 5 5\n" +
                "3: 4 5 | 5 4\n" +
                "4: \"a\"\n" +
                "5: \"b\"")
        assertEquals(-1, puzzle.textMatchesRule(rules, rules[5]!!, "c", 0))
    }

    @Test
    fun `text matches rule 8`() {
        val rulesText = "0: 4 1 5\n" +
                "1: 2 3\n" +
                "2: 4 4\n" +
                "3: 4 5\n" +
                "4: \"a\"\n" +
                "5: \"b\""

        val rules = puzzle.parseRules(rulesText)
        assertEquals(4, puzzle.textMatchesRule(rules, rules[1]!!, "aaab", 0))
    }

    @Test
    fun `text matches rule 9`() {
        val rulesText = "0: 4 1 5\n" +
                "1: 2 3\n" +
                "2: 4 4\n" +
                "3: 4 5\n" +
                "4: \"a\"\n" +
                "5: \"b\""

        val rules = puzzle.parseRules(rulesText)
        assertEquals(4, puzzle.textMatchesRule(rules, rules[1]!!, "aaab", 0))
    }

    @Test
    fun `text matches rule 10`() {
        val rulesText = "0: 4 1 5\n" +
                "1: 2 3\n" +
                "2: 4 4\n" +
                "3: 4 5\n" +
                "4: \"a\"\n" +
                "5: \"b\""

        val rules = puzzle.parseRules(rulesText)
        assertEquals(6, puzzle.textMatchesRule(rules, rules[0]!!, "aaaabb", 0))
    }

    @Test
    fun `combos for rules - 1`() {
        val rulesText = "0: 4 1 5\n" +
                "1: 2 3\n" +
                "2: 4 4\n" +
                "3: 4 5\n" +
                "4: \"a\"\n" +
                "5: \"b\""

        val rules = puzzle.parseRules(rulesText)
        val combos = puzzle.combos(rules, rules[5]!!, mutableMapOf())
        assertEquals(setOf("b"), combos)
    }

    @Test
    fun `combos for rules - 2`() {
        val rulesText = "0: 4 1 5\n" +
                "1: 2 3\n" +
                "2: 4 4\n" +
                "3: 4 5\n" +
                "4: \"a\"\n" +
                "5: \"b\""

        val rules = puzzle.parseRules(rulesText)
        val combos = puzzle.combos(rules, rules[4]!!, mutableMapOf())
        assertEquals(setOf("a"), combos)
    }

    @Test
    fun `combos for rules - 3`() {
        val rulesText = "0: 4 1 5\n" +
                "1: 2 3\n" +
                "2: 4 4\n" +
                "3: 4 5\n" +
                "4: \"a\"\n" +
                "5: \"b\""

        val rules = puzzle.parseRules(rulesText)
        val combos = puzzle.combos(rules, rules[3]!!, mutableMapOf())
        assertEquals(setOf("ab"), combos)
    }

    @Test
    fun `combos for rules - 4`() {
        val rulesText = "0: 4 1 5\n" +
                "1: 2 3\n" +
                "2: 4 4\n" +
                "3: 4 5\n" +
                "4: \"a\"\n" +
                "5: \"b\""

        val rules = puzzle.parseRules(rulesText)
        val combos = puzzle.combos(rules, rules[2]!!, mutableMapOf())
        assertEquals(setOf("aa"), combos)
    }

    @Test
    fun `combos for rules - 5`() {
        val rulesText = "0: 4 1 5\n" +
                "1: 2 3\n" +
                "2: 4 4\n" +
                "3: 4 5\n" +
                "4: \"a\"\n" +
                "5: \"b\""

        val rules = puzzle.parseRules(rulesText)
        val combos = puzzle.combos(rules, rules[1]!!, mutableMapOf())
        assertEquals(setOf("aaab"), combos)
    }

    @Test
    fun `combos for rules - 6`() {
        val rulesText = "0: 4 1 5\n" +
                "1: 2 3\n" +
                "2: 4 4\n" +
                "3: 4 5\n" +
                "4: \"a\"\n" +
                "5: \"b\""

        val rules = puzzle.parseRules(rulesText)
        val combos = puzzle.combos(rules, rules[0]!!, mutableMapOf())
        assertEquals(setOf("aaaabb"), combos)
    }

    @Test
    fun `combos for rules - 7`() {
        val rulesText = "0: 4 1 5 | 3\n" +
                "1: 2 3\n" +
                "2: 4 4\n" +
                "3: 4 5\n" +
                "4: \"a\"\n" +
                "5: \"b\""

        val rules = puzzle.parseRules(rulesText)
        val combos = puzzle.combos(rules, rules[0]!!, mutableMapOf())
        assertEquals(setOf("aaaabb", "ab"), combos)
    }

    @Test
    fun `combos for rules - 8`() {
        val rulesText = "0: 4 1 5\n" +
                "1: 2 3 | 3 2\n" +
                "2: 4 4 | 5 5\n" +
                "3: 4 5 | 5 4\n" +
                "4: \"a\"\n" +
                "5: \"b\""

        val rules = puzzle.parseRules(rulesText)
        val combos = puzzle.combos(rules, rules[3]!!, mutableMapOf())
        assertEquals(setOf("ab", "ba"), combos)
    }

    @Test
    fun `combos for rules - 9`() {
        val rulesText = "0: 4 1 5\n" +
                "1: 2 3 | 3 2\n" +
                "2: 4 4 | 5 5\n" +
                "3: 4 5 | 5 4\n" +
                "4: \"a\"\n" +
                "5: \"b\""

        // 1 - ["aaab", "aaba", "bbab", "bbba", "abaa", "baaa", "abbb", "babb"]
        // 2 - ["aa", "bb"]
        // 3 - ["ab", "ba"]
        // 4 - ["a"]
        // 5 - ["b"]
        val rules = puzzle.parseRules(rulesText)
        val combos = puzzle.combos(rules, rules[1]!!, mutableMapOf())
        assertEquals(setOf("aaab", "aaba", "bbab", "bbba", "abaa", "baaa", "abbb", "babb"), combos)
    }

    @Test
    fun `combos for rules - 10`() {
        val rulesText = "0: 4 1 5\n" +
                "1: 2 3 | 3 2\n" +
                "2: 4 4 | 5 5\n" +
                "3: 4 5 | 5 4\n" +
                "4: \"a\"\n" +
                "5: \"b\""

        // 1 - ["aaab", "aaba", "bbab", "bbba", "abaa", "baaa", "abbb", "babb"]
        // 2 - ["aa", "bb"]
        // 3 - ["ab", "ba"]
        // 4 - ["a"]
        // 5 - ["b"]
        val rules = puzzle.parseRules(rulesText)
        val combos = puzzle.combos(rules, rules[0]!!, mutableMapOf())
        assertEquals(setOf("aaaabb", "aaabab", "abbabb", "abbbab", "aabaab", "abaaab", "aabbbb", "ababbb"), combos)
    }

    @Test
    fun `examples one by one`() {
        val rules = puzzle.parseRulesTwo(exampleTwo.split("\n\n")[0])
        val rule42Combos: Set<String> = puzzle.combos(rules, rules[42]!!, mutableMapOf())
        val rule31Combos: Set<String> = puzzle.combos(rules, rules[31]!!, mutableMapOf())

        val messages = ("bbabbbbaabaabba\n" +
                "babbbbaabbbbbabbbbbbaabaaabaaa\n" +
                "aaabbbbbbaaaabaababaabababbabaaabbababababaaa\n" +
                "bbbbbbbaaaabbbbaaabbabaaa\n" +
                "bbbababbbbaaaaaaaabbababaaababaabab\n" +
                "ababaaaaaabaaab\n" +
                "ababaaaaabbbaba\n" +
                "baabbaaaabbaaaababbaababb\n" +
                "abbbbabbbbaaaababbbbbbaaaababb\n" +
                "aaaaabbaabaaaaababaa\n" +
                "aaaabbaabbaaaaaaabbbabbbaaabbaabaaa\n" +
                "aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba").split("\n")


        val errors = messages.filter { !puzzle.matchesPartTwo(rule42Combos, rule31Combos, it) }
        assertEquals(listOf<String>(), errors)
    }

    @Test
    fun `should match rule zero`() {
        val rules = puzzle.parseRulesTwo(exampleTwo.split("\n\n")[0])
        val rule42Combos: Set<String> = puzzle.combos(rules, rules[42]!!, mutableMapOf())
        val rule31Combos: Set<String> = puzzle.combos(rules, rules[31]!!, mutableMapOf())

        assertEquals(true, puzzle.matchesPartTwo(rule42Combos, rule31Combos, "babbbbaabbbbbabbbbbbaabaaabaaa"))
        assertEquals(true, puzzle.matchesPartTwo(rule42Combos, rule31Combos, "bbbbbbbaaaabbbbaaabbabaaa"))
        assertEquals(true, puzzle.matchesPartTwo(rule42Combos, rule31Combos, "bbbababbbbaaaaaaaabbababaaababaabab"))
        assertEquals(true, puzzle.matchesPartTwo(rule42Combos, rule31Combos, "abbbbabbbbaaaababbbbbbaaaababb"))
        assertEquals(true, puzzle.matchesPartTwo(rule42Combos, rule31Combos, "aaaaabbaabaaaaababaa"))
        assertEquals(true, puzzle.matchesPartTwo(rule42Combos, rule31Combos, "aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba"))
    }
}

class Puzzle19 {
    data class Token(val ruleId: Int?, val char: Char?) {
        companion object {
            fun from(text: String): Token {
                return if (text.startsWith("\"")) {
                    Token(null, text[1])
                }
                else {
                    Token(text.toInt(), null)
                }
            }
        }
    }

    data class SubRule(val tokens: List<Token>) {
        fun matches(puzzle: Puzzle19, rules: Map<Int, Rule>, text: String, startTextIndex: Int): Int {
            var textIndex = startTextIndex

            for (token in tokens) {
                val char = text.getOrNull(textIndex) ?: return -1

                val (ruleId, tokenChar) = token

                if (ruleId != null) {
                    val rulesMatched = puzzle.textMatchesRule(rules, rules[ruleId]!!, text, textIndex)

                    if (rulesMatched == -1) {
                        return -1
                    }

                    textIndex = rulesMatched
                }
                else {
                    if (tokenChar != null && tokenChar != char) {
                        return -1
                    }

                    textIndex += 1
                }
            }

            return textIndex
        }

        fun combos(puzzle: Puzzle19, rules: Map<Int, Rule>, memo: MutableMap<Int, Set<String>>): List<String> {
            var currentStuff = listOf("")

            for (token in tokens) {
                val (ruleId: Int?, char: Char?) = token

                if (char != null) {
                    currentStuff = currentStuff.map { it + char.toString() }
                }
                else if (ruleId != null) {
                    currentStuff = currentStuff.flatMap {
                        puzzle.combos(rules, rules[ruleId]!!, memo).map { aaa ->
                            it + aaa
                        }
                    }
                }
                else {
                    throw RuntimeException()
                }
            }

            return currentStuff
        }
    }

    data class Rule(val name: Int, val subRules: List<SubRule>) {
        fun matches(puzzle: Puzzle19, rules: Map<Int, Rule>, text: String, startIndex: Int): Int {
            for (subRule in subRules) {
                val charactersMatched = subRule.matches(puzzle, rules, text, startIndex)

                if (charactersMatched != -1) {
                    return charactersMatched
                }
            }

            return -1
        }

        fun combos(puzzle: Puzzle19, rules: Map<Int, Rule>, memo: MutableMap<Int, Set<String>>): Set<String> {
            if (memo[name] == null) {
                val combos = subRules.flatMap { subRule ->
                    subRule.combos(puzzle, rules, memo)
                }.toSet()

                memo[name] = combos
            }

            return memo[name]!!
        }
    }

    fun solveOne(puzzleText: String): Int {
        val (rulesText, messagesText) = puzzleText.split("\n\n")
        val messages = messagesText.split("\n")
        val rules = parseRules(rulesText)

        return messages.count { message -> matchesRuleZero(rules, message) }
    }

    private fun matchesRuleZero(rules: Map<Int, Rule>, message: String): Boolean {
        return textMatchesRule(rules, rules[0]!!, message, 0) == message.length
    }

    fun textMatchesRule(rules: Map<Int, Rule>, rule: Rule, text: String, index: Int): Int {
        return rule.matches(this, rules, text, index)
    }

    fun parseRules(rulesText: String): Map<Int, Rule> {
        return rulesText.split("\n").associate { line ->
            val (name, subRuleText) = line.split(": ")

            val subRules: List<SubRule> = subRuleText.split(" | ").map { subRuleText ->
                val tokens = subRuleText.split(" ").map { Token.from(it) }
                SubRule(tokens)
            }

            val ruleId = name.toInt()
            ruleId to Rule(ruleId, subRules)
        }
    }

    fun solveTwo(puzzleText: String): Int {
        val (rulesText, messagesText) = puzzleText.split("\n\n")
        val messages = messagesText.split("\n")
        val rules = parseRulesTwo(rulesText)

        val rule42Combos: Set<String> = combos(rules, rules[42]!!, mutableMapOf())
        val rule31Combos: Set<String> = combos(rules, rules[31]!!, mutableMapOf())

        return messages.count { message -> matchesPartTwo(rule42Combos, rule31Combos, message) }
    }

    fun matchesPartTwo(rule42Combos: Set<String>, rules31Combos: Set<String>, message: String): Boolean {
        val (remainingMessage, rule31Matches) = chopOffAndCount(message, rules31Combos)
        val (finalMessage, rule42Matches) = chopOffAndCount(remainingMessage, rule42Combos)
        return finalMessage.isEmpty() && rule31Matches > 0 && rule42Matches > rule31Matches
    }

    private fun chopOffAndCount(initialMessage: String, combos: Set<String>): Pair<String, Int> {
        var count = 0
        var message = initialMessage
        var match: String?

        while (true) {
            match = combos.find { message.endsWith(it) }

            if (match == null) {
                return message to count
            }

            count++
            message = message.substring(0, message.length - match.length)
        }
    }

    fun parseRulesTwo(rulesText: String): Map<Int, Rule> {
        val doctoredRulesText = rulesText.replace("8: 42", "8: 42 | 42 8")
                .replace("11: 42 31", "11: 42 31 | 42 11 31")
        return parseRules(doctoredRulesText)
    }

    fun combos(rules: Map<Int, Rule>, rule: Rule, memo: MutableMap<Int, Set<String>>): Set<String> {
        return rule.combos(this, rules, memo)
    }
}

