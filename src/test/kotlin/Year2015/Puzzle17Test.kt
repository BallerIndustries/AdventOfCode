package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle17Test {
    val puzzle = Puzzle17()
    val puzzleText = this::class.java.getResource("/2015/puzzle17.txt").readText().replace("\r", "")
    val exampleText = """
    """.trimIndent()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(18965440, result)
    }

    @Test
    fun `puzzle part b`() {
        // 103 too low
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(17862900, result)
    }
}

class Puzzle17 {


    data class InProgress(val used: List<Int>, val remaining: List<Int>) {

        fun totalSize() = used.sum()

        fun addContainerSize(containerSize: Int): InProgress {
            if (!remaining.contains(containerSize)) throw RuntimeException("aaaghh noo!!!")

            val newUsed = used + containerSize
            val indexOfItemToRemove= remaining.indexOf(containerSize)
            val newRemaining = remaining.toMutableList()
            newRemaining.removeAt(indexOfItemToRemove)

            return InProgress(newUsed, newRemaining)
        }
    }

    fun solveOne(puzzleText: String): Int {
        val containerSizes = puzzleText.split("\n").map { it.toInt() }

        var containerConfigurations = containerSizes.mapIndexed { index, it ->
            val remainingList = containerSizes.toMutableList()
            remainingList.removeAt(index)
            InProgress(listOf(it), remainingList)
        }

        while (containerConfigurations.any { it.remaining.isNotEmpty() }) {
            containerConfigurations = containerConfigurations.flatMap { containerConfiguration ->
                containerConfiguration.remaining.map { remainingContainerSize ->
                    containerConfiguration.addContainerSize(remainingContainerSize)
                }
            }


        }








        return 2222
    }



    fun solveTwo(puzzleText: String): Int {
        return 100
    }
}