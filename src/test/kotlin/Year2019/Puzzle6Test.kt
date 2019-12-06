package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle6Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle6.txt").readText().replace("\r", "")
    val puzzle = Puzzle6()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(270768, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(451, result)
    }
}

class Puzzle6 {
    data class Node(val name: String, val parentName: String?, val children: List<String>) {
        fun addChild(name: String): Node {
            return this.copy(children = this.children + name)
        }

        fun countStepsTo(graph: MutableMap<String, Node>, s: String): Int {
            var currentNode = this
            var steps = 0

            while (currentNode.name != s) {
                currentNode = graph[currentNode.parentName]!!
                steps++
            }

            return steps
        }

        fun getAllNodesUntil(graph: Map<String, Node>, s: String): List<Node> {
            var currentNode = this
            val list = mutableListOf<Node>()

            while (currentNode.name != s) {
                currentNode = graph[currentNode.parentName]!!
                list.add(currentNode)
            }

            return list
        }
    }

    fun parseGraph(puzzleText: String): MutableMap<String, Node> {
        val nameToNode = mutableMapOf<String, Node>()

        // Pares all child nodes
        puzzleText.split("\n").forEach { line ->
            val (parentName, nodeName) = line.split(")")

            if (!nameToNode.containsKey(nodeName)) {
                nameToNode[nodeName] = Node(nodeName, parentName, listOf())
            }
            else {
                throw RuntimeException("sodifjsdf")
            }
        }

        // Link up parent nodes to children
        puzzleText.split("\n").forEach { line ->

            val (parentName, nodeName) = line.split(")")
            nameToNode[parentName] = (nameToNode[parentName] ?: Node(parentName, null, listOf())).addChild(nodeName)
        }


        return nameToNode
    }


    fun solveOne(puzzleText: String): Int {
        val graph = parseGraph(puzzleText)
        return graph.values.sumBy { node: Node -> node.countStepsTo(graph, "COM") }
    }

    fun solveTwo(puzzleText: String): Int {
        val graph = parseGraph(puzzleText)
        val santaNode = graph.values.find { it.name == "SAN" }!!
        val myNode = graph.values.find { it.name == "YOU" }!!

        val santaToComPath: List<Node> = santaNode.getAllNodesUntil(graph, "COM")
        val myToComPath: List<Node> = myNode.getAllNodesUntil(graph, "COM")

        val lowestCommonParent = santaToComPath.first { santaNode -> myToComPath.contains(santaNode) }

        return santaNode.countStepsTo(graph, lowestCommonParent.name) + myNode.countStepsTo(graph, lowestCommonParent.name) - 2
    }
}

