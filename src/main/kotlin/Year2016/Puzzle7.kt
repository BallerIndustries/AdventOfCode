package Year2016

import java.util.regex.Pattern

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