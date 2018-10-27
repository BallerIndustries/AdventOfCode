package Year2016

class Puzzle12 {
    fun parseCommandText(commandList: String): List<Command> {
        return commandList.split("\n").map { commandText ->
            val tmp = commandText.split(" ")

            when (tmp[0]) {
                "cpy" -> Copy(tmp[1], tmp[2])
                "inc" -> IncrementRegister(tmp[1])
                "dec" -> DecrementRegister(tmp[1])
                "jnz" -> JumpIfNotZero(tmp[1], tmp[2])
                else -> throw RuntimeException("Sorry don't support that command. command = ${tmp[0]}")
            }
        }
    }

    fun runCommands(commandText: String, initialState: State = State()): State {
        var state = initialState
        val commands = parseCommandText(commandText)

        while (state.programCounter < commands.size) {
            val command = commands[state.programCounter]
            state = command.execute(state)
        }

        return state
    }

    interface Command {
        fun execute(state: State): State
    }

    data class Copy(val valueOrRegister: String, val destinationRegister: String) : Command {
        override fun execute(state: State): State {
            val newProgramCounter = state.programCounter + 1
            val mutableRegisters = state.registers + mapOf(destinationRegister to state.getValueOrValueFromRegister(valueOrRegister))
            return State(newProgramCounter, mutableRegisters)
        }
    }

    data class IncrementRegister(val sourceRegister: String) : Command {
        override fun execute(state: State): State {
            val newProgramCounter = state.programCounter + 1
            val newRegisters = state.registers + mapOf(sourceRegister to state.getValueOrValueFromRegister(sourceRegister) + 1)
            return State(newProgramCounter, newRegisters)
        }
    }

    data class DecrementRegister(val sourceRegister: String) : Command {
        override fun execute(state: State): State {
            val newProgramCounter = state.programCounter + 1
            val newRegisters = state.registers + mapOf(sourceRegister to state.getValueOrValueFromRegister(sourceRegister) - 1)
            return State(newProgramCounter, newRegisters)
        }
    }

    data class JumpIfNotZero(val conditionalValueOrRegister: String, val deltaValueOrRegister: String) : Command {
        override fun execute(state: State): State {
            if (state.getValueOrValueFromRegister(conditionalValueOrRegister) == 0) {
                return State(state.programCounter + 1, state.registers)
            }
            else {
                val deltaValue = state.getValueOrValueFromRegister(deltaValueOrRegister)
                return State(state.programCounter + deltaValue, state.registers)
            }
        }
    }

    data class State(val programCounter: Int = 0, val registers: Map<String, Int> = initialRegisters) {
        companion object {
            val initialRegisters = mapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0)
        }

        fun getValueOrValueFromRegister(valueOrRegister: String): Int {
            return if (valueOrRegister.toIntOrNull() != null) valueOrRegister.toInt() else registers[valueOrRegister]!!
        }
    }
}