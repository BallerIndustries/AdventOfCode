package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle15Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle15.txt").readText().replace("\r", "")
    val puzzle = Puzzle15()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(203, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(9007186, result)
    }

    @Test
    fun `example part a`() {
        val puzzleText = "0,3,6"
        val result = puzzle.solveOne(puzzleText)
        assertEquals(436, result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = "0,3,6"
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(175594, result)
    }

    @Test
    fun `iterate a lot`() {
        var number = 0
        (0 until 30_000_000).forEach {
            number++
        }




    }
}

class Puzzle15 {
    data class LastTwo(val indexes: List<Int>) {
        fun add(index: Int): LastTwo {
            val newList = (listOf(index) + this.indexes).subList(0, 2)
            return this.copy(indexes = newList)
        }
    }

    data class LastTwoV2(val index0: Int?, val index1: Int?) {
        fun add(index: Int): LastTwoV2 {
            return if (index0 == null) {
                this.copy(index0 = index)
            } else
                this.copy(index0 = index, index1 = this.index0)
        }

        fun size(): Int {
            return listOfNotNull(index0, index1).count()
        }
    }

    class LastTwoV3(var index0: Int?, var index1: Int?) {
        fun add(index: Int): LastTwoV3 {
            this.index1 = this.index0
            this.index0 = index
            return this
        }
//            return if (index0 == null) {
//                this.copy(index0 = index)
//            } else
//                this.copy(index0 = index, index1 = this.index0)
//        }

        fun size(): Int {
            return listOfNotNull(index0, index1).count()
        }
    }

    fun solveOne(puzzleText: String): Int {
        val numbers = puzzleText.split(",").map { it.toInt() }
        val numbersToLastTwo = numbers.mapIndexed { index, number ->
            number to LastTwoV2(index+1, null)
        }.toMap().toMutableMap()

        var turn = numbers.size + 1
        var lastNumber = numbers.last()


        while (true) {
            val lastTwo = numbersToLastTwo[lastNumber]!!

            val currentNumber = if (lastTwo.size() < 2) {
                0
            } else if (lastTwo.size() == 2) {
                lastTwo.index0!! - lastTwo.index1!!
            }
            else {
                throw RuntimeException()
            }

            println("Turn = $turn currentNumber = $currentNumber")
            val jurness = numbersToLastTwo[currentNumber]
            numbersToLastTwo[currentNumber] = jurness?.add(turn) ?: LastTwoV2(index0 = turn, index1 = null)

            if (turn == 2020) {
                return currentNumber
            }

            turn++
            lastNumber = currentNumber
        }
    }

    fun solveTwo(puzzleText: String): Int {
        val numbers = puzzleText.split(",").map { it.toInt() }
        val numbersToLastTwo = numbers.mapIndexed { index, number ->
            number to LastTwoV3(index+1, null)
        }.toMap().toMutableMap()

        var turn = numbers.size + 1
        var lastNumber = numbers.last()


        while (true) {
            val lastTwo = numbersToLastTwo[lastNumber]!!

            val currentNumber = when {
                lastTwo.size() < 2 -> 0
                lastTwo.size() == 2 -> lastTwo.index0!! - lastTwo.index1!!
                else -> throw RuntimeException()
            }

            //println("Turn = $turn currentNumber = $currentNumber")

            if (numbersToLastTwo.containsKey(currentNumber)) {
                numbersToLastTwo[currentNumber]?.add(turn)
            }
            else {
                numbersToLastTwo[currentNumber] = LastTwoV3(index0 = turn, index1 = null)
            }

//            val jurness = numbersToLastTwo[currentNumber]
//            numbersToLastTwo[currentNumber] = jurness?.add(turn) ?: LastTwoV3(index0 = turn, index1 = null)

            if (turn == 30_000_000) {
                return currentNumber
            }

            turn++
            lastNumber = currentNumber
        }
    }
}

