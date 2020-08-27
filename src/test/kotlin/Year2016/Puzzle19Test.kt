package Year2016

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle19Test {
    val puzzle = Puzzle19()
    val puzzleText = this::class.java.getResource("/2016/puzzle19.txt").readText().replace("\r", "")

    @Test
    fun `can solve part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(1815603, result)
    }

    @Test
    fun `can solve part b`() {
        val result= puzzle.solveTwo(puzzleText)
        assertEquals(1410630, result)
    }

    @Test
    fun `part two with aoc example`() {
        val result= puzzle.solveTwo("5")
        assertEquals(2, result)
    }
}

class Puzzle19 {
    data class Node(var next: Node? = null, var previous: Node? = null, val elfNumber: Int) {
        fun remove() {
            val previous = this.previous!!
            val next = this.next!!
            previous.next = next
            next.previous = previous
        }
    }

    fun solveOne(puzzleText: String): Int {
        val allElves = createElfLinkedList(puzzleText)
        var elfCount = allElves.size
        var currentElf = allElves.first()

        while (elfCount > 1) {
            currentElf.next!!.remove()
            currentElf = currentElf.next!!
            elfCount--
        }

        return currentElf.elfNumber
    }

    fun solveTwo(puzzleText: String): Int {
        val allElves = createElfLinkedList(puzzleText)
        var elfCount = allElves.size
        var currentElf = allElves.first()
        var midElf = allElves[allElves.size / 2]

        while (elfCount > 1) {
            midElf.remove()
            midElf = midElf.next!!

            if (elfCount % 2 == 1) {
                midElf = midElf.next!!
            }

            currentElf = currentElf.next!!
            elfCount--
        }

        return currentElf.elfNumber
    }

    private fun createElfLinkedList(puzzleText: String): List<Node> {
        val numberOfElves = puzzleText.toInt()
        val allElves = (1..numberOfElves).map { Node(elfNumber = it) }

        allElves.mapIndexed { index, elf ->
            val nextIndex = if (index == allElves.lastIndex) 0 else index + 1
            val previousIndex = if (index == 0) allElves.lastIndex else index - 1
            elf.next = allElves[nextIndex]
            elf.previous = allElves[previousIndex]
        }
        return allElves
    }
}
