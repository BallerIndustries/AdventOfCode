package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

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
    @Ignore
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(18965440, result)
    }

    @Test
    @Ignore
    fun `puzzle part b`() {
        // 103 too low
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(17862900, result)
    }

    @Test
    fun `example part a next gen`() {
        val result = puzzle.solveOneNextGen(puzzleText, 25)
        assertEquals(4, result)
    }
}

class Puzzle17 {
    data class Container(val id: Int, val size: Int)

    data class InProgress(val used: List<Container>, val remaining: List<Container>) {
        fun totalSize() = used.sumBy { it.size }

        fun addContainer(containerToAdd: Container): InProgress {
            if (!remaining.contains(containerToAdd)) throw RuntimeException("aaaghh noo!!!")

            val indexOfItemToRemove= remaining.indexOf(containerToAdd)
            val newUsed = used + remaining[indexOfItemToRemove]
            val newRemaining = remaining.toMutableList()
            newRemaining.removeAt(indexOfItemToRemove)

            return InProgress(newUsed, newRemaining)
        }
    }

    fun solveOneNextGen(puzzleText: String, targetSize: Int = 150): Int {
        val containers = puzzleText.split("\n")
            .mapIndexed { index, text -> Container(index, text.toInt()) }
            .sortedBy { it.size }

        val memo = mutableMapOf<Int, List<List<Int>>>()

        (1 .. targetSize).forEach { theSize ->
            val combos = getCombinations(containers, theSize)
        }

        return 100
    }

    fun solveOne(puzzleText: String, targetSize: Int = 150): Int {
        val containerSizes = puzzleText.split("\n").mapIndexed { index, text -> Container(index, text.toInt()) }

        val dog = getCombinations(containerSizes, targetSize)

        return dog.count()
    }

    private fun getCombinations(containerSizes: List<Container>, targetSize: Int): List<List<Int>> {
        var containerConfigurations = containerSizes.mapIndexed { index, it ->
            val remainingList = containerSizes.toMutableList()
            remainingList.removeAt(index)
            InProgress(listOf(it), remainingList)
        }

        while (containerConfigurations.any { it.totalSize() < targetSize }) {
            containerConfigurations = containerConfigurations.flatMap { containerConfiguration ->

                val totalSize = containerConfiguration.totalSize()
                if (totalSize == targetSize) {
                    listOf(containerConfiguration)
                } else if (totalSize > targetSize) {
                    listOf()
                } else {
                    containerConfiguration.remaining.mapNotNull { remainingContainerSize ->
                        containerConfiguration.addContainer(remainingContainerSize)
                    }
                }
            }
        }

        val dog = containerConfigurations
            .filter { it.used.sumBy { it.size } == targetSize }
            .map { it.used.map { it.id }.sorted() }
            .distinct()
        return dog
    }

    fun solveTwo(puzzleText: String): Int {
        return 100
    }
}