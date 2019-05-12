package Year2016

import org.junit.Assert.assertEquals
import org.junit.Test

class Puzzle22Test {
    val puzzle = Puzzle22()
    val puzzleText = this::class.java.getResource("/2016/puzzle22.txt").readText().replace("\r", "")

    @Test
    fun `can solve part a`() {
        // 1995 too high
        // 1007 too high
        val result = puzzle.solveOne(puzzleText)
        assertEquals("gbhcefad", result)
    }

    @Test
    fun `can solve part b`() {
        // 108 is too low
        val result= puzzle.solveTwo(puzzleText)
        assertEquals("gahedfcb", result)
    }
}

class Puzzle22 {
    data class Node(val name: String, val size: Int, val used: Int, val available: Int) {
        fun isNotEmpty() = this.used != 0
    }

    data class Point(val x: Int, val y: Int)

    fun solveOne(puzzleText: String): Int {
        val grid = parseGrid(puzzleText)
        val nodes = grid.values.toList()
        var count = 0

        for (index in 0 until nodes.size) {
            for (jindex in 0 until nodes.size) {
                if (index == jindex) continue

                val nodeA = nodes[index]
                val nodeB = nodes[jindex]

                if (nodeA.isNotEmpty() && nodeB.available > nodeA.used) {
                    count++
                }
            }
        }

        return count
    }

    private fun parseGrid(puzzleText: String): Map<Point, Node> {
        val nodes = puzzleText.split("\n")
                .let { lines -> lines.subList(2, lines.size) }
                .map { line ->
                    val tmp = line.split(Regex("\\s"))
                    val cheese = tmp.mapNotNull { it.replace("T", "").toIntOrNull() }
                    Node(tmp[0], cheese[0], cheese[1], cheese[2])
                }

        val grid = nodes.associate { node ->
            val (x, y) = node.name.replace("/dev/grid/node-", "")
                    .replace("x", "")
                    .replace("y", "")
                    .split("-")
                    .map { it.toInt() }

            Point(x, y) to node
        }
        return grid
    }

    fun solveTwo(puzzleText: String): String {
        return "22323"
    }
}
