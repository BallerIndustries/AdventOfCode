package Year2015

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle17Test {
    val puzzle = Puzzle17()
    val puzzleText = this::class.java.getResource("/2015/puzzle17.txt").readText().replace("\r", "")
    val exampleText = """
        20
        15
        10
        5
        5
    """.trimIndent()

    @Test
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText, 25)
        assertEquals(4, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(654, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(57, result)
    }
}

class Puzzle17 {
    data class Container(val id: Int, val size: Int)

    data class InProgress(val used: Set<Container>, val remaining: Set<Container>) {
        fun totalSize() = used.sumBy { it.size }

        fun addContainer(containerToAdd: Container): InProgress {
            if (!remaining.contains(containerToAdd)) throw RuntimeException("aaaghh noo!!!")

            val newUsed = used + containerToAdd
            val newRemaining = remaining.filter { it != containerToAdd }.toSet()
            return InProgress(newUsed, newRemaining)
        }
    }

    fun solveOne(puzzleText: String, targetSize: Int = 150): Int {
        val containerSizes = puzzleText.split("\n").mapIndexed { index, text -> Container(index, text.toInt()) }
        val dog = getCombinations(containerSizes, targetSize)
        return dog.count()
    }

    fun solveTwo(puzzleText: String): Int {
        val containerSizes = puzzleText.split("\n").mapIndexed { index, text -> Container(index, text.toInt()) }
        val dog = getCombinations(containerSizes, 150)
        val minListLength = dog.minBy { it.count() }!!.count()
        return dog.count { it.count() == minListLength }
    }

    private fun getCombinations(containerSizes: List<Container>, targetSize: Int): List<List<Int>> {
        var containerConfigurations = containerSizes.mapIndexed { index, it ->
            val remainingList = containerSizes.toSet()
            InProgress(setOf(), remainingList)
        }.toSet()

        while (containerConfigurations.any { it.totalSize() < targetSize }) {
            containerConfigurations = containerConfigurations.flatMap { containerConfiguration ->

                val totalSize = containerConfiguration.totalSize()
                if (totalSize == targetSize) {
                    listOf(containerConfiguration)
                } else if (totalSize > targetSize) {
                    listOf()
                } else {
                    containerConfiguration.remaining.map { remainingContainerSize ->
                        containerConfiguration.addContainer(remainingContainerSize)
                    }
                }
            }.toSet()
        }

        val dog = containerConfigurations
            .filter { it.used.sumBy { it.size } == targetSize }
            .map { it.used.map { it.id }.sorted() }
            .distinct()
        return dog
    }
}