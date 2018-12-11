package Year2015

import generateGrid
import jdk.nashorn.internal.ir.Assignment
import junit.framework.Assert.*
import org.junit.Test
import sun.reflect.generics.reflectiveObjects.NotImplementedException

class Puzzle7Test {
    val puzzle = Puzzle7()
    val puzzleText = this::class.java.getResource(
            "/2015/puzzle7.txt").readText().replace("\r", "")

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(579999, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(17837115, result)
    }
}

class Puzzle7 {
    class AndInstruction(val left: String, val right: String, val destination: String) : Instruction()
    class NotInstruction(val source: String, val destination: String) : Instruction()
    class RightShift(val source: String, val destination: String, val amount: Int) : Instruction()
    class LeftShift(val source: String, val destination: String, val amount: Int) : Instruction()
    class OrInstruction(val left: String, val right: String, val destination: String) : Instruction()

    class AssignmentInstructino(val source: String, val destination: String) : Instruction() {
        override fun canExecute(state: Map<String, Int>): Boolean {
            return source.toIntOrNull() != null || state.containsKey(source)
        }

        override fun execute(state: Map<String, Int>): Map<String, Int> {
            val copy = state.toMutableMap()

            if (!canExecute(state)) {
                copy[destination] = getValue(state, source)
            }

            return copy
        }
    }

    abstract class Instruction {
        open fun canExecute(state: Map<String, Int>): Boolean {
            return false
        }

        open fun execute(state: Map<String, Int>): Map<String, Int> {
            throw NotImplementedException()
        }

        fun getValue(state: Map<String, Int>, registerNameOrValue: String): Int {
            if (registerNameOrValue.toIntOrNull() != null) return registerNameOrValue.toInt() else return state[registerNameOrValue]!!
        }
    }



    fun getFromState(state: Map<String, Int>, registerNameOrValue: String): Int? {
        val horse = registerNameOrValue.toIntOrNull()
        return if (horse != null) horse else state[registerNameOrValue]
    }

    fun solveOne(puzzleText: String): Int {
        val state = mutableMapOf<String, Int>()

        val instructions = puzzleText.split("\n").map { line ->
            val pieces = line.split(" ")

            if (line.contains("AND")) {
                AndInstruction(pieces[0], pieces[2], pieces[4])
            }
            else if (line.contains("OR")) {
                OrInstruction(pieces[0], pieces[2], pieces[4])
            }
            else if (line.contains("NOT")) {
                NotInstruction(pieces[1], pieces[3])
            }
            else if (line.contains("LSHIFT")) {
                LeftShift(pieces[0], pieces[4], pieces[2].toInt())
            }
            else if (line.contains("RSHIFT")) {
                RightShift(pieces[0], pieces[4], pieces[2].toInt())
            }
            else if (line.contains("->")) {
                AssignmentInstructino(pieces[0], pieces[2])
            }
            else {
                throw RuntimeException()
            }
        }

        val executableInstructions = instructions.count { it.canExecute(state) }

//        instructions.forEach { instruction -> }

        println(executableInstructions)




        return 0
    }

    fun solveTwo(puzzleText: String): Int {
       return 0
    }
}