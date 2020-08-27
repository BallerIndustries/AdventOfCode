package Year2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UtilTest {
    @Test
    fun `should be able to get all permutations of a, b, c`() {
        val permutations = listOf('a', 'b', 'c').permutations().toSet()
        val expected = setOf(
            listOf('a', 'b', 'c'),
            listOf('a', 'c', 'b'),
            listOf('b', 'a', 'c'),
            listOf('b', 'c', 'a'),
            listOf('c', 'a', 'b'),
            listOf('c', 'b', 'a')
        )
        assertEquals(expected, permutations)
    }

    @Test
    fun `should be able to get all permutations for 1, 2`() {
        val permutations = listOf(1, 2).permutations().toSet()
        val expected = setOf(
            listOf(1, 2),
            listOf(2, 1)
        )

        assertEquals(expected, permutations)
    }

    @Test
    fun `should be able to get permutations of an empty list`() {
        val permutations = listOf<Double>().permutations().toSet()
        val expected = setOf<Double>()
        assertEquals(expected, permutations)
    }
}