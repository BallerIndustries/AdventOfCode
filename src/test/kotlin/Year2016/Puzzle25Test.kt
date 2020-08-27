package Year2016

import org.junit.jupiter.api.Test

class Puzzle25Test {
    val puzzle = Puzzle25()
    val puzzleText = this::class.java.getResource("/2016/puzzle25.txt").readText().replace("\r", "")


    @Test
    fun `a aa a a aaa`() {


        fun horseHorse(target: Int): Int {

            var n = 1
            while (n < target) {
                if (n % 2 == 0) {
                    n = n * 2 + 1
                }
                else {
                    n *= 2
                }
            }

            return n - target
        }

        val target = 9 * 282
        println(horseHorse(target))
    }
}

class Puzzle25 {
}
