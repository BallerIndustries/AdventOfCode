package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*

class Puzzle8Test {
    val puzzleText = this::class.java.getResource(
            "/2018/puzzle8.txt").readText()
    val puzzle = Puzzle8()


        @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("a", result)
    }
//
//    @Test
//    fun `puzzle part b`() {
//        val result = puzzle.solveTwo(puzzleText)
//        assertEquals("b", result)
//    }

    @Test
    fun `example`() {
        val result = puzzle.solveOne("2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2")
        val expected = listOf(
                Node("A", 2, 3),
                Node("B", 0, 3),
                Node("C", 1, 1),
                Node("D", 0, 1)
        )

        assertEquals(expected, result)
    }

    @Test
    fun `one node no children two meta datas`() {
        val nodes = puzzle.solveOne("0 2 100 101")
        assertEquals(listOf(Node("A", 0, 2)), nodes)
    }

    @Test
    fun `two nodes no children one meta data each`() {
        val nodes = puzzle.solveOne("0 1 100 0 1 101")
        val expected = listOf(
                Node("A", 0, 1),
                Node("B", 0, 1))

        assertEquals(expected, nodes)
    }

    @Test
    fun `two nodes one is a child of the other`() {
        val nodes = puzzle.solveOne("1 1 0 1 100 101")
        val expected = listOf(
                Node("A", 1, 1),
                Node("B", 0, 1))

        assertEquals(expected, nodes)
    }

    @Test
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
    fun `createChildren can find two leafy children`() {
        val children = puzzle.createChildren('A', 2, listOf(0, 1, 100, 0, 1, 100)).first
        val expected = listOf(
                Node("B", 0, 1),
                Node("C", 0, 1)
        )

        assertEquals(expected, children)
    }

    @Test
    fun `createChildren can find one leafy child`() {
        val children = puzzle.createChildren('A', 1, listOf(0, 1, 100, 0, 1, 100)).first
        val expected = listOf(
                Node("B", 0, 1)
        )

        assertEquals(expected, children)
    }

    @Test
    fun `createChildren can find leafy and non leafy children`() {
        val children = puzzle.createChildren('A', 1, listOf(1, 1, 0, 1, 100, 100)).first
        val expected = listOf(
                Node("B", 1, 1),
                Node("C", 0, 1)
        )

        assertEquals(expected, children)
    }

    @Test
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

    data class Node(val name: String, val childCount: Int, val metaDataCount: Int)

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
                    nodes.addAll(children)
                    index += indexAfterChildren
                }

                index += metaDataCount

                nodeName = nodes.maxBy { it.name }!!.name[0] + 1
//                println(nodeName)
            }

            return nodes
        }

        fun createChildren(parentName: Char, parentsChildCount: Int, numbers: List<Int>): Pair<List<Node>, Int> {
            var index = 0
            var nodeName = parentName + 1
            val nodes = mutableListOf<Node>()
            var processedAtThisLevel = 0

            while (processedAtThisLevel < parentsChildCount) {
                val childCount = numbers[index]
                val metaDataCount = numbers[index + 1]

//                println("name = $nodeName childCount = $childCount metaDataCount = $metaDataCount")
                nodes.add(Node(nodeName.toString(), childCount, metaDataCount))
                processedAtThisLevel++
                index += 2

//                if (nodes.count() > 1) println(nodes.count() )

                if (childCount == 0) {
                    index += metaDataCount
                }
                else {
                    // Need to move the index forward after processing the children
                    val (children, indexDelta) = createChildren(nodeName, childCount, numbers.subList(index, numbers.lastIndex))
                    nodes.addAll(children)
                    index += indexDelta + metaDataCount
                }

                nodeName++
            }

            nodes.sortBy { it.name }
            return nodes to index
        }


        fun solveTwo(puzzleText: String): String {
            return ""
        }
    }
}





