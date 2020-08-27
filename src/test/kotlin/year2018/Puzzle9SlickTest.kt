package year2018

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle9SlickTest {
    val puzzleText = this::class.java.getResource("/2018/puzzle9.txt").readText().replace("\r", "")
    val puzzle = Puzzle9()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(375465, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(3037741441, result)
    }

    data class Node(var previous: Node? = null, var next: Node? = null, var data: Int) {
        companion object {
            fun createHead(data: Int): Node {
                val head = Node(data = data)
                head.previous = head
                head.next = head
                return head
            }

            fun insertNodeBetween(before: Node, after: Node, data: Int): Node {
                val inserted = Node(before, after, data)
                before.next = inserted
                after.previous = inserted
                return inserted
            }
        }

        fun getNodeXToTheRight(places: Int): Node {
            var node = this
            (0 until places).forEach { node = node.next!! }
            return node
        }

        fun getNodeXToTheLeft(places: Int): Node {
            var node = this
            (0 until places).forEach { node = node.previous!! }
            return node
        }

        fun delete() {
            this.previous!!.next = this.next
            this.next!!.previous = this.previous
        }
    }

    class Puzzle9 {
        fun solveOne(puzzleText: String): Long {
            val (playerCount, lastMarble) = puzzleText.split(" ").mapNotNull { it.toIntOrNull() }
            return getWinningScore(playerCount, lastMarble)
        }

        fun solveTwo(puzzleText: String): Long {
            val (playerCount, lastMarble) = puzzleText.split(" ").mapNotNull { it.toIntOrNull() }
            return getWinningScore(playerCount, lastMarble * 100)
        }

        private fun getWinningScore(playerCount: Int, lastMarble: Int): Long {
            val elvesToScores = (1 .. playerCount).associate { it to 0L }.toMutableMap()
            val head = Node.createHead(0)

            var currentNode = head

            (1 .. lastMarble).forEach { marbleNumber ->
                if (marbleNumber % 23 == 0) {

                    // Go back seven places
                    val toBeRemoved = currentNode.getNodeXToTheLeft(7)

                    // The one after this is the current node
                    currentNode = toBeRemoved.next!!

                    // Add the score to the elf score map
                    val elfNumber = (marbleNumber % elvesToScores.size) + 1
                    elvesToScores[elfNumber] = elvesToScores[elfNumber]!! + marbleNumber + toBeRemoved.data

                    // Remove the node
                    toBeRemoved.delete()
                }
                else {
                    val twoAhead = currentNode.getNodeXToTheRight(2)
                    val oneAhead = twoAhead.previous!!
                    currentNode = Node.insertNodeBetween(oneAhead, twoAhead, marbleNumber)
                }
            }

            return elvesToScores.values.max()!!
        }
    }
}