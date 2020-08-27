package Year2016

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle22Test {
    val puzzle = Puzzle22()
    val puzzleText = this::class.java.getResource("/2016/puzzle22.txt").readText().replace("\r", "")

    @Test
    fun `can solve part a`() {
        // 1995 too high
        // 1007 too high
        val result = puzzle.solveOne(puzzleText)
        assertEquals(981, result)
    }

    @Test
    fun `can solve part b`() {
        // 233 too high
        puzzle.solveTwo(puzzleText)
        //// Get in front of the far right node
        //
        //35 // to top
        //67 // to in front of node
        //
        //// Move to the far left
        //// Grid is 35 across
        //
        //(33 * 5) + 1 + 67
    }
}

class Puzzle22 {
    data class Node(val name: String, val size: Int, val used: Int, val available: Int, val position: Point, val hasTheGoldenData: Boolean) {
        fun isNotEmpty() = this.used != 0
    }

    data class Point(val x: Int, val y: Int)

    fun solveOne(puzzleText: String): Int {
        val grid = parseGrid(puzzleText)
        val nodes = grid.values.toList()
        val viablePairs = getViablePairs(nodes)
        return viablePairs.size
    }

    private fun getViablePairs(nodes: List<Node>): MutableList<Pair<Node, Node>> {
        val viablePairs = mutableListOf<Pair<Node, Node>>()

        for (index in 0 until nodes.size) {
            for (jindex in 0 until nodes.size) {
                if (index == jindex) continue

                val nodeA = nodes[index]
                val nodeB = nodes[jindex]

                if (nodeA.isNotEmpty() && nodeB.available > nodeA.used) {
                    viablePairs.add(nodeA to nodeB)
                }
            }
        }

        return viablePairs
    }

    private fun parseGrid(puzzleText: String): Map<Point, Node> {
        val nodes = puzzleText.split("\n")
                .let { lines -> lines.subList(2, lines.size) }
                .map { line ->
                    val tmp = line.split(Regex("\\s"))
                    val nodeName = tmp[0]
                    val theNumbers = tmp.mapNotNull { it.replace("T", "").toIntOrNull() }
                    val (x, y) = nodeName.replace("/dev/grid/node-", "")
                            .replace("x", "")
                            .replace("y", "")
                            .split("-")
                            .map { it.toInt() }

                    Node(nodeName, theNumbers[0], theNumbers[1], theNumbers[2], Point(x, y), false)
                }

        val jur = nodes.associate { node -> node.position to node }.toMutableMap()
        val maxX = jur.keys.maxBy { it.x }!!.x
        val goldenIndex = Point(maxX, 0)
        jur[goldenIndex] = jur[goldenIndex]!!.copy(hasTheGoldenData = true)

        return jur
    }

    fun paddyPaddyPadPad(text: String): String {
        return when {
            text.length == 1 -> "  $text"
            text.length == 2 -> " $text"
            else -> text
        }
    }

    fun solveTwo(puzzleText: String): String {
        val grid = parseGrid(puzzleText)

        val maxX = grid.keys.maxBy { it.x }!!.x
        val maxY = grid.keys.maxBy { it.y }!!.y

        val dog = (0 .. maxY).map { y ->
            (0 .. maxX).map { x ->

                val node = grid[Point(x, y)]!!
                val partA = paddyPaddyPadPad(node.used.toString())
                val partB = paddyPaddyPadPad((node.used + node.available).toString())
                val cheese = "[$partA / $partB]"
                cheese
            }.joinToString(" ")
        }.joinToString("\n")

        println(dog)

        return "22323"
    }
}
