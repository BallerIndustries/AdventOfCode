package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle14Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle14.txt").readText().replace("\r", "")
    val puzzle = Puzzle14()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(14925946402938, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(3706820676200, result)
    }

    @Test
    fun `example part a`() {
        val puzzleText = "mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X\n" +
                "mem[8] = 11\n" +
                "mem[7] = 101\n" +
                "mem[8] = 0"
        val result = puzzle.solveOne(puzzleText)
        assertEquals(165, result)
    }

    @Test
    fun `example part b`() {
        //3092356790655 WRONG
        val puzzleText = "mask = 000000000000000000000000000000X1001X\n" +
                "mem[42] = 100\n" +
                "mask = 00000000000000000000000000000000X0XX\n" +
                "mem[26] = 1"
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(208, result)
    }

    @Test
    fun `range producers a` () {
        val resull = puzzle.resolveMemoryAddresses(0, "000000000000000000000000000000X1101X", listOf()).sorted()
        assertEquals(listOf(26L, 27L, 58L, 59L), resull)
    }

    @Test
    fun `range producers b` () {
        val resull = puzzle.resolveMemoryAddresses(0, "00000000000000000000000000000001X0XX", listOf()).sorted()
        assertEquals(listOf(16L, 17L, 18L, 19L, 24L, 25L, 26L, 27L), resull)
    }

    @Test
    fun `range producers c` () {
        val text = "100000000000000000000000000000000000"
        val resull = puzzle.resolveMemoryAddresses(0, text, listOf()).sorted()
        assertEquals(listOf(text.toLong(2)), resull)
    }

    @Test
    fun `range producers d` () {
        val text = "010000000000000000000000000000000000"
        val resull = puzzle.resolveMemoryAddresses(0, text, listOf()).sorted()
        assertEquals(listOf(text.toLong(2)), resull)
    }

    @Test
    fun `range producers 3` () {
        val text = "001000000000000000000000000000000000"
        val resull = puzzle.resolveMemoryAddresses(0, text, listOf()).sorted()
        assertEquals(listOf(text.toLong(2)), resull)
    }






}

class Puzzle14 {
    fun solveOne(puzzleText: String): Long {
        val memory = mutableMapOf<Int, Long>()
        var mask = ""

        puzzleText.split("\n").forEach { line ->
            if (line.startsWith("mask")) {
                mask = line.split(" = ")[1]
            }
            else if (line.startsWith("mem")) {
                val tmp = line.split(" = ")
                val address = tmp[0].replace("mem[", "").replace("]", "").toInt()
                val value = tmp[1].toLong()

                val binaryValue = value.toString(2).padStart(36, '0')
                val result = maskMeUp(binaryValue, mask)
                val numericResult = result.toLong(2)

//                println("value  = $binaryValue")
//                println("mask   = $mask")
//                println("result = $result")
//                println()

                memory[address] = numericResult
            }
        }

        return memory.values.sumByDouble { it.toDouble() }.toLong()
    }

    private fun maskMeUp(value: String, mask: String): String {
        var i = 0
        val buffer = StringBuilder()

        while (i < value.length) {
            val maskChar = mask[i]
            val valueChar = value[i]

            if (maskChar == 'X') {
                buffer.append(valueChar)
            }
            else {
                buffer.append(maskChar)
            }

            i++
        }

        return buffer.toString()
    }

    data class RangeAndValue(val start: Int, val end: Int, val value: Long)

    fun solveTwo(puzzleText: String): Long {
        var mask = ""
        val homeOnTheRange = mutableListOf<RangeAndValue>()
        val memory = mutableMapOf<Long, Long>()

        puzzleText.split("\n").forEach { line ->
            if (line.startsWith("mask")) {
                mask = line.split(" = ")[1]
            }
            else if (line.startsWith("mem")) {
                val tmp = line.split(" = ")
                val address = tmp[0].replace("mem[", "").replace("]", "").toLong().toString(2).padStart(36, '0')
                val value = tmp[1].toLong()
                val result = floatMaskMeUp(address, mask)

//                println("address = $address")
//                println("mask    = $mask")
//                println("result  = $result")
//                println()


                resolveMemoryAddresses(0, result, listOf()).forEach {
                    memory[it] = value
                }
            }
        }

        var counter = 0L

        memory.values.forEach {
            counter += it
        }

        return counter;

        // Iterate through the command, figure out what ranges need to store what
        // 26 - 27 STORE 100
        // 58 - 59 STORE 100
        // 16 - 19 STORE 1
        // 24 - 27 STORE 1

        // Collapse ranges into exclusive ranges
        // 24 - 27 STORE 1
        // 16 - 19 STORE 1
        // 58 - 59 STORE 100




    }

    private fun floatMaskMeUp(address: String, mask: String): String {
        var i = 0
        val buffer = StringBuilder()

        while (i < address.length) {
            val maskChar = mask[i]
            val addressChar = address[i]

            when (maskChar) {
                '0' -> buffer.append(addressChar)
                '1' -> buffer.append('1')
                'X' -> buffer.append('X')
                else -> throw RuntimeException()
            }

            i++
        }

        return buffer.toString()
    }

    //00000000000000000000000000000001X0XX
    fun resolveMemoryAddresses(counter: Long, address: String, result: List<Long>): List<Long> {
        var index = address.lastIndex
        var vCounter = counter

        while (index >= 0) {
            val char = address[index]
            val powerOfTwo = 35 - index
            //println("powerOfTwo = $powerOfTwo vCounter = $vCounter")

            if (char == 'X') {
                val oneCase = Math.pow(2.0, powerOfTwo.toDouble()).toLong()
                val remainder = address.substring(0, index)
                return resolveMemoryAddresses(vCounter, remainder, result) +
                        resolveMemoryAddresses(vCounter + oneCase, remainder, result)
            }
            else if (char == '1') {
                val increment = Math.pow(2.0, powerOfTwo.toDouble()).toLong()
//                println("increment = $increment")
                vCounter += increment
            }

            index--
        }

        return result + vCounter
    }

    fun produceRanges(address: String, value: Long): List<RangeAndValue> {
        val constant = address.replace("X", "0").toInt(2)
        var previousChar = address[0]
        var inXBlock = previousChar == 'X'
        var xStart = if (inXBlock) 0 else -1
        var index = 1

        val ranges = mutableListOf<RangeAndValue>()

        //00000000000000000000000000000001X0XX
        while (index < address.length) {
            val char = address[index]

            if (inXBlock && char != 'X') {
                if (xStart == -1) {
                    throw RuntimeException()
                }

                ranges.add(RangeAndValue(xStart, index, value))

                xStart = -1
                inXBlock = false
            }
            else if (!inXBlock && char == 'X') {
                xStart = index
                inXBlock = true
            }



            index++
            previousChar = char
        }

        if (inXBlock) {
            if (xStart == -1) {
                throw RuntimeException()
            }

            ranges.add(RangeAndValue(xStart, address.length, value))
        }


        return ranges.map { range ->
            val start = constant + (Math.pow(2.0, 36 - range.start.toDouble())).toInt()
            val end = constant + (Math.pow(2.0, 36 - range.end.toDouble())).toInt()
            RangeAndValue(start, end, range.value)

        }
    }
}

