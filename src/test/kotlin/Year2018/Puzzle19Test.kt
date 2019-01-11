package Year2018

import junit.framework.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test
import java.lang.RuntimeException

class Puzzle19Test {
    val puzzleText = this::class.java.getResource("/2018/puzzle19.txt").readText().replace("\r", "")
    val puzzle = Puzzle19()

    @Test
    fun `example a`() {
        val exampleAText = """
            #ip 0
            seti 5 0 1
            seti 6 0 2
            addi 0 1 0
            addr 1 2 3
            setr 1 0 0
            seti 8 0 4
            seti 9 0 5
        """.trimIndent()

        val valInRegisterZero = puzzle.solveOne(exampleAText)
        assertEquals(6, valInRegisterZero)
    }

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(1152, result)
    }

    @Test
    @Ignore("Too slow")
    fun `puzzle part b`() {
       val result = puzzle.solveTwo(puzzleText)
        assertEquals(213057, result)
    }

    class Puzzle19 {
        fun solveOne(puzzleText: String, initialState: State = State.Zero()): Int {
            val lines = puzzleText.split("\n")
            val instructionRegister = lines[0].split(" ")[1].toInt()
            var instructionPointerValue = 0

            val instructionNumberToInstruction = lines.subList(1, lines.size)
                .mapIndexed { index, line -> index + 2 to line }
                .toMap()

            var state = initialState

//            R3 = 1
//
//            do {
//                if (R1 * R3 == R5) {
//                    R0 += R1
//                    R2 = 1
//                }
//                else {
//                    R2 = 0
//                }
//
//                R3 += 1
//            } while (R3 <= R5)




            while (true) {
                // The instruction pointer contains 0, and so the first instruction is executed (seti 5 0 1).
                // It updates register 0 to the current instruction pointer value (0),
                // sets register 1 to 5,
                // sets the instruction pointer to the value of register 0
                // (which has no effect, as the instruction did not modify register 0),
                // and then adds one to the instruction pointer.

                if (instructionPointerValue == 2 && state[1] != 0) {
                    if (state[5] % state[1] == 0) {
                        state[0] += state[1]
                    }

                    state[2] = 0
                    state[3] = state[5]
                    instructionPointerValue = 12
                    continue
                }



                val instructionText = instructionNumberToInstruction[instructionPointerValue + 2] ?: break
                val command = Command.from(instructionText)
                val instruction = Instruction.from(instructionText)

                // Write instructionPointer to instructionPointer register
                state = state.setValue(instructionRegister, instructionPointerValue)

                // Execute the instruction
                state = instruction.execute(state, command)

                // Read in the instruction pointer value
                instructionPointerValue = state.getValue(instructionRegister)

                // Increment ip
                instructionPointerValue++
            }

            return state.getValue(0)
        }

        fun solveTwo(puzzleText: String): Int {
            return solveOne(puzzleText, State.Funkazilla())
        }
    }

    abstract class Instruction {
        companion object {
            private val nameToInstruction = mapOf(
                "addr"  to AddR(),
                "addi"  to AddI(),
                "mulr"  to MulR(),
                "muli"  to MulI(),
                "bandr" to BAndR(),
                "bandi" to BAndI(),
                "borr"  to BOrR(),
                "bori"  to BOrI(),
                "setr"  to SetR(),
                "seti"  to SetI(),
                "gtir"  to GTIR(),
                "gtri"  to GTRI(),
                "gtrr"  to GTRR(),
                "eqir"  to EQIR(),
                "eqri"  to EQRI(),
                "eqrr"  to EQRR()
            )

            fun from(instructionText: String): Instruction {
                return nameToInstruction[instructionText.split(" ")[0]]!!
            }
        }

        abstract fun execute(state: State, command: Command): State
    }

    class AddR : Instruction() {
        override fun execute(state: State, command: Command): State {
            val regCNumber = command.third
            val result = state.getValue(command.first) + state.getValue(command.second)

            return state.setValue(regCNumber, result)
        }
    }

    class AddI: Instruction() {
        override fun execute(state: State, command: Command): State {
            val regCNumber = command.third
            val result = state.getValue(command.first) + command.second

            return state.setValue(regCNumber, result)
        }
    }

    class MulR: Instruction() {
        override fun execute(state: State, command: Command): State {
            val regCNumber = command.third
            val result = state.getValue(command.first) * state.getValue(command.second)

            return state.setValue(regCNumber, result)
        }
    }

    class MulI: Instruction() {
        override fun execute(state: State, command: Command): State {
            val regCNumber = command.third
            val result = state.getValue(command.first) * command.second

            return state.setValue(regCNumber, result)
        }
    }

    class BAndR: Instruction() {
        override fun execute(state: State, command: Command): State {
            val regCNumber = command.third
            val result = state.getValue(command.first) and state.getValue(command.second)

            return state.setValue(regCNumber, result)
        }
    }

    class BAndI: Instruction() {
        override fun execute(state: State, command: Command): State {
            val regCNumber = command.third
            val result = state.getValue(command.first) and command.second

            return state.setValue(regCNumber, result)
        }
    }

    class BOrR: Instruction() {
        override fun execute(state: State, command: Command): State {
            val regCNumber = command.third
            val result = state.getValue(command.first) or state.getValue(command.second)

            return state.setValue(regCNumber, result)
        }
    }

    class BOrI: Instruction() {
        override fun execute(state: State, command: Command): State {
            val regCNumber = command.third
            val result = state.getValue(command.first) or command.second

            return state.setValue(regCNumber, result)
        }
    }

    class SetR: Instruction() {
        override fun execute(state: State, command: Command): State {
            val regCNumber = command.third
            val result = state.getValue(command.first)

            return state.setValue(regCNumber, result)
        }
    }

    class SetI: Instruction() {
        override fun execute(state: State, command: Command): State {
            val regCNumber = command.third
            val result = command.first

            return state.setValue(regCNumber, result)
        }
    }

    class GTIR: Instruction() {
        override fun execute(state: State, command: Command): State {

            //sets register C to 1 if value A is greater than register B. Otherwise, register C is set to 0.
            val regCNumber = command.third
            val result = if (command.first > state.getValue(command.second)) 1 else 0

            return state.setValue(regCNumber, result)
        }
    }

    class GTRI: Instruction() {
        override fun execute(state: State, command: Command): State {

            //sets register C to 1 if register A is greater than value B. Otherwise, register C is set to 0.
            val regCNumber = command.third
            val result = if (state.getValue(command.first) > command.second) 1 else 0

            return state.setValue(regCNumber, result)
        }
    }

    class GTRR: Instruction() {
        override fun execute(state: State, command: Command): State {

            //sets register C to 1 if register A is greater than register B. Otherwise, register C is set to 0.
            val regCNumber = command.third
            val result = if (state.getValue(command.first) > state.getValue(command.second)) 1 else 0

            return state.setValue(regCNumber, result)
        }
    }

    class EQIR: Instruction() {
        override fun execute(state: State, command: Command): State {

            //sets register C to 1 if value A is equal to register B. Otherwise, register C is set to 0.
            val regCNumber = command.third
            val result = if (command.first == state.getValue(command.second)) 1 else 0

            return state.setValue(regCNumber, result)
        }
    }

    class EQRI: Instruction() {
        override fun execute(state: State, command: Command): State {
            val regCNumber = command.third
            val result = if (state.getValue(command.first) == command.second) 1 else 0

            return state.setValue(regCNumber, result)
        }
    }

    class EQRR: Instruction() {
        override fun execute(state: State, command: Command): State {
            val regCNumber = command.third
            val result = if (state.getValue(command.first) == state.getValue(command.second)) 1 else 0

            return state.setValue(regCNumber, result)
        }
    }

    data class Command(val first: Int, val second: Int, val third: Int) {
        companion object {
            fun from(text: String): Command {
                val jurs = text.split(" ").subList(1, 4).map { it.toInt() }
                return Command(jurs[0], jurs[1], jurs[2])
            }
        }
    }

    data class State(val a: Int, val b: Int, val c: Int, val d: Int, val e: Int, val f: Int) {
        companion object {
            fun Zero() = State(0, 0, 0, 0, 0, 0)

            fun Funkazilla() = Zero().copy(a = 1)

            fun from(list: List<Int>): State {
                return State(list[0], list[1], list[2], list[3], list[4], list[5])
            }
        }

        operator fun get(registerNumber: Int): Int {
            return getValue(registerNumber)
        }

        operator fun set(registerNumber: Int, value: Int) {
            setValue(registerNumber, value)
        }

        fun getValue(registerNumber: Int): Int {
            return when(registerNumber) {
                0 -> a
                1 -> b
                2 -> c
                3 -> d
                4 -> e
                5 -> f
                else -> throw RuntimeException()
            }
        }

        fun setValue(registerNumber: Int, value: Int): State {
            return when(registerNumber) {
                0 -> this.copy(a = value)
                1 -> this.copy(b = value)
                2 -> this.copy(c = value)
                3 -> this.copy(d = value)
                4 -> this.copy(e = value)
                5 -> this.copy(f = value)
                else -> throw RuntimeException()
            }
        }
    }
}