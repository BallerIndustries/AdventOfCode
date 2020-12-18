package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.*

class Puzzle18Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle18.txt").readText().replace("\r", "")
    val puzzle = Puzzle18()

    @Test
    fun `puzzle part a`() {
        //11004703763405 too high
        val result = puzzle.solveOne(puzzleText)
        assertEquals(964875, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(158661360, result)
    }

    @Test
    fun `example part a1`() {
        assertEquals(71, puzzle.evaluate("1 + 2 * 3 + 4 * 5 + 6"))
        assertEquals(51, puzzle.evaluate("1 + (2 * 3) + (4 * (5 + 6))"))
    }

    @Test
    fun `example part a2`() {
        assertEquals(13632, puzzle.evaluate("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))
    }

    @Test
    fun `example part a3`() {
        assertEquals(126, puzzle.evaluate("(6 + 9 * 8 + 6)"))
    }

    @Test
    fun `example part a4`() {
        assertEquals(54, puzzle.evaluate("(2 + 4 * 9)"))
    }

    @Test
    fun `example part a5`() {
        assertEquals(132, puzzle.evaluate("(6 + 9 * 8 + 6) + 6)"))
    }

    @Test
    fun `example part a6`() {
        assertEquals(12, puzzle.evaluate("2 + 4 * 2"))
    }

    @Test
    fun `example part a7`() {
        val line = "2 * 3 + (4 * 5)\n" +
                "5 + (8 * 3 + 9 + 3 * 4 * 3)\n" +
                "5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))\n" +
                "((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"

        assertEquals(26 + 437 + 12240 + 13632, puzzle.solveOne(line))
    }

    @Test
    fun `example part b1`() {
        assertEquals(51, puzzle.evaluateWithPrecedence("1 + (2 * 3) + (4 * (5 + 6))"))
    }

    @Test
    fun `example part b2`() {
        assertEquals(46, puzzle.evaluateWithPrecedence("2 * 3 + (4 * 5)"))
    }

    @Test
    fun `example part b3`() {
        assertEquals(1445, puzzle.evaluateWithPrecedence("5 + (8 * 3 + 9 + 3 * 4 * 3)"))
    }

    @Test
    fun `example part b4`() {
        assertEquals(576, puzzle.evaluateWithPrecedence("12 * 5 + 2 + 9 * 3"))
    }

    @Disabled
    @Test
    fun `example part b`() {
        val puzzleText = ""
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(241861950, result)
    }
}

class Puzzle18 {
    fun solveOne(puzzleText: String): Long {
        return puzzleText.split("\n").map {
            evaluate(it)
        }.reduce { acc, it -> acc + it}
    }

    fun evaluate(line: String): Long {
        val tokens = parseLine(line)

        if (tokens.size != line.replace(" ", "").length) {
            throw RuntimeException()
        }

        val stack = Stack<String>()

        tokens.forEach { token ->
            when (token) {
                ")" -> stack.push(resolveUntilSentinel(stack).toString())
                else -> stack.push(token)
            }
        }

        return resolveUntilSentinel(stack)
    }

    private fun resolveUntilSentinel(stack: Stack<String>): Long {
        val jur = mutableListOf<String>()

        while (stack.isNotEmpty()) {
            if (stack.peek() == "(") {
                stack.pop()
                break
            }

            jur.add(stack.pop())
        }

        var total = 0L
        var index = 0
        jur.reverse()

        while (index < jur.size) {
            when (jur[index]) {
                "+" -> {
                    total += jur[++index].toLong()
                    index++
                }
                "*" -> {
                    total *= jur[++index].toLong()
                    index++
                }
                else -> total += jur[index++].toLong()
            }
        }

        return total
    }

    private fun resolveUntilSentinelWithPrecedence(stack: Stack<String>): Long {
        val tokens = mutableListOf<String>()

        while (stack.isNotEmpty()) {
            if (stack.peek() == "(") {
                stack.pop()
                break
            }

            tokens.add(stack.pop())
        }

        tokens.reverse()

        // [ 12 * 5 + 2 + 9 * 3 ]
        // [ 12 * 7 + 9 * 3 ]
        // [ 12 * 16 * 3 ]

        // Resolve addition
        val withoutAddition = mutableListOf<String>()
        var index = 0

        while (index < tokens.size) {
            val token = tokens[index]

            if (token == "+") {
                val a = withoutAddition.last().toLong()
                val b = tokens[index+1].toLong()
                val result = (a + b).toString()
                withoutAddition.removeAt(withoutAddition.lastIndex)
                withoutAddition.add(result)
                index += 2
            }
            else {
                withoutAddition.add(token)
                index++
            }
        }

        return withoutAddition.filterNot { it == "*" }.map { it.toLong() }.reduce { acc, elem -> acc * elem}


        // Resolve multiplication


//        while (index < jur.size) {
//            when (jur[index]) {
//                "+" -> {
//                    total += jur[++index].toLong()
//                    index++
//                }
//                "*" -> {
//                    total *= jur[++index].toLong()
//                    index++
//                }
//                else -> total += jur[index++].toLong()
//            }
//        }

    }

    private fun parseLine(line: String): List<String> {
        var index = 0
        val tokens = mutableListOf<String>()
        val scrunchedUpLine = line.replace(" ", "")

        while (index < scrunchedUpLine.length) {
            when (scrunchedUpLine[index]) {
                '+', '*', '(', ')' -> {
                    tokens.add(scrunchedUpLine[index].toString())
                    index += 1
                }
                else -> {
                    val number = getNumber(scrunchedUpLine, index)
                    tokens.add(number)
                    index += number.length
                }
            }
        }

        return tokens
    }

    private fun getNumber(line: String, index: Int): String {
        var i = index
        val buffer = StringBuilder()

        while (i < line.length) {
            if (line[i] !in '0'..'9') {
                break
            }

            buffer.append(line[i])
            i++
        }

        return buffer.toString()
    }

    fun solveTwo(puzzleText: String): Long {
        return puzzleText.split("\n").map {
            evaluateWithPrecedence(it)
        }.reduce { acc, it -> acc + it}
    }

    fun evaluateWithPrecedence(line: String): Long {
        val tokens = parseLine(line)
        val lineWithoutWhiteSpace = line.replace(" ", "")

        println("lineWithoutWhiteSpace = $lineWithoutWhiteSpace")
        println("tokens = $tokens")

        val stack = Stack<String>()

        tokens.forEach { token ->
            when (token) {
                ")" -> stack.push(resolveUntilSentinelWithPrecedence(stack).toString())
                else -> stack.push(token)
            }
        }

        return resolveUntilSentinelWithPrecedence(stack)

    }
}

