package Year2016

import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import java.util.regex.Pattern

class Puzzle7Test {

    val puzzle = Puzzle7()

    @Test
    fun `abba outside brackets, supports TLS`() {
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
    fun `ioxxoj should contain an abba pattern`() {
        val result = puzzle.textHasAbbaPattern("ioxxoj")
        assertTrue(result)
    }

    @Test
    fun `abba should contain an abba pattern`() {
        val result = puzzle.textHasAbbaPattern("abba")
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
        Assert.assertEquals(count, 110)
    }

    @Test
    fun `aba{bab}xyz supports SSL`() {
        assertTrue(puzzle.supportsSSL("aba[bab]xyz"))
    }

    @Test
    fun `xyx{xyx}xyx does not support SSL`() {
        assertFalse(puzzle.supportsSSL("xyx[xyx]xyx"))
    }

    @Test
    fun `aaa{kek}eke supports SSL`() {
        assertTrue(puzzle.supportsSSL("aaa[kek]eke"))
    }

    @Test
    fun `zazbz{bzb}cdb supports SSL`() {
        assertTrue(puzzle.supportsSSL("zazbz[bzb]cdb"))
    }

    @Test
    fun `dsfoisjdfi j`() {
        puzzle.supportsSSL("txxplravpgztjqcw[txgmmtlhmqpmmwp]bmhfgpmafxqwtrpr[inntmjmgqothdzfqgxq]cvtwvembpvdmcvk")
    }

    @Test
    fun `puzzle part b`() {
        val input = Puzzle1Test::class.java.getResource("/2016/puzzle7.txt").readText()
        val count = puzzle.countSSLIpAddresses(input)
        Assert.assertEquals(count, 242)
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

    fun supportsSSL(ipAddress: String): Boolean {
        val bracketedTextList = getBracketedText(ipAddress)
        val nonBracketedTextList = getUnbracketedText(ipAddress)

        val bracketedAbaPatterns = bracketedTextList
                .flatMap { getSubstrings(it, 3) }
                .filter(this::hasAbaPattern)
                .toSet()

        val nonBracketedAbaPatterns = nonBracketedTextList
                .flatMap { getSubstrings(it, 3) }
                .filter(this::hasAbaPattern)
                .map { "${it[1]}${it[0]}${it[1]}" }
                .toSet()

        return bracketedAbaPatterns.any { nonBracketedAbaPatterns.contains(it) }
    }

    fun textHasAbbaPattern(text: String): Boolean {
        return getSubstrings(text, 4).any { hasAbbaPattern(it) }
    }

    private fun getSubstrings(text: String, size: Int): List<String> {
        return text.mapIndexed { index, _ -> text.substring(index, Math.min(index + size, text.length)) }
                .filter { it.length == size }
    }

    private fun hasAbaPattern(threeCharacterString: String): Boolean {
        if (threeCharacterString.length != 3) throw RuntimeException("Angry, was expecting a three character string. threeCharacterString.length = ${threeCharacterString.length}")
        return threeCharacterString[0] == threeCharacterString[2] && !threeCharacterString.all { it == threeCharacterString[0] }
    }

    private fun hasAbbaPattern(fourCharacterString: String): Boolean {
        if (fourCharacterString.length != 4) throw RuntimeException("Angry, was expecting a four character string. fourCharacterString.length = ${fourCharacterString.length}")
        return fourCharacterString[0] == fourCharacterString[3] && fourCharacterString[1] == fourCharacterString[2] && !fourCharacterString.all { it == fourCharacterString[0] }
    }

    fun countTLSIpAddresses(input: String): Int {
        return input.split('\n').filter(this::supportsTLS).count()
    }

    fun countSSLIpAddresses(input: String): Int {
        return input.split("\r\n").filter(this::supportsSSL).count()
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
