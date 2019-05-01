package Year2015

import Year2015.Puzzle24.InProgress.Companion.startingPoint
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.lang.RuntimeException

class Puzzle24Test {
    val puzzle = Puzzle24()
    val puzzleText = this::class.java.getResource("/2015/puzzle24.txt").readText().replace("\r", "")
    val exampleText = "".trimIndent()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(255, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(334, result)
    }
}

class Puzzle24 {

    data class InProgress(val used: Set<Int>, val remaining: Set<Int>) {

        companion object {
            fun startingPoint(number: Int, allNumbers: List<Int>): InProgress {
              return InProgress(setOf(number), (allNumbers - number).toSet())
            }
        }

        fun tickAlong(targetSum: Int): List<InProgress> {
            return remaining.mapNotNull { remainingNumber ->
                val newUsed = this.used + remainingNumber
                val newRemaining = this.remaining - remainingNumber
                val sum = newUsed.sum()

                if (sum == targetSum) {
                    null
                }
                else if (sum > targetSum) {
                    null
                }
                else {
                    InProgress(newUsed, newRemaining)
                }
            }
        }
    }


    fun solveOne(puzzleText: String): Int {
        val weights = puzzleText.split("\n").map { it.toInt() }.sortedDescending()
        val compartmentWeight = weights.sum() / 3

        var inProgressStuff = weights.map { startingPoint(it, weights) }

//        while (inProgressStuff.any { it.remaining.isNotEmpty() }) {
//            inProgressStuff = inProgressStuff.flatMap { inProgress -> inProgress.tickAlong(compartmentWeight) }
//        }

        // Lets do a depth first search instead

        return 222
    }

    fun solveTwo(puzzleText: String): Int {
        return 444
    }
}