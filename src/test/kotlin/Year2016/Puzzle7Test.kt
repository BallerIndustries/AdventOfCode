package Year2016

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

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
        assertEquals(count, 110)
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
        val input = Puzzle1Test::class.java.getResource("/2016/puzzle7.txt").readText().replace("\r\n", "\n")
        val count = puzzle.countSSLIpAddresses(input)
        assertEquals(count, 242)
    }
}

