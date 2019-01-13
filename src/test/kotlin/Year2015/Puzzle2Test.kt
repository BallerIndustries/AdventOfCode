package Year2015

//import junit.framework.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class Puzzle2Test {
    val puzzle = Puzzle2()
    val input = this::class.java.getResource("/2015/puzzle2.txt").readText().replace("\r", "").split("\n")

    @Test
    fun `58 sqrft of paper for a 2x3x4 box`() {
        val area = puzzle.getArea(2, 3, 4)
        assertEquals(58, area)
    }

    @Test
    fun `42 sqrft of paper for a 1x1x10 box`() {
        val area = puzzle.getArea(1, 1, 10)
        assertEquals(43, area)
    }

    @Test
    fun `puzzle part a`() {
        val areas = puzzle.getAllBoxAreas(input)
        assertEquals(1606483, areas)
    }

    @Test
    fun `34 feet of ribbon needed for a 2x3x4 box`() {
        val ribbonLength = puzzle.getRibbonLength(2, 3, 4)
        assertEquals(34, ribbonLength)
    }

    @Test
    fun `puzzle part b`() {
        val lengths = puzzle.getAllRibbonLengths(input)
        assertEquals(3842356, lengths)
    }
}

class Puzzle2 {
    fun getAllBoxAreas(lines: List<String>): Int {
        return lines.sumBy { line ->
            val tmp = line.split("x").map { it.toInt() }
            getArea(tmp[0], tmp[1], tmp[2])
        }
    }



    fun getArea(w: Int, l: Int, h: Int): Int {
        val sideA = 2*l*w
        val sideB = 2*w*h
        val sideC = 2*h*l
        return  sideA + sideB + sideC + listOf(l*w, w*h, h*l).min()!!
    }

    fun getRibbonLength(w: Int, l: Int, h: Int): Int {
        val horses = listOf(w, l, h).sorted().map { it + it }
        return horses[0] + horses[1] + (w * l * h)
    }

    fun getAllRibbonLengths(lines: List<String>): Int {
        return lines.sumBy { line ->
            val tmp = line.split("x").map { it.toInt() }
            getRibbonLength(tmp[0], tmp[1], tmp[2])
        }
    }
}
