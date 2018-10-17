package Year2016

class Puzzle6 {
    fun errorCorrect(input: String): String {
        val lines = input.split("\n")
        val firstLine = lines[0]

        if (!lines.all { it.length == firstLine.length }) {
            throw RuntimeException("Yo! all lines should be of the same length!")
        }

        val transposedLines = (0 until firstLine.length).map { index ->
            lines.joinToString("") { it[index].toString() }
        }

        val dog = transposedLines.map { line ->
            val horse = line.groupBy { it }
                    .map { it.key to it.value.size }
                    .sortedByDescending { it.second }

            horse.first().first
        }.joinToString("")

        return dog
    }

    fun errorCorrectTwo(input: String): String {
        val lines = input.split("\n")
        val firstLine = lines[0]

        if (!lines.all { it.length == firstLine.length }) {
            throw RuntimeException("Yo! all lines should be of the same length!")
        }

        val transposedLines = (0 until firstLine.length).map { index ->
            lines.joinToString("") { it[index].toString() }
        }

        val dog = transposedLines.map { line ->
            val horse = line.groupBy { it }
                    .map { it.key to it.value.size }
                    .sortedBy { it.second }

            horse.first().first
        }.joinToString("")

        return dog
    }

}