package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigInteger

class Puzzle13Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle13.txt").readText().replace("\r", "")
    val puzzle = Puzzle13()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(964875, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(158661360, result)
    }

    @Test
    fun `example part a`() {
        val puzzleText = "939\n7,13,x,x,59,x,31,19"
        val result = puzzle.solveOne(puzzleText)
        assertEquals(295, result)
    }

    @Test
    fun `example part b - 1`() {
        val puzzleText = "939\n7,13,x,x,59,x,31,19"
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(BigInteger.valueOf(1068781), result)
    }

    @Test
    fun `example part b - 2`() {
        val puzzleText = "939\n17,x,13,19"
        val result = puzzle.solveTwo(puzzleText)
        val result2 = puzzle.solveTwoSpeedy(puzzleText)
        assertEquals(BigInteger.valueOf(3417L), result)
        assertEquals(BigInteger.valueOf(3417L), result2)
    }

    @Test
    fun `example part b - 3`() {
        val puzzleText = "939\n1789,37,47,1889"
        val result1 = puzzle.solveTwo(puzzleText)
        val result2 = puzzle.solveTwoSpeedy(puzzleText)
        assertEquals(BigInteger.valueOf(1202161486), result1)
        assertEquals(BigInteger.valueOf(1202161486), result2)
    }

    @Test
    fun `example part b - 4`() {
        val puzzleText = "939\n3,x,4,5"
        val result1 = puzzle.solveTwo(puzzleText)
        val result2 = puzzle.solveTwoSpeedy(puzzleText)
        assertEquals(BigInteger.valueOf(42), result1)
        assertEquals(BigInteger.valueOf(42), result2)
    }
}

class Puzzle13 {
    fun solveOne(puzzleText: String): Int {
        val lines = puzzleText.split("\n")
        val myTimestamp = lines[0].toInt()
        val busIds = lines[1].split(",").filter { it != "x" }.map { it.toInt() }

        val offsets = busIds.associateWith { busId ->
            val aa = ((myTimestamp / busId) + 1) * busId
            aa
        }

        val (busId, busLeaveTime) = offsets.minBy { it.value }!!
        return busId * (busLeaveTime - myTimestamp)
    }

    data class Constraint(val offset: Int, val multiple: Int) {
        fun bigOffset() = BigInteger.valueOf(offset.toLong())
        fun bigMultiple() = BigInteger.valueOf(multiple.toLong())
    }

    fun solveTwo(puzzleText: String): BigInteger {
        val constraints = parseConstraints(puzzleText)


//        println("{")
        print("solve ")
        constraints.forEach {
            print("(t + ${it.offset}) mod ${it.multiple} = 0;")
        }
//        println("}")

        // {
        // (t + 0) mod 29 = 0
        // (t + 23) mod 37 = 0
        // (t + 29) mod 631 = 0
        // (t + 47) mod 13 = 0
        // (t + 48) mod 19 = 0
        // (t + 52) mod 23 = 0
        // (t + 60) mod 383 = 0
        // (t + 70) mod 41 = 0
        // (t + 77) mod 17 = 0
        // }

        return solveConstraints(constraints)
    }

    private fun solveConstraints(constraints: List<Constraint>): BigInteger {
        val biggestConstraint = constraints.maxBy { it.multiple }!!
        val multiple = BigInteger.valueOf(biggestConstraint.multiple.toLong())
        var current = BigInteger.ZERO

        while (true) {
            val firstTime = current - biggestConstraint.bigOffset() //BigInteger.valueOf(biggestConstraint.offset.toLong())

            val allConstraintsMatch = constraints.all {
                val result = (firstTime + it.bigOffset()) % it.bigMultiple()
                result == BigInteger.ZERO
            }

            if (allConstraintsMatch) {
                return firstTime
            }

            current += multiple
        }
    }

    private fun parseConstraints(puzzleText: String): List<Constraint> {
        val constraints = puzzleText.split("\n")[1].split(",").mapIndexed { index, text ->
            if (text == "x") {
                null
            } else {
                Constraint(index, text.toInt())
            }
        }.filterNotNull()

        return constraints
    }

    fun solveTwoSpeedy(puzzleText: String): BigInteger {
        val constraints = parseConstraints(puzzleText).sortedByDescending { it.multiple }
        val twoBiggest = constraints.subList(0, 2)
        val increment = solveConstraints(twoBiggest)
        val biggestConstraint = constraints.first()
        var current = increment

        while (true) {
            val time = current - biggestConstraint.bigOffset() //BigInteger.valueOf(biggestConstraint.offset.toLong())

            val allConstraintsMatch = constraints.all {
                val result = (time + it.bigOffset()) % it.bigMultiple()
                result == BigInteger.ZERO
            }

            if (allConstraintsMatch) {
                return time
            }

            current += increment
        }
    }
}

