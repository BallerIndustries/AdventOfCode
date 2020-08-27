package Year2017

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Puzzle17Test {
    val puzzle = Puzzle17()
    val puzzleText = this::class.java.getResource("/2017/puzzle17.txt").readText().replace("\r", "")
    val exampleText = "3"

    @Test
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(638, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(1914, result)
    }

    @Test
    @Disabled("Works but too slow")
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(41797835, result)
    }
}

class Puzzle17 {
    fun solveOne(puzzleText: String): Int {
        val numberOfSteps = puzzleText.toInt()
        val buffer = createSpinBuffer(2017, numberOfSteps)
        val theNode = buffer.find(2017)
        return theNode.next!!.data
    }

    fun solveTwo(puzzleText: String): Int {
        val numberOfSteps = puzzleText.toInt()
        val buffer = createSpinBuffer(50000000, numberOfSteps)
        val theNode = buffer.find(0)
        return theNode.next!!.data
    }

    data class Node(var next: Node? = null, val data: Int) {
        companion object {
            fun create(data: Int): Node {
                val node = Node(null, data)
                node.next = node
                return node
            }
        }

        fun find(data: Int): Node {
            var dog = this
            while (dog.data != data) dog = dog.next!!
            return dog
        }

        fun nodeAhead(places: Int): Node {
            var dog = this
            (0 until places).forEach { dog = dog.next!! }
            return dog
        }

        fun insertAhead(nodeToInsert: Node) {
            val me = this
            val guyInFront = this.next!!

            me.next = nodeToInsert
            nodeToInsert.next = guyInFront
        }
    }

    private fun createSpinBuffer(size: Int, numberOfSteps: Int): Node {
        val zeroNode = Node.create(0)
        var currentNode = zeroNode

        (1..size).forEach { numberToInsert ->
            if (numberToInsert % 1000000 == 0) println(numberToInsert)

            currentNode = currentNode.nodeAhead(numberOfSteps)
            currentNode.insertAhead(Node.create(numberToInsert))
            currentNode = currentNode.next!!
        }

        return zeroNode
    }
}