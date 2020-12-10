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
        val joltages = ((puzzleText.split("\n").map { it.toInt() }) + 0).sorted()
        val graph = mutableMapOf<Int, Int>()
        val jurs = mutableListOf<Int>()

        joltages.forEach { joltage ->
            val count = joltages.count { it in (joltage + 1 .. joltage + 3) }
            graph[joltage] = count
            jurs.add(count)
        }

        // Find the subgraphs, and figure out the permutations in the subgraphs
        val aList = mutableListOf<Int>()
        val bigList = mutableListOf<List<Int>>()
        var index = 1
        var prev = graph[joltages[0]]!!

        if (prev > 1) {
            aList.add(prev)
        }

        while (index < joltages.size) {
            var current = graph[joltages[index]]!!

            if (current > 1) {
                aList.add(joltages[index])
            }
            else if ((current == 1 || current == 0) && aList.isNotEmpty()) {
                println(aList)
                bigList.add(aList.map { it })
                aList.clear()
            }

            prev = current
            index++
        }

//        joltages.forEachIndexed { index, i ->
//
//
//
//        }

        val dog = bigList.map { list ->
            val number: Int = findPathsBeyondLast(graph, list.first(), list.last())
            number
        }

        var product = 1
        dog.forEach { product *= it }
        return product

//        return dog.reduce { 1, i -> it * i }
//
//        return 22
    }

    private fun findPathsBeyondLast(graph: Map<Int, Int>, current: Int, last: Int): Int {
        val joltages = graph.keys.sorted()

        if (current > last) {
            return 1
        }
        if (current == last) {
            return graph[current]!!
        }

        val aaa: List<Int> =  joltages.filter { it in (current + 1 .. current + 3)  }

        return aaa.sumBy { findPathsBeyondLast(graph, it, last) }



        // 4 -> 5 -> 6
        // 4 -> 5 -> 7
        // 4 -> 6
        // 4 -> 7





//        println()
//        TODO("Not yet implemented")
    }
}

