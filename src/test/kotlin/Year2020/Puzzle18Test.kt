package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class Puzzle18Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle18.txt").readText().replace("\r", "")
    val puzzle = Puzzle18()

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
        assertEquals(71, puzzle.evaluateV2("1 + 2 * 3 + 4 * 5 + 6"))
//        assertEquals(7, puzzle.evaluateV2("1 + (2 * 3)"))
        assertEquals(51, puzzle.evaluateV2("1 + (2 * 3) + (4 * (5 + 6))"))
    }

    @Test
    fun `example part a2`() {
        //assertEquals(51, puzzle.evaluateV2("1 + (2 * 3) + (4 * (5 + 6))"))
        assertEquals(13632, puzzle.evaluateV2("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))

//        assertEquals(7, puzzle.evaluateV2("1 + (2 * 3)"))
//        assertEquals(51, puzzle.evaluateV2("1 + (2 * 3) + (4 * (5 + 6))"))
    }

    @Test
    fun `example part b`() {
        val puzzleText = ""
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(241861950, result)
    }
}

class Puzzle18 {
    fun solveOne(puzzleText: String): Int {
        return 1;
    }

    fun evaluateV3(line: String): Long {
        val tokens = parseLine(line)

        var total = 0L

        // 11 + 2 * 3 + 4 * 5 + 6

        // 11 2 + = 13
        // 13 3 * = 39
        // 39 4

        return resolve(0, tokens)
    }

    fun evaluateV2(line: String): Long {
        val tokens = parseLine(line)
        val stack = Stack<String>()

        tokens.forEach { token ->
            when (token) {
                ")" -> {
                    do {
                        val b = stack.pop().toLong()
                        val operation = stack.pop()
                        val a = stack.pop().toLong()
                        println("$a $operation $b")

                        val result = when (operation) {
                            "+" -> (a + b).toString()
                            "-" -> (a - b).toString()
                            "*" -> (a * b).toString()
                            else -> throw RuntimeException()
                        }

                        if (stack.peek() == "(") {
                            stack.pop()
                            stack.push(result)
                            break
                        }

                        stack.push(result)

                    } while (stack.isNotEmpty())
                }
                else -> stack.push(token)
            }

            println("token = $token stack = $stack")
        }

        while (stack.size > 1) {
            val b = stack.pop().toLong()
            val operation = stack.pop()
            val a = stack.pop().toLong()
            println("$a $operation $b")

            val result = when (operation) {
                "+" -> (a + b).toString()
                "-" -> (a - b).toString()
                "*" -> (a * b).toString()
                else -> throw RuntimeException()
            }

            stack.push(result)
        }

        return stack.pop().toLong()
    }

    // 1 + (2 * 3)

    private fun resolve(index: Int, tokens: List<String>): Long {
        var index1 = index
        val stack = Stack<String>()

        while (index1 < tokens.size) {
            val token = tokens[index1]

            when (token) {
                "+", "-", "*" -> stack.add(token)
                "(" -> stack.add(token)
                ")" -> stack.pop()
                else -> {
                    val head: String? = if (stack.isEmpty()) null else stack.peek()

                    when (head) {
                        "+" -> {
                            val result = stack.pop().toLong() + token.toLong()
                            stack.add(result.toString())
                        }
                        "-" -> {
                            val result = stack.pop().toLong() - token.toLong()
                            stack.add(result.toString())
                        }
                        "*" -> {
                            val result = stack.pop().toLong() * token.toLong()
                            stack.add(result.toString())
                        }
                        else -> {
                            stack.add(token)
                        }
                    }
                }
            }

            println("token = $token stack = $stack")

            index1++
        }

        return stack.pop().toLong()
    }

    private fun parseLine(line: String): List<String> {
        var index = 0;
        val tokens = mutableListOf<String>()
        val scrunchedUpLine = line.replace(" ", "")

        while (index < scrunchedUpLine.length) {
            when (scrunchedUpLine[index]) {
                '+', '-', '*', '(', ')' -> {
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

    fun evaluate(line: String): Long {
        val tokens = line.split(" ")
        var index = 0
        var result = 0L

        // Example 1
        // 1 + (2 * 3)
        // 1 + 6

        // Example 2
        // 1 + (2 * 3) + (4 * (5 + 6))
        // 1 + (2 * 3) + (4 * 11)
        // 1 + (2 * 3) + 44
        // 1 + 6 + 44

        // 1. Pull out the brackets
        //2 * 3, 5 + 6 and (4 * (5 + 6))



        while (index < tokens.size) {
            val token = tokens[index]

            when (token) {
                "+" -> result += tokens[++index].toLong()
                "-" -> result -= tokens[++index].toLong()
                "*" -> result *= tokens[++index].toLong()
                "/" -> result /= tokens[++index].toLong()
                "(" -> { }
                ")" -> { }
                else -> {
                    result = token.toLong()
                }
            }

            index++
        }

        return result
    }

    fun solveTwo(puzzleText: String): Int {


        return 1
    }
}

