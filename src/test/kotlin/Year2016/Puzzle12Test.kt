package Year2016

import Year2016.Puzzle12.State
import org.junit.Assert.*
import org.junit.Test



class Puzzle12Test {

    val puzzle = Puzzle12()

    @Test
    fun `can parse copy value command`() {
        val commands = puzzle.parseCommandText("cpy 41 a")
        assertEquals(commands, listOf(Puzzle12.Copy("41", "a")))
    }

    @Test
    fun `can parse copy register command`() {
        val commands = puzzle.parseCommandText("cpy b a")
        assertEquals(commands, listOf(Puzzle12.Copy("b", "a")))
    }

    @Test
    fun `can parse increment command`() {
        val commands = puzzle.parseCommandText("inc a")
        assertEquals(commands, listOf(Puzzle12.IncrementRegister("a")))
    }

    @Test
    fun `can parse decrement command`() {
        val commands = puzzle.parseCommandText("dec a")
        assertEquals(commands, listOf(Puzzle12.DecrementRegister("a")))
    }

    @Test
    fun `can parse jump command`() {
        val commands = puzzle.parseCommandText("jnz a 2")
        assertEquals(commands, listOf(Puzzle12.JumpIfNotZero("a", "2")))
    }

    @Test
    fun `state after a copy value command`() {
        val initialState = State()
        val currentState = Puzzle12.Copy("12", "a").execute(initialState)
        assertEquals(currentState, State(1, mapOf("a" to 12, "b" to 0, "c" to 0, "d" to 0)))
    }

    @Test
    fun `state after a copy register command`() {
        val initialState = State()
        val state1 = Puzzle12.Copy("12", "a").execute(initialState)
        val state2 = Puzzle12.Copy("a", "b").execute(state1)
        assertEquals(state2, State(2, mapOf("a" to 12, "b" to 12, "c" to 0, "d" to 0)))
    }

    @Test
    fun `state after an increment register command`() {
        val initialState = State()
        val state1 = Puzzle12.IncrementRegister("a").execute(initialState)
        assertEquals(state1, State(1, mapOf("a" to 1, "b" to 0, "c" to 0, "d" to 0)))
    }

    @Test
    fun `state after an decrement register command`() {
        val initialState = State()
        val state1 = Puzzle12.DecrementRegister("a").execute(initialState)
        assertEquals(state1, State(1, mapOf("a" to -1, "b" to 0, "c" to 0, "d" to 0)))
    }

    @Test
    fun `state after jump command, when register is zero`() {
        val initialState = State()
        val state1 = Puzzle12.JumpIfNotZero("a", "7").execute(initialState)
        assertEquals(state1, State(1, mapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0)))
    }

    @Test
    fun `state after jump command, when register is not zero`() {
        val initialState = State()
        val stateOne = Puzzle12.IncrementRegister("a").execute(initialState)
        val stateTwo = Puzzle12.JumpIfNotZero("a", "7").execute(stateOne)
        assertEquals(stateTwo, State(8, mapOf("a" to 1, "b" to 0, "c" to 0, "d" to 0)))
    }

    @Test
    fun `example input`() {
        val input = "cpy 41 a\n" +
                "inc a\n" +
                "inc a\n" +
                "dec a\n" +
                "jnz a 2\n" +
                "dec a"

        val finalState = puzzle.runCommands(input)
        assertEquals(finalState, State(6, mapOf("a" to 42, "b" to 0, "c" to 0, "d" to 0)))
    }

    @Test
    fun `puzzle part a`() {
        val input = Puzzle12::class.java.getResource("/2016/puzzle12.txt").readText().replace("\r", "")
        val finalState = puzzle.runCommands(input)
        assertEquals(finalState, State(23, mapOf("a" to 318077, "b" to 196418, "c" to 0, "d" to 0)))
    }

    @Test
    fun `puzzle part b`() {
        val input = Puzzle12::class.java.getResource("/2016/puzzle12.txt").readText().replace("\r", "")
        val initialState = State(0, mapOf("a" to 0, "b" to 0, "c" to 1, "d" to 0))
        val finalState = puzzle.runCommands(input, initialState)
        assertEquals(finalState, State(23, mapOf("a" to 9227731, "b" to 5702887, "c" to 0, "d" to 0)))
    }
}

