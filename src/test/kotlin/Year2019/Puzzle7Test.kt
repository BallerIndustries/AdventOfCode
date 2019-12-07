package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle7Test {
    val puzzleText = Puzzle7Test::class.java.getResource("/2019/puzzle7.txt").readText().replace("\r", "")
    val puzzle = Puzzle7()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(262086, result)
    }

    @Test
    fun `puzzle part b`() {
        // NOT 262086
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(5371621, result)
    }

    @Test
    fun `example a`() {
        val result = puzzle.solveTwo("3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5")
        assertEquals(139629729, result)
    }
}

class Puzzle7 {
    fun solveOne(puzzleText: String): Int? {
        val virtualMachine = IntCodeVirtualMachine()
        val permutations = getPermutationsForRange(0, 4)
        val input = puzzleText.split(",").map { it.toLong() }

        return permutations.map { phases ->
            val (first, second, third, fourth, fifth) = phases.map { phase -> State(input, userInput = listOf(phase)) }
            var result = virtualMachine.runProgram(first.addUserInput(0))
            result = virtualMachine.runProgram(second.addUserInput(result.lastPrintedValue!!.toInt()))
            result = virtualMachine.runProgram(third.addUserInput(result.lastPrintedValue!!.toInt()))
            result = virtualMachine.runProgram(fourth.addUserInput(result.lastPrintedValue!!.toInt()))
            result = virtualMachine.runProgram(fifth.addUserInput(result.lastPrintedValue!!.toInt()))
            result.lastPrintedValue!!.toInt()
        }.max()
    }

    fun solveTwo(puzzleText: String): Int? {
        val virtualMachine = IntCodeVirtualMachine()
        val permutations = getPermutationsForRange(5, 9)
        val allOutputs = mutableListOf<Int>()

        permutations.forEach {phases ->
            val input = puzzleText.split(",").map { it.toLong() }
            val amplifiers = phases.map { phase -> State(input, userInput = listOf(phase)) }.toMutableList()
            var lastOutput = 0
            var index = 0

            while (!amplifiers.last().isHalted) {
                val state = amplifiers[index]
                val currentAmplifier: State = virtualMachine.runProgram(state.copy(userInput = state.userInput + lastOutput))

                if (currentAmplifier.lastPrintedValue != null) {
                    lastOutput = currentAmplifier.lastPrintedValue!!.toInt()
                }

                amplifiers[index] = currentAmplifier
                index = if (index == amplifiers.lastIndex) 0 else index + 1
            }

            allOutputs.add(lastOutput)
        }

        return allOutputs.max()
    }

    private fun getPermutationsForRange(from: Int, to: Int): List<List<Int>> {
        val permutations = mutableListOf<List<Int>>()

        (from..to).forEach { A ->
            (from..to).forEach { B ->
                (from..to).forEach { C ->
                    (from..to).forEach { D ->
                        (from..to).forEach { E ->
                            if (setOf(A, B, C, D, E).size == 5) {
                                permutations.add(listOf(A, B, C, D, E))
                            }
                        }
                    }
                }
            }
        }

        return permutations
    }
}