package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle16Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle16.txt").readText().replace("\r", "")
    val puzzle = Puzzle16()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("a", result)
    }

    @Test
    fun `example a`() {
        val text = """80871224585914546619083218645595"""
        val result = puzzle.solveOne(text)
        assertEquals("24176176", result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("a", result)
    }
}

class Puzzle16 {
    fun solveOne(puzzleText: String): String {
        var list = puzzleText.map { it.toString().toInt() }
        val basePattern = listOf(0, 1, 0, -1)

        (0 until 100).forEach {
            list = goThroughAPhase(list, basePattern)
        }

        return list.subList(0, 8).joinToString("")
    }

    private fun goThroughAPhase(list: List<Int>, basePattern: List<Int>): List<Int> {
        val phaseTwo = list.mapIndexed { index, _ ->
            val repeatAmount = index + 1
            val pattern = multiplyAndOffset(basePattern, repeatAmount)
            var sum = 0

            list.forEachIndexed { index, number ->
                val patternMultiplicand = pattern[index % pattern.size]

                //println("$number * $patternMultiplicand")
                sum += number * patternMultiplicand
            }

            sum.toString().last().toString().toInt()
        }
        return phaseTwo
    }

    val memo = mutableMapOf<Int, List<Int>>()

    private fun multiplyAndOffset(basePattern: List<Int>, repeatAmount: Int): List<Int> {
        return memo.getOrPut(repeatAmount) {
            val newList = mutableListOf<Int>()

            basePattern.forEach { number ->
                (0 until repeatAmount).forEach {
                    newList.add(number)
                }
            }

            newList.add(newList[0])
            newList.removeAt(0)
            newList
        }
    }

    fun solveTwo(puzzleText: String): String {
        var list = puzzleText.map { it.toString().toInt() }
        val basePattern = listOf(0, 1, 0, -1)

        (0 until 10_000).forEach {
            list = goThroughAPhase(list, basePattern)
        }

        return list.subList(0, 8).joinToString("")
    }
}

