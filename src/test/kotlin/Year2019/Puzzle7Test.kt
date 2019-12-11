package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle7Test {
    val puzzleText = Puzzle7Test::class.java.getResource("/2019/puzzle7.txt").readText().replace("\r", "")
    val puzzle = Puzzle7()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(262086L, result)
    }

    @Test
    fun `puzzle part b`() {
        // NOT 262086
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(5371621L, result)
    }

    @Test
    fun `example a`() {
        val result = puzzle.solveTwo("3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5")
        assertEquals(139629729L, result)
    }
}

class Puzzle7 {
    fun solveOne(puzzleText: String): Long? {
        val virtualMachine = IntCodeVirtualMachine()
        val input = puzzleText.split(",").map { it.toLong() }

        return listOf(0L, 1L, 2L, 3L, 4L).permutations().map { phases ->
            val (first, second, third, fourth, fifth) = phases.map { phase -> State(input, userInput = listOf(phase)) }
            var result = virtualMachine.runProgram(first.addUserInput(0))
            result = virtualMachine.runProgram(second.addUserInput(result.outputList.first()))
            result = virtualMachine.runProgram(third.addUserInput(result.outputList.first()))
            result = virtualMachine.runProgram(fourth.addUserInput(result.outputList.first()))
            result = virtualMachine.runProgram(fifth.addUserInput(result.outputList.first()))
            result.outputList.first()
        }.max()
    }

    fun solveTwo(puzzleText: String): Long? {
        val virtualMachine = IntCodeVirtualMachine()
        val allOutputs = mutableListOf<Long>()

        listOf(5L, 6L, 7L, 8L, 9L).permutations().forEach { phases ->
            val input = puzzleText.split(",").map { it.toLong() }
            val amplifiers = phases.map { phase -> State(input, userInput = listOf(phase)) }.toMutableList()
            var lastOutput = 0L
            var index = 0

            while (!amplifiers.last().isHalted) {
                val state = amplifiers[index]
                val currentAmplifier: State = virtualMachine.runProgram(state.copy(userInput = state.userInput + lastOutput))

                if (currentAmplifier.outputList != null) {
                    lastOutput = currentAmplifier.outputList.first()
                }

                amplifiers[index] = currentAmplifier
                index = if (index == amplifiers.lastIndex) 0 else index + 1
            }

            allOutputs.add(lastOutput)
        }

        return allOutputs.max()
    }
}