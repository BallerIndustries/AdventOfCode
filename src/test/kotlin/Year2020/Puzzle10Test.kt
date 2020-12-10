package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle10Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle10.txt").readText().replace("\r", "")
    val puzzle = Puzzle10()

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
        val puzzleText = "28\n" +
                "33\n" +
                "18\n" +
                "42\n" +
                "31\n" +
                "14\n" +
                "46\n" +
                "20\n" +
                "48\n" +
                "47\n" +
                "24\n" +
                "23\n" +
                "49\n" +
                "45\n" +
                "19\n" +
                "38\n" +
                "39\n" +
                "11\n" +
                "1\n" +
                "32\n" +
                "25\n" +
                "35\n" +
                "8\n" +
                "17\n" +
                "7\n" +
                "9\n" +
                "4\n" +
                "2\n" +
                "34\n" +
                "10\n" +
                "3"
        val result = puzzle.solveOne(puzzleText)
        assertEquals(514579, result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = "16\n" +
                "10\n" +
                "15\n" +
                "5\n" +
                "1\n" +
                "11\n" +
                "7\n" +
                "19\n" +
                "6\n" +
                "12\n" +
                "4"
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(8, result)
    }

    @Test
    fun `example 2 part b`() {
        val puzzleText = "28\n" +
                "33\n" +
                "18\n" +
                "42\n" +
                "31\n" +
                "14\n" +
                "46\n" +
                "20\n" +
                "48\n" +
                "47\n" +
                "24\n" +
                "23\n" +
                "49\n" +
                "45\n" +
                "19\n" +
                "38\n" +
                "39\n" +
                "11\n" +
                "1\n" +
                "32\n" +
                "25\n" +
                "35\n" +
                "8\n" +
                "17\n" +
                "7\n" +
                "9\n" +
                "4\n" +
                "2\n" +
                "34\n" +
                "10\n" +
                "3"
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(19208, result)
    }
}

class Puzzle10 {
    fun solveOne(puzzleText: String): Int {
        val joltages = puzzleText.split("\n").map { it.toInt() }.sorted()

        var current = 0
        val deltas = mutableListOf<Int>()

        joltages.forEachIndexed { index, joltage ->
            deltas.add(joltage - current)
            current = joltage
        }

        return deltas.count { it == 1 } * (deltas.count { it == 3 } + 1)
    }

    fun solveTwo(puzzleText: String): Int {
        val joltages = puzzleText.split("\n").map { it.toInt() }.sorted()
        val graph = mutableMapOf<Int, Int>()
        val jurs = mutableListOf<Int>()

        joltages.forEach { joltage ->
            val count = joltages.count { it in (joltage + 1 .. joltage + 3) }
            graph[joltage] = count
            jurs.add(count)
        }

        // Find the subgraphs, and figure out the permutations in the subgraphs


        var index = 1
        var product = jurs[0]
        val horses = mutableListOf<Int>()

        while (index < jurs.size) {

            val curr = jurs[index]
            val prev = jurs[index - 1]

            if (prev == 1 && curr > 1) {
                product = curr
            }
            else if (prev > 1 && curr > 1) {
                product = product * curr
            }
            else if (curr == 1 && prev > 1) {
                horses.add(product)
                product = 1
            }



            index++
        }

        return horses.sum()
        //return product

    }
}

