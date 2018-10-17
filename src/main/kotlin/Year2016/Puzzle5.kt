package Year2016

import org.apache.commons.codec.digest.DigestUtils

class Puzzle5 {
    val utils = DigestUtils("MD5")

    fun getDoorCode(doorId: String): String {
        var buffer = ""

        (1..Int.MAX_VALUE).forEach { number ->
            val hash = utils.digestAsHex(doorId + number)

            if (hash.startsWith("00000")) {
                buffer += hash[5]

                if (buffer.length >= 8) {
                    return buffer
                }
            }
        }

        throw RuntimeException("Tried all the Ints, still couldn't find hashes. wtf!?!")
    }

    fun getPositionalIndexDoorCode(doorId: String): String {
        val doorCodeBuffer = arrayOf<Char?>(null, null, null, null, null, null, null, null)

        (1..Int.MAX_VALUE).forEach { number ->
            val hash = utils.digestAsHex(doorId + number)

            if (hash.startsWith("00000")) {
                val index = hash[5]
                val value = hash[6]

                if (index in '0'..'7' && doorCodeBuffer[index.toString().toInt()] == null) {
                    doorCodeBuffer[index.toString().toInt()] = value

                    if (doorCodeBuffer.all { it != null }) {
                        return doorCodeBuffer.joinToString("")
                    }
                }
            }
        }

        throw RuntimeException("Tried all the Ints, still couldn't find hashes. wtf!?!")
    }
}