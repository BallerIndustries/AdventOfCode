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
        // 176 not right
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

        with(puzzle.combos(rules, rules[42]!!, "", mutableMapOf())) {
            println("combos for 42 - $this")
        }

        with(puzzle.combos(rules, rules[31]!!, "", mutableMapOf())) {
            println("combos for 31 - $this")
        }
    }

    @Test
    fun `combos for puzzleText`() {
        val rulesText = puzzleText.split("\n\n")[0]
        val rules = puzzle.parseRules(rulesText)

        with(puzzle.combos(rules, rules[42]!!, "", mutableMapOf())) {
            println("combos for 42 - $this")
        }

        with(puzzle.combos(rules, rules[31]!!, "", mutableMapOf())) {
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
        val combos = puzzle.combos(rules, rules[5]!!, "", mutableMapOf())
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
        val combos = puzzle.combos(rules, rules[4]!!, "", mutableMapOf())
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
        val combos = puzzle.combos(rules, rules[3]!!, "", mutableMapOf())
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
        val combos = puzzle.combos(rules, rules[2]!!, "", mutableMapOf())
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
        val combos = puzzle.combos(rules, rules[1]!!, "", mutableMapOf())
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
        val combos = puzzle.combos(rules, rules[0]!!, "", mutableMapOf())
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
        val combos = puzzle.combos(rules, rules[0]!!, "", mutableMapOf())
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
        val combos = puzzle.combos(rules, rules[3]!!, "", mutableMapOf())
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
        val combos = puzzle.combos(rules, rules[1]!!, "", mutableMapOf())
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
        val combos = puzzle.combos(rules, rules[0]!!, "", mutableMapOf())
        assertEquals(setOf("aaaabb", "aaabab", "abbabb", "abbbab", "aabaab", "abaaab", "aabbbb", "ababbb"), combos)
    }

    @Test
    fun `examples one by one`() {
        val rules = puzzle.parseRulesTwo(exampleTwo.split("\n\n")[0])
        puzzle.setCombos(rules)

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


        val errors = messages.filter {
            val result = puzzle.matchesRuleZero(rules, it)
            !result
        }

        println(errors)
    }

    @Test
    fun `should match rule zero`() {
        val rules = puzzle.parseRulesTwo(exampleTwo.split("\n\n")[0])
        puzzle.setCombos(rules)

        assertEquals(true, puzzle.matchesRuleZero(rules, "babbbbaabbbbbabbbbbbaabaaabaaa"))
//        assertEquals(true, puzzle.matchesRuleZero(rules, "bbbbbbbaaaabbbbaaabbabaaa"))
//        assertEquals(true, puzzle.matchesRuleZero(rules, "bbbababbbbaaaaaaaabbababaaababaabab"))
//        assertEquals(true, puzzle.matchesRuleZero(rules, "abbbbabbbbaaaababbbbbbaaaababb"))
//        assertEquals(true, puzzle.matchesRuleZero(rules, "aaaaabbaabaaaaababaa"))
//        assertEquals(true, puzzle.matchesRuleZero(rules, "aabbbbbaabbbaaaaaabbbbbababaaaaabbaaabba"))
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

        fun combos(puzzle: Puzzle19, rules: Map<Int, Rule>, prefix: String, memo: MutableMap<Int, Set<String>>): List<String> {
            var currentStuff = listOf("")

            for (token in tokens) {
                val (ruleId: Int?, char: Char?) = token

                if (char != null) {
                    currentStuff = currentStuff.map { it + char.toString() }
                }
                else if (ruleId != null) {
                    currentStuff = currentStuff.flatMap { it ->
                        puzzle.combos(rules, rules[ruleId]!!, "", memo).map { aaa ->
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
            // Rule 8: 42 | 42 8, aka one or more repetitions of rule #42
            if (name == 8 && puzzle.rule42Combos != null) {
                return matchRuleEight(puzzle.rule42Combos!!, text, startIndex)
            }
            // Rule 11: 42 31 | 42 11 31, aka one or more repetitions of 4231
            else if (name == 11 && puzzle.rule31Combos != null && puzzle.rule42Combos != null) {
                println("Encountered Rule #11")
                return matchRuleEleven(puzzle.rule31Combos!!, puzzle.rule42Combos!!, text, startIndex)
            }


            for (subRule in subRules) {
                val charactersMatched = subRule.matches(puzzle, rules, text, startIndex)

                if (charactersMatched != -1) {
                    return charactersMatched
                }
            }

            return -1
        }

        private fun matchRuleEleven(rule31Combos: Set<String>, rule42Combos: Set<String>, initialText: String, startIndex: Int): Int {
            var matchAmount = 0
            var text = initialText.substring(startIndex)
            var match = rule42Combos.find { text.startsWith(it) }
            //println(rule42Combos.filter { text.startsWith(it) })
            var rule42MatchCount = 0

            if (match == null) {
                println("rule42MatchCount = $rule42MatchCount")
                return -1
            }

            while (match != null) {
                rule42MatchCount++
                matchAmount += match.length
                text = text.substring(match.length)
                println("text = $text")
                match = rule42Combos.find { text.startsWith(it) }
                //println(rule42Combos.filter { text.startsWith(it) })
            }

            println("rule42MatchCount = $rule42MatchCount")

            ////////////////////////////////////////////
            // Now you must find matchCount rule31Combos
            ////////////////////////////////////////////
            var rule31MatchCount = 0

            match = rule31Combos.find { text.startsWith(it) }

            if (match == null) {
                println("rule31MatchCount = $rule31MatchCount")
                return -1
            }

            while (match != null) {
                rule31MatchCount++
                matchAmount += match.length
                text = text.substring(match.length)
                println("text = $text")
                match = rule31Combos.find { text.startsWith(it) }
            }

            if (rule31MatchCount != rule42MatchCount) {
                println("matchCounts do not match, returning -1")
                return -1
            }

            val charactersMatched = startIndex + matchAmount
            println("matchCounts match, returning $charactersMatched")

            return charactersMatched
        }

        private fun matchRuleEight(rule42Combos: Set<String>, initialText: String, startIndex: Int): Int {
            var matchAmount = 0
            var text = initialText
            var match = rule42Combos.find { text.startsWith(it) }

            if (match == null) {
                return -1
            }

            while (match != null) {
                matchAmount = match.length
                text = text.substring(matchAmount)
                match = rule42Combos.find { text.startsWith(it) }
            }

            return startIndex + matchAmount
        }

        fun combos(puzzle: Puzzle19, rules: Map<Int, Rule>, prefix: String, memo: MutableMap<Int, Set<String>>): Set<String> {
            if (memo[name] == null) {
                val combos = subRules.flatMap { subRule ->
                    subRule.combos(puzzle, rules, prefix, memo)
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

        return messages.count { message ->
            val result = matchesRuleZero(rules, message)

            if (result) {
                println(message)
            }

            result

        }
    }

    fun matchesRuleZero(rules: Map<Int, Rule>, message: String): Boolean {
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

    var rule42Combos: Set<String>? = null
    var rule31Combos: Set<String>? = null

    fun solveTwo(puzzleText: String): Int {
        val (jurness, messagesText) = puzzleText.split("\n\n")
        val messages = messagesText.split("\n")
        val rules = parseRulesTwo(jurness)

        setCombos(rules)

        return messages.count { message ->
            val result = matchesRuleZero(rules, message)

            if (result) {
                println(message)
            }

            result

        }
    }

    public fun setCombos(rules: Map<Int, Rule>) {
        rule42Combos = combos(rules, rules[42]!!, "", mutableMapOf())
        rule31Combos = combos(rules, rules[31]!!, "", mutableMapOf())
    }

    fun parseRulesTwo(jurness: String): Map<Int, Rule> {
        val rulesText = jurness.replace("8: 42", "8: 42 | 42 8")
                .replace("11: 42 31", "11: 42 31 | 42 11 31")


        val rules = parseRules(rulesText)
        //return Pair(messages, rules)
        return rules
    }

    fun combos(rules: Map<Int, Rule>, rule: Rule, prefix: String, memo: MutableMap<Int, Set<String>>): Set<String> {
        return rule.combos(this, rules, prefix, memo)
    }
}

