package year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Puzzle8Test {
    val puzzleText = this::class.java.getResource(
            "/2018/puzzle8.txt").readText()
    val puzzle = Puzzle8()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(46829, puzzle.sumMetaData(result))
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveOne(puzzleText)
//        puzzle.pruneKids(result)

        // Find all nodes where the number of children does not match teh child count
//        val childCountMismatch = result.filter { it.childCount != it.children.count() }
//        val childListLarger = result.filter { it.children.count() > it.childCount }
//        val metaDataCountMismatch = result.filter { it.metaDataCount != it.metaData.count() }
//
//        assertEquals(0, metaDataCountMismatch.count())
//        assertEquals(0, childCountMismatch.count())
//        assertEquals(0, childListLarger.count())
//

        val sum = puzzle.sumNodesPartTwo(result[0])
        assertEquals(37450, sum)


        //println("Fucking cunttsss!!!")


    }

    @Test
    fun `there should be no childCount mismatches`() {
        val oneChild = "1 1"
        val result = puzzle.solveOne("$oneChild $oneChild 0 1 100 200 300")
        val childCountMismatch = result.filter { it.childCount != it.children.size }

        assertEquals(0, childCountMismatch.count())
    }

    @Test
    @Disabled
    fun `example`() {
        val nodes = puzzle.solveOne("2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2")
        val assertableNodes = nodes.map { it.toAssertable() }

        val expected = listOf(
            AssertableNode("A", 2, 3, mutableListOf(1, 1, 2), mutableListOf("B", "C", "D")),
            AssertableNode("B", 0, 3, mutableListOf(10, 11, 12), mutableListOf()),
            AssertableNode("C", 1, 1, mutableListOf(2), mutableListOf("D")),
            AssertableNode("D", 0, 1, mutableListOf(99), mutableListOf())
        )

        assertEquals(expected, assertableNodes)
        assertEquals(138, puzzle.sumMetaData(nodes))
    }

    @Test
    fun `example part two`() {
        val nodes = puzzle.solveOne("2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2")
        val sum = puzzle.sumNodesPartTwo(nodes[0])
        assertEquals(66, sum)

    }

    @Test
    @Disabled
    fun `one node no children two meta datas`() {
        val nodes = puzzle.solveOne("0 2 100 101")
        assertEquals(listOf(Node("A", 0, 2)), nodes)
    }

    @Test
    @Disabled
    fun `two nodes no children one meta data each`() {
        val nodes = puzzle.solveOne("0 1 100 0 1 101")
        val expected = listOf(
                Node("A", 0, 1),
                Node("B", 0, 1))

        assertEquals(expected, nodes)
    }

    @Test
    @Disabled
    fun `two nodes one is a child of the other`() {
        val nodes = puzzle.solveOne("1 1 0 1 100 101")
        val expected = listOf(
                Node("A", 1, 1),
                Node("B", 0, 1))

        assertEquals(expected, nodes)
    }

    @Test
    @Disabled
    fun `three nodes one is a child of the other`() {
        val nodes = puzzle.solveOne("1 1 1 1 0 1 100 101 100")
        val expected = listOf(
                Node("A", 1, 1),
                Node("B", 1, 1),
                Node("C", 0, 1)
        )

        assertEquals(expected, nodes)
    }

    @Test
    @Disabled
    fun `createChildren can find two leafy children`() {
        val children = puzzle.createChildren('A', 2, listOf(0, 1, 100, 0, 1, 100)).first
        val expected = listOf(
                Node("B", 0, 1),
                Node("C", 0, 1)
        )

        assertEquals(expected, children)
    }

    @Test
    @Disabled
    fun `createChildren can find one leafy child`() {
        val children = puzzle.createChildren('A', 1, listOf(0, 1, 100, 0, 1, 100)).first
        val expected = listOf(
                Node("B", 0, 1)
        )

        assertEquals(expected, children)
    }

    @Test
    @Disabled
    fun `createChildren can find leafy and non leafy children`() {
        val children = puzzle.createChildren('A', 1, listOf(1, 1, 0, 1, 100, 100)).first
        val expected = listOf(
                Node("B", 1, 1),
                Node("C", 0, 1)
        )

        assertEquals(expected, children)
    }

    @Test
    @Disabled
    fun `createChildren can find three nodes in a line`() {
        val children = puzzle.createChildren('A', 1, listOf(1, 1, 1, 1, 0, 1, 99, 100, 101)).first
        val expected = listOf(
                Node("B", 1, 1),
                Node("C", 1, 1),
                Node("D", 0, 1)
        )

        assertEquals(expected, children)
    }

    @Test
    @Disabled
    fun `example using createChildren`() {
        val input = listOf(2, 3, 0, 3, 10, 11, 12, 1, 1, 0, 1, 99, 2, 1, 1, 2)
        val actual = puzzle.createChildren('@', 1, input).first

        val expected = listOf(
                Node("A", 2, 3),
                Node("B", 0, 3),
                Node("C", 1, 1),
                Node("D", 0, 1)
        )

        assertEquals(actual, expected)
    }

    @Test
    fun `index after children for the example should be`() {
        val input = listOf(0, 3, 10, 11, 12, 1, 1, 0, 1, 99, 2, 1, 1, 2)
        val actual = puzzle.createChildren('@', 2, input).second
        assertEquals(11, actual)
    }

    data class AssertableNode(
        val name: String, val childCount: Int, val metaDataCount: Int,
        val metaData: MutableList<Int> = mutableListOf(),
        val childNames: MutableList<String> = mutableListOf()
    )

    data class Node(val name: String, val childCount: Int, val metaDataCount: Int,
        val metaData: MutableList<Int> = mutableListOf(),
        var children: MutableList<Node> = mutableListOf()
    ) {

        fun toAssertable(): AssertableNode {
            return AssertableNode(name, childCount, metaDataCount, metaData, children.map { it.name }.toMutableList())
        }
    }


    class Puzzle8 {
        fun solveOne(puzzleText: String): List<Node> {
            var nodeName = 'A'
            val numbers = puzzleText.split(" ").map { it.toInt() }
            val nodes = mutableListOf<Node>()

            var index = 0

            while (index < numbers.count()) {
                val childCount = numbers[index]
                val metaDataCount = numbers[index + 1]
                val node = Node(nodeName.toString(), childCount, metaDataCount)
                nodes.add(node)

                // Point ahead of childCount and metaDataCount
                index += 2

                // Will have to come back to this jerk to sort out its meta data entries.
                if (node.childCount > 0) {
                    val (children, indexAfterChildren) = createChildren(nodeName, node.childCount, numbers.subList(index, numbers.lastIndex))
                    node.children.addAll(children)
                    nodes.addAll(children)
                    index += indexAfterChildren
                }

                // Add in the meta data
                (index until index + metaDataCount).forEach { node.metaData.add(numbers[it]) }

                index += metaDataCount
                nodeName = nodes.maxBy { it.name }!!.name[0] + 1
            }

            return nodes
        }

        fun createChildren(parentName: Char, parentsChildCount: Int, numbers: List<Int>): Pair<List<Node>, Int> {
            var index = 0
            var nodeName = parentName + 1
            val nodes = mutableListOf<Node>()
            var processedAtThisLevel = 0
            val nodesAtThisLevel = mutableListOf<Node>()

            while (processedAtThisLevel < parentsChildCount) {
                val childCount = numbers[index]
                val metaDataCount = numbers[index + 1]

                val node = Node(nodeName.toString(), childCount, metaDataCount)
                nodes.add(node)

                nodesAtThisLevel.add(node)

                processedAtThisLevel++
                index += 2

                if (childCount == 0) {
                    (index until index + metaDataCount).forEach { node.metaData.add(numbers[it]) }
                    index += metaDataCount
                }
                else {
                    // Need to move the index forward after processing the children
                    val (children, indexDelta) = createChildren(nodeName, childCount, numbers.subList(index, numbers.lastIndex))
                    nodes.addAll(children)
                    node.children.addAll(children)

                    index += indexDelta

                    (index until index + metaDataCount).forEach { node.metaData.add(numbers[it]) }

                    index += metaDataCount
                }

                nodeName++
            }

            if (nodesAtThisLevel.size != parentsChildCount) throw RuntimeException("FUCKK!!!")

            nodesAtThisLevel.sortBy { it.name }
            return nodesAtThisLevel to index
        }

        fun pruneKids(nodes: List<Node>) {
            nodes.forEach { node -> node.children = node.children.subList(0, node.childCount) }
        }

        fun sumMetaData(nodes: List<Node>): Int {
            val allNodes = mutableListOf<Node>()
            val processList = mutableListOf(nodes.first())

            while (processList.isNotEmpty()) {
                val item = processList.removeAt(0)
                allNodes.add(item)
                processList.addAll(item.children)
            }

            return allNodes.sumBy { it.metaData.sum() }
        }


        fun solveTwo(puzzleText: String): String {
            return ""
        }

        fun sumNodesPartTwo(node: Node): Int {
            if (node.children.isEmpty()) {
                return node.metaData.sum()
            }

            val normalisedIndexes = node.metaData.map { it - 1 }
                .filter { it < node.childCount }

            return normalisedIndexes.sumBy { indexToChild ->

                val child = node.children[indexToChild]
                sumNodesPartTwo(child)
            }
        }
    }
}





