package Year2018

import junit.framework.TestCase.assertEquals
import org.junit.Assert
import org.junit.Test
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream

class Puzzle1Test {
    val puzzleText = Puzzle1Test::class.java.getResource("/2018/puzzle1.txt").readText().replace("\r", "\n")
    val puzzle = Puzzle1()

    @Test
    fun `puzzle part a`() {
        val horse = puzzle.solveOne(puzzleText)
        assertEquals(454, horse)
    }

    @Test
    fun `puzzle part b`() {
        val horse = puzzle.solveTwo(puzzleText)
        assertEquals(566, horse)
    }

    @Test
    fun `asdij asdij asdij`() {
        val inputText = "+3\n+3\n+4\n-2\n-4"
        val dog = puzzle.solveTwo(inputText)
        assertEquals(dog, 10)
    }

    @Test
    fun `can count lines in a file`() {
        val uri = this::class.java.getResource("/2018/puzzle1.txt").toURI()
        val file = File(uri)
        val result = puzzle.countLines(file)
        assertEquals(957, result)
    }

    @Test
    fun `can count words in a file`() {
        val uri = this::class.java.getResource("/2018/puzzle4.txt").toURI()
        val file = File(uri)
        val result = puzzle.countWords(file)
        assertEquals(3668, result)
    }

    @Test
    fun `can count chars in a file`() {
        val uri = this::class.java.getResource("/2018/puzzle4.txt").toURI()
        val file = File(uri)
        val result = puzzle.countChars(file)
        assertEquals(33899, result)
    }
}

class Puzzle1 {
    fun solveOne(puzzleText: String): Int {
        return puzzleText.split("\n").sumBy(this::parseNumber)
    }

    fun solveTwo(horse: String): Int {
        var currentFreq = 0
        val seenBefore = mutableSetOf(currentFreq)
        val deltas = horse.split("\n").map { line -> parseNumber(line) }

        while (true) {
            deltas.forEach { delta ->
                val nextFreq = currentFreq + delta
                if (!seenBefore.add(nextFreq)) return nextFreq

                currentFreq = nextFreq
            }
        }
    }

    private fun parseNumber(line: String): Int {
        val number = line.substring(1).toInt()
        return if (line[0] == '-') number * -1 else number
    }

    fun countLines(file: File): Long {
        file.inputStream().bufferedReader().use { stream: BufferedReader ->
            return stream.lines().count()
        }
    }

    fun countWords(file: File): Long {
        file.inputStream().bufferedReader().use { stream ->
            return stream.lineSequence().sumBy { line -> line.count { char -> char == ' '} }.toLong()
        }
    }

    fun countChars(file: File): Int {
        file.inputStream().bufferedReader().use { stream ->
            return stream.lineSequence().sumBy { line -> line.length }
        }
    }
}

