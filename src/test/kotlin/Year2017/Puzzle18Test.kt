package Year2017

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle18Test {
    val puzzle = Puzzle18()
    val puzzleText = this::class.java.getResource("/2017/puzzle18.txt").readText().replace("\r", "")
    val exampleText = """
        set a 1
        add a 2
        mul a a
        mod a 5
        snd a
        set a 0
        rcv a
        jgz a -1
        set a 1
        jgz a -2
    """.trimIndent()

    @Test
    fun `example part a`() {
        val result = puzzle.solveOne(exampleText)
        assertEquals(4, result)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(9423, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(7620, result)
    }
}

class Puzzle18 {
    data class State(
        val inbox: MutableList<Long> = mutableListOf(),
        val outbox: MutableList<Long> = mutableListOf(),
        val programCounter: Long = 0L,
        val lastSoundPlayed: Long = -1L,
        val offset: Long? = null,
        val recoverInstruction: Long? = null,
        val registers: Map<Char, Long> = mapOf(),
        val sendCount: Long = 0L
    )

    fun solveOne(puzzleText: String): Long {
        val codes = puzzleText.split("\n").map { line -> Instruction.parsePartOne(line) }
        var state = State()

        while (state.recoverInstruction == null) {
            state = runProgram(codes, state)
        }

        return state.recoverInstruction!!
    }

    private fun runProgram(codes: List<Instruction>, programStateConst: State): State {
        var programState = programStateConst
        val instruction = codes[programState.programCounter.toInt()]
        programState = instruction.execute(programState)

        val programCounter = if (programState.offset != null) programState.programCounter + programState.offset!! else programState.programCounter + 1
        programState = programState.copy(offset = null, programCounter = programCounter)

        return programState
    }

    fun solveTwo(puzzleText: String): Long {
        val codes = puzzleText.split("\n").map { line -> Instruction.parsePartTwo(line) }
        val programZeroQueue = mutableListOf<Long>()
        val programOneQueue = mutableListOf<Long>()

        var programZeroState = State(inbox = programZeroQueue, outbox = programOneQueue, registers = mapOf('p' to 0L))
        var programOneState = State(inbox = programOneQueue, outbox = programZeroQueue, registers = mapOf('p' to 1L))

        while (true) {
            val programeZeroInstruction = codes[programZeroState.programCounter.toInt()]
            val programeOneInstruction = codes[programOneState.programCounter.toInt()]

            if (programeOneInstruction is Receive && programeZeroInstruction is Receive && programZeroQueue.isEmpty() && programOneQueue.isEmpty()) {
                break
            }

            programZeroState = runProgram(codes, programZeroState)
            programOneState = runProgram(codes, programOneState)
        }

        return programOneState.sendCount
    }

    data class Snd(val x: String) : Instruction {
        override fun execute(state: State): State {
            val lastSoundPlayed = getValueOrRegister(state, x)
            println("playing sound with frequency = $x")
            return state.copy(lastSoundPlayed = lastSoundPlayed)
        }
    }

    data class Set(val x: Char, val y: String) : Instruction {
        override fun execute(state: State): State {
            // Set register x to the value of y
            val yValue = getValueOrRegister(state, y)
            println("setting $x = $yValue")
            val newRegisters = state.registers + (x to yValue)
            return state.copy(registers = newRegisters)
        }
    }

    data class Add(val x: Char, val y: String) : Instruction {
        override fun execute(state: State): State {
            val yValue = getValueOrRegister(state, y)
            val xValue = getValueOrRegister(state, x.toString())
            println("setting $x = $xValue + $yValue")

            val newRegisters = state.registers + (x to xValue + yValue)
            return state.copy(registers = newRegisters)
        }
    }

    data class Mul(val x: Char, val y: String) : Instruction {
        override fun execute(state: State): State {
            val yValue = getValueOrRegister(state, y)
            val xValue = getValueOrRegister(state, x.toString())
            println("setting $x = $xValue * $yValue")
            val newRegisters = state.registers + (x to xValue * yValue)
            return state.copy(registers = newRegisters)
        }
    }

    data class Mod(val x: Char, val y: String) : Instruction {
        override fun execute(state: State): State {
            val yValue = getValueOrRegister(state, y)
            val xValue = getValueOrRegister(state, x.toString())
            println("setting $x = $xValue % $yValue")
            val newRegisters = state.registers + (x to xValue % yValue)
            return state.copy(registers = newRegisters)
        }
    }

    data class Receive(val x: Char): Instruction {
        override fun execute(state: State): State {
            if (state.inbox.isEmpty()) {
                println("inbox is empty, will wait for one cycle and try again")
                return state.copy(offset = 0)
            }
            else {
                val thejur = state.inbox.removeAt(0)
                println("setting $x = $thejur from the message queue")
                val newRegisters = state.registers + (x to thejur)
                return state.copy(registers = newRegisters)
            }
        }
    }

    data class Send(val x: Char): Instruction {
        override fun execute(state: State): State {
            val xValue = getValueOrRegister(state, x.toString())
            state.outbox.add(xValue)
            println("sending $xValue to the other program's message queue")
            return state.copy(sendCount = state.sendCount + 1)
        }
    }

    data class Rcv(val x: Char) : Instruction {
        override fun execute(state: State): State {
            val xValue = getValueOrRegister(state, x.toString())

            if (xValue != 0L) {
                return state.copy(recoverInstruction = state.lastSoundPlayed)
            }
            else {
                println("failed to recover, $x = $xValue")
                return state
            }
        }
    }

    data class Jgz(val x: Char, val y: String) : Instruction {
        override fun execute(state: State): State {
            val xValue = getValueOrRegister(state, x.toString())
            val yValue = getValueOrRegister(state, y)

            if (xValue > 0) {
                println("jumping offset = $yValue")
                return state.copy(offset = yValue)
            }
            else {
                println("skipping jump $x = 0")
                return state
            }
        }
    }

    interface Instruction {
        companion object {
            fun parsePartOne(text: String): Instruction {
                val tmp = text.split(" ")

                return when (tmp[0]) {
                    "snd" -> Snd(tmp[1])
                    "set" -> Set(tmp[1][0], tmp[2])
                    "add" -> Add(tmp[1][0], tmp[2])
                    "mul" -> Mul(tmp[1][0], tmp[2])
                    "mod" -> Mod(tmp[1][0], tmp[2])
                    "rcv" -> Rcv(tmp[1][0])
                    "jgz" -> Jgz(tmp[1][0], tmp[2])
                    else -> throw RuntimeException("fuck!")
                }
            }

            fun parsePartTwo(text: String): Instruction {
                val tmp = text.split(" ")

                return when (tmp[0]) {
                    "snd" -> Send(tmp[1][0])
                    "set" -> Set(tmp[1][0], tmp[2])
                    "add" -> Add(tmp[1][0], tmp[2])
                    "mul" -> Mul(tmp[1][0], tmp[2])
                    "mod" -> Mod(tmp[1][0], tmp[2])
                    "rcv" -> Receive(tmp[1][0])
                    "jgz" -> Jgz(tmp[1][0], tmp[2])
                    else -> throw RuntimeException("fuck!")
                }
            }
        }

        fun execute(state: State): State

        fun getValueOrRegister(state: State, valueOrRegister: String): Long {
            return if (valueOrRegister.toLongOrNull() != null) {
                valueOrRegister.toLong()
            } else {
                state.registers[valueOrRegister[0]] ?: 0
            }
        }
    }
}