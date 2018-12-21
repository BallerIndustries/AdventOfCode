//package Year2015
//
//import junit.framework.Assert.assertEquals
//import org.junit.Test
//
//class Puzzle3Test {
//    val puzzleText = this::class.java.getResource(
//            "/2015/puzzle3.txt").readText().replace("\r", "")
//    val puzzle = Puzzle3()
//
//    @Test
//    fun `puzzle part a`() {
//        val result = puzzle.stateAfter(puzzleText)
//        assertEquals(11108, result.length)
//    }
//
//    @Test
//    fun `puzzle part b`() {
//        val result = puzzle.solveTwo(puzzleText)
//        assertEquals(5094, result)
//    }
//
//}
//
//class Puzzle3 {
//    fun stateAfter(puzzleText: String): Int {
//        return 0
//    }
//
//    fun solveTwo(puzzleText: String): Int {
//        return 0
//    }
//}