package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle9Test {
    val puzzle = Puzzle9()
    val puzzleText = this::class.java.getResource(
            "/2015/puzzle9.txt").readText().replace("\r", "")

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(1371, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(2117, result)
    }
}

class Puzzle9 {

    data class NodeData(val townName: String, val distance: Int)

    fun solveOne(puzzleText: String): Int {
        val graph = parseGraph(puzzleText)
        println(graph)
        return -1
    }

    private fun parseGraph(puzzleText: String): MutableMap<String, MutableList<NodeData>> {
        val octopus = mutableMapOf<String, MutableList<NodeData>>()

        puzzleText.split("\n").forEach { line ->
            val tmp = line.split(" ")
            val (from, _, to) = tmp
            val distance = tmp[4].toInt()

            if (!octopus.containsKey(from)) {
                octopus[from] = mutableListOf()
            }

            octopus[from] = (octopus[from]!! + NodeData(to, distance)).toMutableList()
        }
        return octopus
    }

    fun solveTwo(puzzleText: String): Int {
        return -1
    }
}