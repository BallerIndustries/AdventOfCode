package year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle8SlickTest {
    val puzzleText = this::class.java.getResource("/2018/puzzle8.txt").readText()
    val puzzle = Puzzle8()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(46829, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(37450, result)
    }

    class Puzzle8 {
        private var numbers = listOf<Int>()
        private var index = 0
        private fun nextInt() = numbers[index++]

        fun solveOne(puzzleText: String): Int {
            numbers = puzzleText.split(" ").map { it.toInt() }
            return sumMetaData(readTree())
        }

        fun solveTwo(puzzleText: String): Int {
            numbers = puzzleText.split(" ").map { it.toInt() }
            return sumPartTwo(readTree())
        }

        private fun sumPartTwo(node: Node): Int {
            if (node.children.isEmpty()) return node.metadata.sum()

            return node.metadata.map { it - 1 }
                .filter { it < node.children.size }
                .map { indexToChild -> node.children[indexToChild] }
                .sumBy { child -> sumPartTwo(child) }
        }

        data class Node(val children: List<Node>, val metadata: List<Int>)

        private fun readTree(): Node {
            val numChildren = nextInt()
            val numMetadata = nextInt()
            val children = (0 until numChildren).map { readTree() }
            val metadata = (0 until numMetadata).map { nextInt() }

            return Node(children, metadata)
        }

        private fun sumMetaData(node: Node): Int {
            return node.metadata.sum() + node.children.sumBy { sumMetaData(it) }
        }
    }
}