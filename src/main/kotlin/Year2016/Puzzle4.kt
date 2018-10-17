package Year2016

class Puzzle4 {
    fun isRealRoom(input: String): Boolean {
        val horse = input.split('-')
        val sectorIdAndChecksum = horse.last()

        val openBracketIndex = sectorIdAndChecksum.indexOf('[')
        if (openBracketIndex == -1) throw RuntimeException("What kind of crazy room name does not have a [ in it?")

        val checkSum = sectorIdAndChecksum.substring(openBracketIndex + 1, sectorIdAndChecksum.length - 1)
        val withoutEnd = horse.subList(0, horse.size - 1).joinToString("")

        val calculatedChecksum = withoutEnd.groupBy { it }
            .map { it.key to it.value.size }
            .sortedWith(compareBy({it.second}, {-it.first.toInt()}))
            .reversed()
            .subList(0, 5)
            .joinToString(separator = "") { it.first.toString() }

        return calculatedChecksum == checkSum
    }

    private fun getSectorId(roomName: String): Int {
        val last = roomName.split('-').last()
        return last.substring(0, last.indexOf('[')).toInt()
    }

    fun sumRealRoomNumbers(input: String): Int {
        var total = 0

        input.split('\n').forEach { roomName ->
            if (isRealRoom(roomName)) {
                total += getSectorId(roomName)
            }
        }

        return total
    }

    fun decrypt(roomName: String): String {
        val sectorId = getSectorId(roomName)
        val shiftAmount = sectorId % 26

        val tmp = roomName.split('-')
        val roomNameWithoutSector = tmp.subList(0, tmp.size - 1).joinToString("-")


        return roomNameWithoutSector.map { letter ->
            if (letter == '-') ' '
            else {
                shiftChar(letter, shiftAmount)
            }
        }.joinToString("") + "-$sectorId"
    }

    fun decryptRealRoomNames(input: String): List<String> {
        return input.split('\n')
                .filter { roomName -> isRealRoom(roomName) }
                .map { roomName -> decrypt(roomName) }
    }

    fun shiftChar(letter: Char, amount: Int): Char {
        val horseAmount = amount % 26
        val newCode = letter.toInt() + horseAmount

        if (newCode >= 97 && newCode <= 122) {
            return newCode.toChar()
        }
        else {
            val overrun = newCode - 122
            return (overrun + 96).toChar()
        }
    }
}