package Year2018

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test

class Puzzle12Test {
    val puzzleText = this::class.java.getResource(
            "/2018/puzzle12.txt").readText()
    val puzzle = Puzzle12()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText, 20)

        // Not 860
        // Not 3547 too low
        assertEquals(0, result)
    }

    @Test
    fun `example a`() {
        val exampleA = "initial state: #..#.#..##......###...###\n" +
            "\n" +
            "...## => #\n" +
            "..#.. => #\n" +
            ".#... => #\n" +
            ".#.#. => #\n" +
            ".#.## => #\n" +
            ".##.. => #\n" +
            ".#### => #\n" +
            "#.#.# => #\n" +
            "#.### => #\n" +
            "##.#. => #\n" +
            "##.## => #\n" +
            "###.. => #\n" +
            "###.# => #\n" +
            "####. => #"

        val result = puzzle.solveOne(exampleA, 20)
        assertEquals(325, result)

//        with (puzzle.stateAfterGeneration(exampleA, 1)) {
//            assertTrue("...#...#....#.....#..#..#..#...........".contains(this))
//        }
//
//        with (puzzle.stateAfterGeneration(exampleA, 2)) {
//            assertTrue("...##..##...##....#..#..#..##..........".contains(this))
//        }
//
//        with (puzzle.stateAfterGeneration(exampleA, 3)) {
//            assertTrue("..#.#...#..#.#....#..#..#...#..........".contains(this))
//        }
//
//        with (puzzle.stateAfterGeneration(exampleA, 20)) {
////            assertEquals(".#....##....#####...#######....#.#..##.", this)
//            assertTrue(".#....##....#####...#######....#.#..##.".contains(this))
//        }




    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("b", result)
    }

    @Test
    fun `trim periods`() {
        assertEquals("###", "......###......".trimPeriods())
        assertEquals("#.###.#", ".....#.###.#.....".trimPeriods())
    }

    class Puzzle12 {
        fun solveOne(puzzleText: String, numberOfGens: Int): Int {
            val state = stateAfterGeneration(puzzleText, 20)

            val indexesOfPotsWithPlants = state.mapIndexed { index, character ->
                if (character == '#') index - 30 else null
            }.filterNotNull().sum()

            return indexesOfPotsWithPlants
        }

        fun stateAfterGeneration(puzzleText: String, numberOfGens: Int): String {
            val lines = puzzleText.split("\n")
            val padding = (0 until 30).map { '.' }.joinToString("")
            val initialState = lines[0].replace("initial state: ", "")
            val paddedState = padding + initialState + padding

            val rules = lines.subList(2, lines.size).associate { line ->
                val tmp = line.split(" => ")
                tmp[0] to tmp[1][0]
            }

            var nextState = paddedState

            (0 until numberOfGens).forEach {
                nextState = showMeTheNextGenShit(nextState, rules)
                println(nextState)
            }

            return nextState
        }

        private fun showMeTheNextGenShit(state: String, rules: Map<String, Char>): String {
            return state.mapIndexed { index, _ ->
                val fiveCharacterString = get5CharSubString(state, index)
                rules[fiveCharacterString] ?: '.'
            }.joinToString("")
        }

        private fun get5CharSubString(text: String, index: Int): String {
            val twoBefore = if (index - 2 >= 0) text[index-2] else '.'
            val oneBefore = if (index - 1 >= 0) text[index-1] else '.'
            val oneAfter = if (index + 1 <= text.lastIndex) text[index+1] else '.'
            val twoAfter = if (index + 2 <= text.lastIndex) text[index+2] else '.'



            return listOf(twoBefore, oneBefore, text[index], oneAfter, twoAfter).joinToString("")
        }

        fun solveTwo(puzzleText: String): String {
            return ""
        }
    }
}

fun String.trimPeriods(): String {
    val first = this.indexOfFirst { it == '#' }
    val last = this.indexOfLast { it == '#' }
    return this.substring(first, last + 1)
}