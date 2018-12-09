package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import java.util.*

class Puzzle9Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle9.txt").readText().replace("\r", "")
    val puzzle = Puzzle9()

    @Test
    fun `processOrders when commands and list are empty`() {
        val result = puzzle.processOrders(listOf(), listOf())
        assertEquals(emptyList<Int>(), result)
    }

    @Test
    fun `processOrders list empty not empty and commands empty`() {
        val result = puzzle.processOrders(listOf(1, 2, 3), listOf())
        assertEquals(listOf(1, 2, 3), result)
    }

    @Test
    fun `processOrders list empty not empty and a command`() {
        val result = puzzle.processOrders(listOf(1, 2, 3), listOf(Puzzle9.AddCommand(0, 100)))
        assertEquals(listOf(100, 1, 2, 3), result)
    }

    @Test
    fun `processOrders list empty not empty and two add commands`() {
        val result = puzzle.processOrders(listOf(1, 2, 3), listOf(Puzzle9.AddCommand(0, 100), Puzzle9.AddCommand(0, 99)))
        assertEquals(listOf(99, 100, 1, 2, 3), result)
    }

    @Test
    fun `processOrders list empty not empty and three add commands`() {
        val orders = listOf(
                Puzzle9.AddCommand(0, 100),
                Puzzle9.AddCommand(0, 99),
                Puzzle9.AddCommand(0, 98)
        )

        val result = puzzle.processOrders(listOf(1, 2, 3), orders)
        assertEquals(listOf(98, 99, 100, 1, 2, 3), result)
    }

    @Test
    fun `processOrders list empty not empty and add commands not at the beginning`() {
        val orders = listOf(
                Puzzle9.AddCommand(1, 100),
                Puzzle9.AddCommand(1, 99),
                Puzzle9.AddCommand(1, 98)
        )

        val result = puzzle.processOrders(listOf(1, 2, 3), orders)
        assertEquals(listOf(1, 98, 99, 100, 2, 3), result)
    }

    @Test
    fun `processOrders list empty not empty and add commands at the end`() {
        val orders = listOf(
                Puzzle9.AddCommand(3, 98),
                Puzzle9.AddCommand(4, 99),
                Puzzle9.AddCommand(5, 100)
        )

        val result = puzzle.processOrders(listOf(1, 2, 3), orders)
        assertEquals(listOf(1, 2, 3, 98, 99, 100), result)
    }

    @Test
    fun `processOrders list empty not empty and asiodja`() {
        val orders = listOf(
                Puzzle9.AddCommand(0, 98),
                Puzzle9.AddCommand(4, 99),
                Puzzle9.AddCommand(0, 100)
        )

        val result = puzzle.processOrders(listOf(1, 2, 3), orders)
        assertEquals(listOf(100, 98, 1, 2, 3, 99), result)
    }

    @Test
    fun `example 1`() {
        assertEquals(8317, puzzle.getWinningScore(10, 1618))
    }

    @Test
    fun `example 2`() {
        assertEquals(146373, puzzle.getWinningScore(13, 7999))
    }

    @Test
    fun `example 3`() {
        assertEquals(2764, puzzle.getWinningScore(17, 1104))
    }

    @Test
    fun `example 4`() {
        assertEquals(54718, puzzle.getWinningScore(21, 6111))
    }

    @Test
    fun `example 5`() {
        assertEquals(37305, puzzle.getWinningScore(30, 5807))
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(375465, result)
    }

    @Ignore
    @Test
    fun `puzzle part b slow`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(0, result)
    }

    class Puzzle9 {
        data class AddCommand(val index: Int, val value: Int)

        data class Shift(val from: Int, val to: Int)

        fun solveOne(puzzleText: String): Long {
            val (playerCount, lastMarble) = puzzleText.split(" ").mapNotNull { it.toIntOrNull() }
            return getWinningScore(playerCount, lastMarble)
        }

        fun solveTwo(puzzleText: String): Long {
            val (playerCount, lastMarble) = puzzleText.split(" ").mapNotNull { it.toIntOrNull() }
            return getWinningScore(playerCount, lastMarble * 100)
        }

        fun processOrders(arrayList: List<Int>, orders: List<AddCommand>): List<Int> {
            // convert orders into shifts
            val shifts = orders.map { order -> Shift(order.index, arrayList.lastIndex) }
            val buffer = (0 until arrayList.size).map { 0 }.toMutableList()
            val returnArray = (0 until buffer.size + shifts.size).map { 0 }.toMutableList()

            shifts.forEach { shift ->
                (shift.from .. shift.to).forEach { buffer[it]++ }
            }

            // Now the buffer should say how much each element is supposed to shift by
            buffer.forEachIndexed { fromIndex, shiftAmount ->
                returnArray[fromIndex + shiftAmount] = arrayList[fromIndex]
            }

            // Now to normalise the add commands
            val normalisedOrders = (0 until orders.size).map { index ->
                val addCommand = orders[index]

                if (index == orders.lastIndex) {
                    addCommand
                }
                else {
                    val thisCommandsIndex = addCommand.index

                    // Count the number of commands ahead of this command that have an index
                    val count = orders.subList(index + 1, orders.size)
                            .count {
                                it.index <= thisCommandsIndex
                            }

                    addCommand.copy(index = thisCommandsIndex + count)
                }
            }

            normalisedOrders.forEach { order -> returnArray[order.index] = order.value}
            return returnArray
        }

        fun getWinningScore(playerCount: Int, lastMarble: Int): Long {
            val elvesToScores = (1 .. playerCount).associate { it to 0L }.toMutableMap()

            val arrayList = ArrayList<Int>()
            arrayList.add(0)
            arrayList.add(1)

            var currentMarbleIndex = 1

            (2 .. lastMarble).forEach { marbleNumber ->

                if (marbleNumber % 100000 == 0) println(marbleNumber)

                if (marbleNumber % 23 == 0) {
                    val elfNumber = (marbleNumber % elvesToScores.size) + 1
                    val currentScore = elvesToScores[elfNumber]!!

                    // Find the marble seven to the left of the currentMarble
                    val indexSevenDown =  if (currentMarbleIndex - 7 >= 0) currentMarbleIndex - 7 else arrayList.size + (currentMarbleIndex - 7)
                    val marbleNumberSevenDown = arrayList[indexSevenDown]

                    // Add the marble number to the current score
                    elvesToScores[elfNumber] = currentScore + marbleNumber + marbleNumberSevenDown

                    // Remove the marble number that is seven down
                    currentMarbleIndex = if (indexSevenDown == arrayList.lastIndex) 0 else indexSevenDown

                    arrayList.removeAt(indexSevenDown)
                }
                else {
                    // Figure out what index to put this marble into
                    val indexOneAheadClockWise = (currentMarbleIndex + 1) % arrayList.size
                    val indexTwoAheadClockWise = (currentMarbleIndex + 2) % arrayList.size

                    if (indexOneAheadClockWise > indexTwoAheadClockWise) {
                        arrayList.add(marbleNumber)
                        currentMarbleIndex = arrayList.lastIndex
                    }
                    else {
                        arrayList.add(indexTwoAheadClockWise, marbleNumber)
                        currentMarbleIndex = indexTwoAheadClockWise
                    }
                }
            }

            return elvesToScores.values.max()!!
        }
    }
}