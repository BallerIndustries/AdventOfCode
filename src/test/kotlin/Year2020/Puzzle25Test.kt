package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle25Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle25.txt").readText().replace("\r", "")
    val puzzle = Puzzle25()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(11288669, result)
    }

    @Test
    fun `example part a`() {
        val puzzleText = "5764801\n17807724"
        val result = puzzle.solveOne(puzzleText)
        assertEquals(14897079, result)
    }

    @Test
    fun `determine secret loop size 1`() {
        val result: Long = puzzle.determineSecretLoopSize(5764801, 7)
        assertEquals(8, result)
    }

    @Test
    fun `determine secret loop size 2`() {
        val result: Long = puzzle.determineSecretLoopSize(17807724, 7)
        assertEquals(11, result)
    }
}

class Puzzle25 {
    fun solveOne(puzzleText: String): Long {
        val (cardPublicKey, doorPublicKey) = puzzleText.split("\n").map { it.toLong() }

        val cardSecretLoopSize = determineSecretLoopSize(cardPublicKey, 7)
        val doorSecretLoopSize = determineSecretLoopSize(doorPublicKey, 7)

        return transformLoop(doorPublicKey, cardSecretLoopSize)
    }

    fun determineSecretLoopSize(publicKey: Long, subjectNumber: Long): Long {
        var value = 1L
        var count = 0L

        while (true) {
            value = transform(value, subjectNumber)
            count++

            if (value == publicKey) {
                return count
            }
        }
    }

    fun transformLoop(subjectNumber: Long, loopSize: Long): Long {
        var value = 1L

        (0 until loopSize).forEach {
            value = transform(value, subjectNumber)
        }

        return value
    }

    private fun transform(value: Long, subjectNumber: Long): Long {
        var value1 = value
        value1 = value1 * subjectNumber
        value1 = value1 % 20201227
        return value1
    }
}

