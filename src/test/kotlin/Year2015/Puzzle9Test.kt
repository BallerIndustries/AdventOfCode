package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle9Test {
    val puzzle = Puzzle9()
    val puzzleText = this::class.java.getResource(
            "/2015/puzzle9.txt").readText().replace("\r", "")

    val exampleText = """
        London to Dublin = 464
        London to Belfast = 518
        Dublin to Belfast = 141
    """.trimIndent()

    @Test
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(605, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(117, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(909, result)
    }
}

class Puzzle9 {

    data class NodeData(val townName: String, val distance: Int)

    data class BeingBuilt(val startTown: String, val visitedTowns: List<NodeData>, val unvisited: List<String>) {
        fun lastTownVisited(): String {
            if (visitedTowns.isEmpty()) {
                return startTown
            }
            else {
                return visitedTowns.last().townName
            }
        }

        fun getValidPaths(pathsFromCurrentTown: List<NodeData>): List<NodeData> {
            val visitedTownNames = visitedTowns.map { it.townName }
            return pathsFromCurrentTown.filter { !visitedTownNames.contains(it.townName) && startTown != it.townName }
        }
    }

    fun solveOne(puzzleText: String): Int {
        val graph = parseGraph(puzzleText)
        val allTownNames = graph.keys.distinct().toSet() + graph.values.flatMap { horse: MutableList<NodeData> -> horse.map { it.townName }}.toSet()
        val inProgress = getAllRoutes(allTownNames, graph)
        val distances = inProgress.map { it.visitedTowns.sumBy { it.distance } }

        return distances.min()!!
    }

    private fun getAllRoutes(allTownNames: Set<String>, graph: MutableMap<String, MutableList<NodeData>>): List<BeingBuilt> {
        var inProgress = allTownNames.map { startTown -> BeingBuilt(startTown, listOf(), allTownNames.filter { it != startTown }) }

        while (inProgress.all { it.unvisited.isNotEmpty() }) {

            inProgress = inProgress.flatMap { beingBuilt ->
                val currentTown = beingBuilt.lastTownVisited()
                val pathsFromCurrentTown = graph[currentTown]

                if (pathsFromCurrentTown == null) {
                    listOf()
                } else {
                    val validPathsFromCurrentTown = beingBuilt.getValidPaths(pathsFromCurrentTown)
                    validPathsFromCurrentTown.map { nextNode: NodeData ->
                        beingBuilt.copy(visitedTowns = beingBuilt.visitedTowns + nextNode, unvisited = beingBuilt.unvisited.filter { it != nextNode.townName })
                    }
                }
            }
        }
        return inProgress
    }

    private fun parseGraph(puzzleText: String): MutableMap<String, MutableList<NodeData>> {
        val octopus = mutableMapOf<String, MutableList<NodeData>>()

        puzzleText.split("\n").forEach { line ->
            val tmp = line.split(" ")
            val (from, _, to) = tmp
            val distance = tmp[4].toInt()

            addPath(octopus, from, to, distance)
            addPath(octopus, to, from, distance)
        }

        return octopus
    }

    fun addPath(graph: MutableMap<String, MutableList<NodeData>>, from: String, to: String, distance: Int) {
        if (!graph.containsKey(from)) {
            graph[from] = mutableListOf()
        }

        graph[from] = (graph[from]!! + NodeData(to, distance)).toMutableList()
    }

    fun solveTwo(puzzleText: String): Int {
        val graph = parseGraph(puzzleText)
        val allTownNames = graph.keys.distinct().toSet() + graph.values.flatMap { horse: MutableList<NodeData> -> horse.map { it.townName }}.toSet()
        val inProgress = getAllRoutes(allTownNames, graph)
        val distances = inProgress.map { it.visitedTowns.sumBy { it.distance } }

        return distances.max()!!
    }
}