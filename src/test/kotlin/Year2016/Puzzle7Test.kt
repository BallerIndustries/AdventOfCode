package Year2016

import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import java.util.regex.Pattern

class Puzzle7Test {

    val puzzle = Puzzle7()

    @Test
    fun `abba outside brackets, supports TLS` () {
        val result = puzzle.supportsTLS("abba[mnop]qrst")
        assertTrue(result)
    }

    @Test
    fun `bddb inside brackets, does not support TLS`() {
        val result = puzzle.supportsTLS("abcd[bddb]xyyx")
        assertFalse(result)
    }

    @Test
    fun `interior characters must be different, does not support TLS`() {
        val result = puzzle.supportsTLS("aaaa[qwer]tyui")
        assertFalse(result)
    }

    @Test
    fun `oxxo outside square brackets, supports TLS`() {
        val result = puzzle.supportsTLS("ioxxoj[asdfgh]zxcvbn")
        assertTrue(result)
    }

    @Test
    fun `can pull out bracketed text into an array of strings`() {
        val input = "ipwwgkvjagdlqkoxlat[widqrotdnywnnbdn]rtviotwkbdqpggscdt[jzbcukafvquuxiu]ctmziuyofwucvdvjom"
        val result = puzzle.getBracketedText(input)
        assertEquals(result, listOf("widqrotdnywnnbdn", "jzbcukafvquuxiu"))
    }

    @Test
    fun `can pull out unbracketed text into an array of strings`() {
        val input = "ipwwgkvjagdlqkoxlat[widqrotdnywnnbdn]rtviotwkbdqpggscdt[jzbcukafvquuxiu]ctmziuyofwucvdvjom"
        val result = puzzle.getUnbracketedText(input)
        assertEquals(result, listOf("ipwwgkvjagdlqkoxlat", "rtviotwkbdqpggscdt", "ctmziuyofwucvdvjom"))
    }

    @Test
    fun `puzzle part a`() {
        val input = Puzzle1Test::class.java.getResource("/2016/puzzle7.txt").readText()
        val count = puzzle.countTLSIpAddresses(input)
        Assert.assertEquals(count, 0)
    }
}

class Puzzle7 {
    fun supportsTLS(ipAddress: String): Boolean {
        val bracketTextList = getBracketedText(ipAddress)
        val nonBracketedTextList = getUnbracketedText(ipAddress)

        if (bracketTextList.any(this::textHasAbbaPattern)) return false
        if (nonBracketedTextList.any(this::textHasAbbaPattern)) return true

        return false
    }

    private fun textHasAbbaPattern(text: String): Boolean {
        text.forEachIndexed { index, _ ->
            if (index + 4 < text.length - 1 && hasAbbaPattern(text.substring(index, index + 4))) return true
        }

        return false
    }

    private fun hasAbbaPattern(fourCharacterString: String): Boolean {
        if (fourCharacterString.length != 4) throw RuntimeException("Angry, was expecting a four character string. fourCharacterString.length = ${fourCharacterString.length}")
        return fourCharacterString[0] == fourCharacterString[3] && fourCharacterString[1] == fourCharacterString[2] && !fourCharacterString.all { it == fourCharacterString[0] }
    }

    fun countTLSIpAddresses(input: String): Int {
        return input.split('\n').filter(this::supportsTLS).count()
    }

    fun getUnbracketedText(input: String): List<String> {
        return input.split(Regex("\\[.+?]"))
    }

    fun getBracketedText(input: String): List<String> {
        val pattern = Pattern.compile("\\[.+?]")
        val matcher = pattern.matcher(input)
        val results = mutableListOf<String>()

        while (matcher.find()) {
            results.add(matcher.group(0).removeSurrounding("[", "]"))
        }

        return results
    }
}
