package Year2015

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle24Test {
    val puzzle = Puzzle24()
    val puzzleText = this::class.java.getResource("/2015/puzzle24.txt").readText().replace("\r", "")
    val exampleText = "".trimIndent()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(10723906903, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(74850409, result)
    }

    @Test
    fun `quantum entanglement of 2, 4, 6 should be`() {
        val dog = Puzzle24.InProgress(listOf(2, 4, 6), listOf())
        val result = dog.quantumEntanglement()
        assertEquals(48, result)
    }
}

// TODO: Please do this again with DFS instead of random search
class Puzzle24 {
    data class InProgress(val used: List<Long>, val remaining: List<Long>) {
        companion object {
            fun create(weights: List<Long>): InProgress {
                val theOne = weights.random()
                return InProgress(listOf(theOne), weights.filter { it != theOne })
            }
        }

        fun tickAlong(): InProgress {
            val theOne = remaining.random()
            return InProgress(used + theOne, remaining.filter { it != theOne })
        }

        fun quantumEntanglement(): Long {
            return used.fold(1L) { acc: Long, value: Long ->
                acc * value
            }
        }

        override fun toString(): String {
            return "InProgress(used=$used, remaining=$remaining, quantumEntanglement=${quantumEntanglement()})"
        }


    }

    fun tickAlongUntilTarget(weights: List<Long>, target: Long): InProgress? {
        var inProgress = InProgress.create(weights)

        while (inProgress.used.sum() < target) {
            inProgress = inProgress.tickAlong()
        }

        return if (inProgress.used.sum() == target) inProgress else null
    }

    fun solveOne(puzzleText: String): Long {
        val weights = puzzleText.split("\n").map { it.toLong() }.sortedDescending()
        val targetWeight = weights.sum() / 3
        var smallestLength = Int.MAX_VALUE
        var smallestQE = Long.MAX_VALUE

        for (i in 0 until 1000000) {
            val dork = tickAlongUntilTarget(weights, targetWeight) ?: continue

            if ((dork.used.size < smallestLength) || (dork.used.size == smallestLength && dork.quantumEntanglement() < smallestQE)) {
                smallestLength = dork.used.size
                smallestQE = dork.quantumEntanglement()
                println("smallestLength = $smallestLength dork = $dork")
            }
        }

        return smallestQE
    }

    fun solveTwo(puzzleText: String): Long {
        val weights = puzzleText.split("\n").map { it.toLong() }.sortedDescending()
        val targetWeight = weights.sum() / 4
        var smallestLength = Int.MAX_VALUE
        var smallestQE = Long.MAX_VALUE

        for (i in 0 until 100000) {
            val dork = tickAlongUntilTarget(weights, targetWeight) ?: continue

            if ((dork.used.size < smallestLength) || (dork.used.size == smallestLength && dork.quantumEntanglement() < smallestQE)) {
                smallestLength = dork.used.size
                smallestQE = dork.quantumEntanglement()
                println("smallestLength = $smallestLength dork = $dork")
            }
        }

        return smallestQE
    }
}