package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle8Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle8.txt").readText().replace("\r", "")
    val puzzle = Puzzle8()

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
        val puzzleText = ""
        val result = puzzle.solveOne(puzzleText)
        assertEquals(514579, result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = "nop +0\n" +
                "acc +1\n" +
                "jmp +4\n" +
                "acc +3\n" +
                "jmp -3\n" +
                "acc -99\n" +
                "acc +1\n" +
                "jmp -4\n" +
                "acc +6"
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(8, result)
    }
}

data class State(val accumulator: Int, val programCounter: Int) {
    companion object {
        val INITIAL = State(0, 0)
    }
}

interface Instruction {
    fun execute(state: State): State
}

class Nop(val delta: Int) : Instruction {
    override fun execute(state: State): State {
        return state.copy(programCounter = state.programCounter + 1)
    }

    fun toJmp(): Jmp {
        return Jmp(delta)
    }
}

class Acc(val delta: Int) : Instruction {
    override fun execute(state: State): State {
        return state.copy(
            accumulator = state.accumulator + delta,
            programCounter = state.programCounter + 1
        )
    }
}

class Jmp(val delta: Int) : Instruction {
    override fun execute(state: State): State {
        return state.copy(
            programCounter = state.programCounter + delta
        )
    }

    fun toNop(): Nop {
        return Nop(delta)
    }
}


class Puzzle8 {
    fun solveOne(puzzleText: String): Int {
        val instructions = puzzleText.split("\n").map { line ->
            val tmp = line.split(" ")
            val opCode = tmp[0]

            when (opCode) {
                "acc" -> Acc(tmp[1].toInt())
                "jmp" -> Jmp(tmp[1].toInt())
                "nop" -> Nop(tmp[1].toInt())
                else -> throw RuntimeException()
            }
        }

        val visitedIndexes = mutableSetOf<Int>()
        var state = State.INITIAL
        var currentInstruction = instructions[0]


        while (true) {
            state = currentInstruction.execute(state)
            currentInstruction = instructions.get(state.programCounter)

            if (!visitedIndexes.add(state.programCounter)) {
                return state.accumulator
            }
        }
    }

    fun solveTwo(puzzleText: String): Int {
        val instructions = puzzleText.split("\n").map { line ->
            val tmp = line.split(" ")
            val opCode = tmp[0]

            when (opCode) {
                "acc" -> Acc(tmp[1].toInt())
                "jmp" -> Jmp(tmp[1].toInt())
                "nop" -> Nop(tmp[1].toInt())
                else -> throw RuntimeException()
            }
        }.toMutableList()

        (0 until instructions.size).forEach { index ->
            val instruction = instructions[index]

            if (instruction is Nop) {
                instructions[index] = instruction.toJmp()

                if (loopsForever(instructions)) {
                    instructions[index] = instruction
                }
            }
            else if (instruction is Jmp) {
                instructions[index] = instruction.toNop()

                if (loopsForever(instructions)) {
                    instructions[index] = instruction
                }
            }
        }

        return 1337


    }

    fun loopsForever(instructions: List<Instruction>): Boolean {
        val visitedIndexes = mutableSetOf<Int>()
        var state = State.INITIAL
        var currentInstruction: Instruction? = instructions[0]

        while (currentInstruction != null) {
            state = currentInstruction.execute(state)
            currentInstruction = instructions.getOrNull(state.programCounter)

            if (!visitedIndexes.add(state.programCounter)) {
                return true
            }
        }

        println(state.accumulator)
        return false
    }
}

