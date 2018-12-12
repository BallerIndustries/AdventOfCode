package Year2018

import junit.framework.Assert.assertEquals
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
        assertEquals(4110, result)
    }

    @Test
    fun `looking for cycles`() {
        var diff = puzzle.solveOne(puzzleText, 154) - puzzle.solveOne(puzzleText, 153)
        println(diff)

        diff = puzzle.solveOne(puzzleText, 155) - puzzle.solveOne(puzzleText, 154)
        println(diff)



//        println(puzzle.solveOne(puzzleText, 153))
//        println(puzzle.solveOne(puzzleText, 154))
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
        assertEquals(2650000000466L, result)
    }

    @Test
    fun `trim periods`() {
        assertEquals("###", "......###......".trimPeriods())
        assertEquals("#.###.#", ".....#.###.#.....".trimPeriods())
    }

    class Puzzle12 {
        fun solveOne(puzzleText: String, numberOfGens: Int): Int {
            val state = stateAfterGeneration(puzzleText, numberOfGens)

            val indexesOfPotsWithPlants = state.mapIndexed { index, character ->
                if (character == '#') index - 200 else null
            }.filterNotNull().sum()

            return indexesOfPotsWithPlants
        }

        fun stateAfterGeneration(puzzleText: String, numberOfGens: Int): String {
            val allDogs = mutableSetOf<String>()

            val lines = puzzleText.split("\n")
            val padding = (0 until 200).map { '.' }.joinToString("")
            val initialState = lines[0].replace("initial state: ", "")
            allDogs.add(initialState)

            val paddedState = padding + initialState + padding

            val rules = lines.subList(2, lines.size).associate { line ->
                val tmp = line.split(" => ")
                tmp[0] to tmp[1][0]
            }

            var nextState = paddedState

            (0 until numberOfGens).forEach {
                nextState = showMeTheNextGenShit(nextState, rules)

                val withoutPs = nextState.trimPeriods()

//                if (!allDogs.add(withoutPs)) {
//                    println("CYCLE! at generation = $it withoutPs = $withoutPs")
//                }



//                println(nextState)
            }

            println(nextState)

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

        fun solveTwo(puzzleText: String): Long {


            val first = solveOne(puzzleText, 153).toLong()
            val second = solveOne(puzzleText, 154).toLong()
            var diff = second - first

            val bigNumber = 50000000000L


            val dog = bigNumber - 153L
            val hugeGen = (diff * dog) + first

            return hugeGen


//            println(diff)
//
//            diff = puzzle.solveOne(puzzleText, 155) - puzzle.solveOne(puzzleText, 154)
//            println(diff)
        }
    }
}

fun String.trimPeriods(): String {
    val first = this.indexOfFirst { it == '#' }
    val last = this.indexOfLast { it == '#' }
    return this.substring(first, last + 1)
}