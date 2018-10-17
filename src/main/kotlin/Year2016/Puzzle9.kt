package Year2016

import java.util.regex.Pattern


data class Marker(val repeatingText: String, val count: Int) {
    override fun toString(): String {
        return (0 until count).joinToString("") { repeatingText }
    }
}

class Puzzle9 {
    fun decompress(compressedText: String): String {
        return split(compressedText).joinToString("") { it.toString() }
    }

    fun split(text: String): List<Any> {
        val returnList = mutableListOf<Any>()
        var (jur, remainingText) = pullOutChunks(text)

        returnList.addAll(jur)

        while (remainingText.isNotEmpty()) {
            val tmp = pullOutChunks(remainingText)
            jur = tmp.first
            remainingText = tmp.second
            returnList.addAll(jur)
        }

        return returnList
    }

    private fun pullOutChunks(text: String): Pair<List<Any>, String> {
        val pattern = Pattern.compile("\\(\\d+x\\d+\\)")
        val matcher = pattern.matcher(text)

        if (!matcher.find()) return listOf(text) to ""

        val startIndex = matcher.start()
        val endIndex = matcher.end()
        val matchedText = text.substring(startIndex, endIndex)
        val (repeatIndex, repeatCount) = matchedText
                .removeSurrounding("(", ")")
                .split('x')
                .map { it.toInt() }

        val endOfMarkerIndex = endIndex + repeatIndex
        val marker = Marker(text.substring(endIndex, endOfMarkerIndex), repeatCount)
        val remainingText = text.substring(endOfMarkerIndex, text.length)

        return if (startIndex > 0) listOf(text.substring(0, startIndex), marker) to remainingText else listOf(marker) to remainingText
    }

    fun jur(compressedText: String, weightings: List<Long>): Long {
        val mutableWeightings = weightings.toMutableList()

        Regex("\\(\\d+x\\d+\\)")
            .findAll(compressedText)
            .map { matchResult: MatchResult -> matchResult.range }
            .forEach { intRange ->
                intRange.forEach { index -> mutableWeightings[index] = 0 }
            }

        return mutableWeightings.sum()
    }


    fun calculateWeightings(compressedText: String): List<Long> {
        val weightings = compressedText.map { 1L }.toMutableList()

        // Set weightings
        val pattern = Pattern.compile("\\(\\d+x\\d+\\)")
        val matcher = pattern.matcher(compressedText)

        while (matcher.find()) {
            val startIndex = matcher.start()
            val endIndex = matcher.end()
            val matchedText = compressedText.substring(startIndex, endIndex)
            val (repeatIndex, repeatCount) = matchedText
                    .removeSurrounding("(", ")")
                    .split('x')
                    .map { it.toInt() }

            val endOfMarkerIndex = endIndex + repeatIndex
            val repeatText = compressedText.substring(endIndex, endOfMarkerIndex)

            // Multiply the weightings
            (endIndex until endOfMarkerIndex).forEach { index -> weightings[index] = weightings[index] * repeatCount }
        }

        return weightings
    }

    fun decompressVersion2(compressed: String): Long {
        val weightings = calculateWeightings(compressed)
        val asdas = jur(compressed, weightings)
        return asdas
    }
}