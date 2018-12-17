package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.lang.StringBuilder

class Puzzle14Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle14.txt").readText()
    val puzzle = Puzzle14()

    @Test
    fun `first example part a`() {
        val result = puzzle.solveOne("9")
        assertEquals("5158916779", result)
    }

    @Test
    fun `second example part a`() {
        val result = puzzle.solveOne("5")
        assertEquals("0124515891", result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("1150511382", result)
    }

    @Test
    fun `first example part b`() {
        val result = puzzle.solveTwo("51589")
        assertEquals(9, result)
    }

    @Test
    fun `2 example part b`() {
        val result = puzzle.solveTwo("01245")
        assertEquals(5, result)
    }

    @Test
    fun `3 example part b`() {
        val result = puzzle.solveTwo("92510")
        assertEquals(18, result)
    }

    @Test
    fun `4 example part b`() {
        val result = puzzle.solveTwo("59414")
        assertEquals(2018, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(20173656, result)
    }

    class Puzzle14 {
        fun solveOne(puzzleText: String): String {
            val numberOfRecipes = puzzleText.toInt()
            val goUntil = numberOfRecipes + 10

            val state = StringBuilder("37")
            var firstElfIndex = 0
            var secondElfIndex = 1

            while (state.length <= goUntil) {
                val firstElfScore = state[firstElfIndex].toString().toInt()
                val secondElfScore = state[secondElfIndex].toString().toInt()
                val number = (firstElfScore + secondElfScore).toString()

                state.append(number)

                firstElfIndex = (firstElfIndex + firstElfScore + 1) % state.length
                secondElfIndex = (secondElfIndex + secondElfScore + 1) % state.length
            }

            return state.substring(numberOfRecipes, numberOfRecipes + 10)
        }

        private fun printState(state: String, firstElfIndex: Int, secondElfIndex: Int) {
            val printable = state.mapIndexed { index, character ->

                if (index == firstElfIndex) {
                    "($character)"
                } else if (index == secondElfIndex) {
                    "[$character]"
                } else {
                    " $character "
                }
            }.joinToString("")

            println(printable)
        }

        fun solveTwo(puzzleText: String): Int {
            val buffer = StringBuilder("37")

            var firstElfIndex = 0
            var secondElfIndex = 1

            while (true) {
                val firstElfScore = buffer[firstElfIndex].toString().toInt()
                val secondElfScore = buffer[secondElfIndex].toString().toInt()
                val number = (firstElfScore + secondElfScore).toString()
                buffer.append(number)

                firstElfIndex = (firstElfIndex + firstElfScore + 1) % buffer.length
                secondElfIndex = (secondElfIndex + secondElfScore + 1) % buffer.length

                val lastTwentyCharacters = getLastTwentyCharacters(buffer)
                val horse = lastTwentyCharacters.indexOf(puzzleText)

                if (horse != -1) {
                    if (buffer.length <= 20) return horse else return horse + buffer.length - 20
                }
            }
        }

        private fun getLastTwentyCharacters(text: StringBuilder): String {
            if (text.length <= 20) {
                return text.toString()
            }

            return text.substring(text.length - 20)
        }
    }
}