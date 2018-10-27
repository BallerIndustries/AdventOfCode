package Year2016

import junit.framework.Assert.assertEquals
import org.apache.commons.codec.digest.DigestUtils
import org.junit.Test

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
        puzzle.findKey(1, "abc")
    }
}

class Puzzle14 {
    fun findKey(index: Int, salt: String): String {
        var jur = 0



    }

}
