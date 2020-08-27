package Year2016

import org.apache.commons.codec.digest.DigestUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle14Test {

    val utils = DigestUtils("MD5")
    val puzzle = Puzzle14()

    @Test
    fun `md5 of abc18 should contain a '888'`() {
        val result = utils.digestAsHex("abc18").contains("888")
        assertEquals(result, true)
    }

    @Test
    fun `find first key`() {
        val firstKey = puzzle.findKey(1, "abc")
        assertEquals(firstKey, 39)
    }

    @Test
    fun `find 64th key`() {
        val firstKey = puzzle.findKey(64, "abc")
        assertEquals(22728, firstKey)
    }

    @Test
    fun `get triple repeat should find triple at the start of a string`() {
        val triple = puzzle.getTripleRepeat("qqqoaisjdoiasjd")
        assertEquals("qqq", triple)
    }

    @Test
    fun `get triple repeat should find triple at the end of a string`() {
        val triple = puzzle.getTripleRepeat("oaisjdoiasjdqqq")
        assertEquals("qqq", triple)
    }

    @Test
    fun `get triple repeat should find triple in the middle of a string`() {
        val triple = puzzle.getTripleRepeat("oaisjdoqqqiasjd")
        assertEquals("qqq", triple)
    }

    @Test
    fun `get triple repeat should return the first triple in a string`() {
        val triple = puzzle.getTripleRepeat("qqqbbb")
        assertEquals("qqq", triple)
    }

    @Test
    fun `puzzle part a`() {
        val firstKey = puzzle.findKey(64, "jlmsuwbz")
        assertEquals(35186, firstKey)
    }

    @Test
    fun `super secure example`() {
        val firstKey = puzzle.findSuperSecureKey(1, "abc")
        assertEquals(10, firstKey)
    }

    @Test
    fun `puzzle part b`() {
        val firstKey = puzzle.findSuperSecureKey(64, "jlmsuwbz")
        assertEquals(22429, firstKey)
    }

    @Test
    fun `key stretching example`() {
        val step1 = utils.digestAsHex("abc0")
        assertEquals("577571be4de9dcce85a041ba0410f29f", step1)

        val step2 = utils.digestAsHex("577571be4de9dcce85a041ba0410f29f")
        assertEquals("eec80a0c92dc8a0777c619d9bb51e910", step2)

        val step3 = utils.digestAsHex("eec80a0c92dc8a0777c619d9bb51e910")
        assertEquals("16062ce768787384c81fe17a7a60c7e3", step3)
    }

    @Test
    fun `abc0 hashed 2017 times should be`() {
        val result = puzzle.getSuperSecureMD5("abc0")
        assertEquals("a107ff634856bb300138cac6568c0f24", result)
    }
}

class Puzzle14 {
    val utils = DigestUtils("MD5")
    val cache = mutableMapOf<String, String>()
//    val superSecureCache = mutableMapOf<String, String>()

    fun getMD5(text: String): String {
        if (!cache.containsKey(text)) {
            cache[text] = utils.digestAsHex(text)
        }

        return cache[text]!!
    }

    fun getSuperSecureMD5(text: String): String {
        if (!cache.containsKey(text)) {
            cache[text] = text

            for (jur in 0 until 2017) {
                cache[text] = utils.digestAsHex(cache[text])
            }
        }

        return cache[text]!!
    }

    fun findKey(keyNumber: Int, salt: String): Int {
        val keys = mutableListOf<String>()

        (0 until Int.MAX_VALUE).forEach { index ->
            val md5 =  getMD5(salt + index)
            val tripleRepeat = getTripleRepeat(md5)

            if (tripleRepeat != null) {
                val quintupleRepeat = tripleRepeat + tripleRepeat[0] + tripleRepeat[0]

                // Look for a quad repeat in the next 1000 hashes
                for (jurIndex in index + 1 .. index + 1000) {
                    val jur = getMD5(salt + jurIndex)

                    if (jur.contains(quintupleRepeat)) {
                        keys.add(jur)
                        break
                    }
                }

                if (keys.size == keyNumber) {
                    return index
                }
            }
        }

        throw RuntimeException("Could not find a key!")
    }

    fun findSuperSecureKey(keyNumber: Int, salt: String): Int {
        val keys = mutableListOf<String>()

        (0 until Int.MAX_VALUE).forEach { index ->
            val md5 =  getSuperSecureMD5(salt + index)
            val tripleRepeat = getTripleRepeat(md5)

            if (tripleRepeat != null) {
                val quintupleRepeat = tripleRepeat + tripleRepeat[0] + tripleRepeat[0]

                // Look for a quad repeat in the next 1000 hashes
                for (jurIndex in index + 1 .. index + 1000) {
                    val jur = getSuperSecureMD5(salt + jurIndex)

                    if (jur.contains(quintupleRepeat)) {
                        keys.add(jur)
                        break
                    }
                }

                if (keys.size == keyNumber) {
                    return index
                }
            }
        }

        throw RuntimeException("Could not find a key!")
    }

    fun getTripleRepeat(hash: String): String? {
        if (hash.length < 3) return null

        (2 until hash.length).forEach { i ->
            val a = hash[i - 2]
            val b = hash[i - 1]
            val c = hash[i]

            if (a == b && a == c) return "$a$b$c"
        }

        return null
    }

}
