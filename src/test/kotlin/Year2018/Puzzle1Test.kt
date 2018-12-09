package Year2018

import org.junit.Assert
import org.junit.Test

class Puzzle1Test {

    val puzzle = Puzzle1()

    @Test
    fun `puzzle part a`() {
        val horse = puzzle.solve(Puzzle1Test::class.java.getResource("/2018/puzzle1.txt").readText())
        println(horse)
    }

    @Test
    fun `puzzle part b`() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Test
    fun `asdij asdij asdij`() {
        val dog = puzzle.solve("+3\r\n+3\r\n+4\r\n-2\r\n-4")
        Assert.assertEquals(dog, 10)
    }
}

class Puzzle1 {
    fun solve(horse: String): Int {
        var currentFreq = 0
        val seenBefore = mutableSetOf(currentFreq)
        val deltas = horse.split("\r\n")
            .map { line -> parseNumber(line) }

        while (true) {
            deltas.forEach { delta ->
                val nextFreq = currentFreq + delta

                if (!seenBefore.add(nextFreq)) {
                    return nextFreq
                }

                currentFreq = nextFreq
            }
        }
    }

    private fun parseNumber(line: String): Int {
        val number = line.substring(1).toInt()

        val freqDelta = if (line[0] == '-') {
            number * -1
        } else {
            number
        }
        return freqDelta
    }
}

