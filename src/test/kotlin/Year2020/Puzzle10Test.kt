package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle10Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle10.txt").readText().replace("\r", "")
    val puzzle = Puzzle10()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(2201, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwoImperative(puzzleText)
        assertEquals(169255295254528, result)
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
        assertEquals(220, result)
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
        val result = puzzle.solveTwoImperative(puzzleText)
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
        val result = puzzle.solveTwoImperative(puzzleText)
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

    fun solveTwo(puzzleText: String): Long {
        val joltages = ((puzzleText.split("\n").map { it.toInt() }) + 0).sorted()

        val graph = joltages.associateWith {
            joltage -> joltages.filter { it in (joltage + 1 .. joltage + 3) }
        }

        return countPaths(graph, 0, joltages.max()!! + 3, mutableMapOf())
    }

    fun solveTwoImperative(puzzleText: String): Long {
        val adaptors = ((puzzleText.split("\n").map { it.toInt() })).sorted()
        val max = adaptors.last()
        val dp = mutableMapOf(0 to 1L)

        adaptors.forEach { i ->
            dp[i] = (dp[i-1] ?: 0) + (dp[i-2] ?: 0) + (dp[i-3] ?: 0)
        }

        return dp[max] ?: throw RuntimeException()
    }

    private fun countPaths(graph: Map<Int, List<Int>>, current: Int, target: Int, memo: MutableMap<Int, Long>): Long {
        if (current == target) {
            return 1
        }

        val children = graph[current]!!

        if (children.isEmpty()) {
            return 1
        }

        if (!memo.containsKey(current)) {
            memo[current] = children.sumByDouble { countPaths(graph, it, target, memo).toDouble() }.toLong()
        }

        return memo[current]!!
    }
}

