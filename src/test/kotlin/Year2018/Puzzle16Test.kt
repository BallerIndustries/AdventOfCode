package Year2018

import junit.framework.Assert
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.lang.RuntimeException

class Puzzle16Test {
    val puzzleText = this::class.java.getResource(
            "/2018/puzzle16.txt").readText()
    val puzzle = Puzzle16()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(493, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("b", result)
    }

    class Puzzle16 {
        val allInstructions = listOf(AddR(), AddI(), MulR(), MulI(), BAndR(), BAndI(), BOrR(), BOrI(),
            SetR(), SetI(), GTIR(), GTRI(), GTRR(), EQIR(), EQRI(), EQRR())

        fun solveOne(puzzleText: String): Int {
            val states = parseStates(puzzleText)

            return states.count { (before, command, after) ->
                behavesLineThreeOrMoreOpCodes(before, command, after)
            }
        }

        fun parseStates(text: String): List<Triple<String, String, String>> {
            val lines = text.split("\n")
            var index = 0

            fun nextLine(): String {
                return lines[index++]
            }

            val returnList = mutableListOf<Triple<String, String, String>>()

            while (index < lines.size) {
                val before = nextLine()
                val command = nextLine()
                val after = nextLine()
                val junkLine = nextLine()

                returnList.add(Triple(before, command, after))
            }

            return returnList
        }

        private fun behavesLineThreeOrMoreOpCodes(before: String, command: String, after: String): Boolean {
            val beforeInts = before.replace("Before: ", "")
                .removeSurrounding("[", "]")
                .split(", ")
                .map { it.toInt() }

            val afterInts = after.replace("After:  ", "")
                .removeSurrounding("[", "]")
                .split(", ")
                .map { it.toInt() }

            val commandObject = Command.from(command.split(" ").map { it.toInt() })
            val beforeState = State.from(beforeInts)
            val afterState = State.from(afterInts)

            val jurs = allInstructions
                .map { instruction -> instruction to instruction.execute(beforeState, commandObject) }
                .filter { it.second == afterState }

//            if (jurs.count() == 1) {
//                println("hto dog!")
//            }

            return jurs.count() >= 3
        }

        fun solveTwo(puzzleText: String): String {
            return ""
        }

        abstract class Instruction {
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

        data class Command(val opcode: Int, val first: Int, val second: Int, val third: Int) {
            companion object {
                fun from(list: List<Int>): Command {
                    return Command(list[0], list[1], list[2], list[3])
                }
            }
        }

        data class State(val a: Int, val b: Int, val c: Int, val d: Int) {
            companion object {
                fun from(list: List<Int>): State {
                    return State(list[0], list[1], list[2], list[3])
                }
            }

            fun getValue(registerNumber: Int): Int {
                return when(registerNumber) {
                    0 -> a
                    1 -> b
                    2 -> c
                    3 -> d
                    else -> throw RuntimeException()
                }
            }

            fun setValue(registerNumber: Int, value: Int): State {
                return when(registerNumber) {
                    0 -> this.copy(a = value)
                    1 -> this.copy(b = value)
                    2 -> this.copy(c = value)
                    3 -> this.copy(d = value)
                    else -> throw RuntimeException()
                }
            }
        }
    }
}