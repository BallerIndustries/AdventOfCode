package Year2017

import junit.framework.Assert.*
import org.junit.Test
import java.lang.RuntimeException

class Puzzle7Test {
    val puzzle = Puzzle7()
    val puzzleText = this::class.java.getResource(
            "/2017/puzzle7.txt").readText().replace("\r", "")

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("airlri", result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(1206, result)
    }
}

class Puzzle7 {

    data class NodeData(val weight: Int, val children: List<String>)

    fun solveOne(puzzleText: String): String {
        val graph = buildGraph(puzzleText)
        val allChildren = graph.values.flatMap { it.children }.toSet()
        return graph.keys.find { !allChildren.contains(it) }!!
    }

    private fun buildGraph(puzzleText: String): Map<String, NodeData> {
        return puzzleText.split("\n").associate { line ->

            val nodeName = line.split(" ").first()
            val nodeWeight = line.split(" ")[1].removeSurrounding("(", ")").toInt()

            val childrenNames = line.split(" -> ").let { jur ->
                if (jur.size > 1) jur[1].split(", ") else listOf()
            }

            val nodeData = NodeData(nodeWeight, childrenNames)

            nodeName to nodeData
        }
    }

    fun solveTwo(puzzleText: String): Int {
        val graph = buildGraph(puzzleText)
        val rootNodeName = solveOne(puzzleText)
        val rootNode = graph[rootNodeName]!!
        var nodeNameToWeights = rootNode.children.map { it to sumAHorse(graph, it) }
        var uglyDucklingNodeName = ""
        var conformistDuckNodeName = ""

        while (!nodeNameToWeights.all { it.second == nodeNameToWeights.first().second }) {

            // Find the node that does not equal the others
            uglyDucklingNodeName = nodeNameToWeights.groupBy { it.second }
                .map { (count, pairs) -> count to pairs.map { it.first } }
                .toMap()
                .entries.find { it.value.size == 1 }!!.value.first()

            conformistDuckNodeName = nodeNameToWeights.groupBy { it.second }
                .map { (count, pairs) -> count to pairs.map { it.first } }
                .toMap()
                .entries.find { it.value.size != 1 }!!.value.first()

            val nodeData = graph[uglyDucklingNodeName]!!
            nodeNameToWeights = nodeData.children.map { it to sumAHorse(graph, it) }

        }

        val delta = sumAHorse(graph, uglyDucklingNodeName) - sumAHorse(graph, conformistDuckNodeName)
        return graph[uglyDucklingNodeName]!!.weight - delta
    }

    private fun sumAHorse(graph: Map<String, NodeData>, nodeName: String): Int {
        var sum = 0
        val toVisit = mutableListOf<String>()
        toVisit.add(nodeName)

        while (toVisit.isNotEmpty()) {
            val currentNodeName = toVisit.removeAt(0)
            val nodeData = graph[currentNodeName] ?: throw RuntimeException("Hey!")

            sum += nodeData.weight
            toVisit.addAll(nodeData.children)
        }

        return sum
    }
}