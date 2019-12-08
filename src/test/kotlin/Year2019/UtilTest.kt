package Year2019

import junit.framework.Assert.assertEquals
import org.junit.Test

class UtilTest {
    @Test
    fun `should be able to get all permutations of a, b, c`() {
        val permutations = listOf('a', 'b', 'c').permutations().toSet()
        val expected = setOf(
            listOf('a', 'b', 'c'),
            listOf('b', 'a', 'c'),
            listOf('c', 'a', 'a')
        )

        assertEquals(expected, permutations)
    }
}