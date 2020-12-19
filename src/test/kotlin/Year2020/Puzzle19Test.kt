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
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(158661360, result)
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

    @Test
    fun `example part b`() {
        val puzzleText = "42: 9 14 | 10 1\n" +
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
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(12, result)
    }

    val rulesText = "0: 4 1 5\n" +
            "1: 2 3 | 3 2\n" +
            "2: 4 4 | 5 5\n" +
            "3: 4 5 | 5 4\n" +
            "4: \"a\"\n" +
            "5: \"b\""


    @Test
    fun `text matches rule 5`() {
        val rules = puzzle.parseRules(rulesText)
        assertEquals(2, puzzle.textMatchesRule(rules, rules[3]!!, "ab", 0))
    }

    @Test
    fun `text matches rule 6`() {
        val rules = puzzle.parseRules(rulesText)
        assertEquals(2, puzzle.textMatchesRule(rules, rules[3]!!, "ba", 0))
    }

    @Test
    fun `text matches rule 1`() {
        val rules = puzzle.parseRules(rulesText)
        assertEquals(1, puzzle.textMatchesRule(rules, rules[4]!!, "a", 0))
    }

    @Test
    fun `text matches rule 2`() {
        val rules = puzzle.parseRules(rulesText)
        assertEquals(1, puzzle.textMatchesRule(rules, rules[5]!!, "b", 0))
    }

    @Test
    fun `text does not match rule 3`() {
        val rules = puzzle.parseRules(rulesText)
        assertEquals(1, puzzle.textMatchesRule(rules, rules[5]!!, "bb", 0))
    }

    @Test
    fun `text does not match rule 4`() {
        val rules = puzzle.parseRules(rulesText)
        assertEquals(1, puzzle.textMatchesRule(rules, rules[4]!!, "aa", 0))
    }

    @Test
    fun `text matches rule 7`() {
        val rules = puzzle.parseRules(rulesText)
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

                //println("textIndex = $textIndex char = $char token = $token")
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

            //println("matched up to textIndex $textIndex")
            return textIndex
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
    }

    fun solveOne(puzzleText: String): Int {
        val (rulesText, messagesText) = puzzleText.split("\n\n")

        val messages = messagesText.split("\n")
        val rules = parseRules(rulesText)

        return messages.count { message ->
            val result = matchesRuleZero(rules, message)

            if (result) {
                println(message)
            }

            result

        }
    }

    private fun matchesRuleZero(rules: Map<Int, Rule>, message: String): Boolean {
        val textMatchesRule = textMatchesRule(rules, rules[0]!!, message, 0)
        println("textMatchesRule = ${textMatchesRule}")
        return textMatchesRule == message.length
    }

    fun textMatchesRule(rules: Map<Int, Rule>, rule: Rule, text: String, index: Int): Int {
        val matches = rule.matches(this, rules, text, index)
        //println("Rule #${rule.name} matched up to index $matches at index $index for text = $text")
        return matches
    }

    fun parseRules(rulesText: String): Map<Int, Rule> {
        val rules = rulesText.split("\n").map { line ->
            val (name, jur) = line.split(": ")

            val subRules: List<SubRule> = jur.split(" | ").map { subRuleText ->
                val tokens = subRuleText.split(" ").map { Token.from(it) }
                SubRule(tokens)
            }

            Rule(name.toInt(), subRules)
        }

        return rules.associateBy { it.name }
    }

    fun solveTwo(puzzleText: String): Int {
        val (jurness, messagesText) = puzzleText.split("\n\n")

        val rulesText = jurness.replace("8: 42", "8: 42 | 42 8")
                .replace("11: 42 31", "11: 42 31 | 42 11 31")

        val messages = messagesText.split("\n")
        val rules = parseRules(rulesText)

        return messages.count { message ->
            val result = matchesRuleZero(rules, message)

            if (result) {
                println(message)
            }

            result

        }
    }
}

