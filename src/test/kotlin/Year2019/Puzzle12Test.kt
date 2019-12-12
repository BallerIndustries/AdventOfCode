package Year2019

import junit.framework.TestCase.assertEquals
import org.junit.Test

class Puzzle12Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle12.txt").readText().replace("\r", "")
    val puzzle = Puzzle12()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals("a", result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals("a", result)
    }

    @Test
    fun `example a`() {
        val text = """
            <x=-1, y=0, z=2>
            <x=2, y=-10, z=-7>
            <x=4, y=-8, z=8>
            <x=3, y=5, z=-1>
        """.trimIndent()

        val result = puzzle.solveOne(text)
        assertEquals("a", result)


    }
}

class Puzzle12 {

    data class Vector3(val x: Long, val y: Long, val z: Long) {
        operator fun plus(vector3: Vector3): Vector3 {
            return Vector3(this.x + vector3.x, this.y + vector3.y, this.z + vector3.z)
        }

        fun sumOfAbsoluteValues(): Long {
            return Math.abs(x) + Math.abs(y) + Math.abs(z)
        }

        override fun toString(): String {
            return "<x=$x, y=$y, z=$z>"
        }


    }

    data class Moon(val moonId: Int, val position: Vector3, val velocity: Vector3) {
        fun addVelocity(delta: Vector3): Moon {
            return this.copy(velocity = this.velocity + delta)
        }

        fun move(): Moon {
            return this.copy(position = this.position + this.velocity)
        }

        fun totalEnergy(): Long {
            return this.potentialEnergy() * this.kineticEnergy()
        }

        private fun kineticEnergy(): Long {
            return this.velocity.sumOfAbsoluteValues()
        }

        private fun potentialEnergy(): Long {
            return this.position.sumOfAbsoluteValues()
        }

        override fun toString(): String {
            return "position=$position, velocity=$velocity)"
        }


    }

    fun solveOne(puzzleText: String): Double {
        var moons = parseMoons(puzzleText)

        println("After 0 steps:")
        moons.forEach { println(it) }
        println()

        // Move the moons along for 1000 steps
        val energies = (1 .. 1000).map { step ->
            // Figure out the new velocity of each moon pased on the gravity
            moons = moons.map { currentMoon ->

                val otherMoons = moons.filter { it != currentMoon }

                val velocityDelta = otherMoons.map { otherMoon ->
                    val x = otherMoon.position.x.compareTo(currentMoon.position.x).toLong()
                    val y = otherMoon.position.y.compareTo(currentMoon.position.y).toLong()
                    val z = otherMoon.position.z.compareTo(currentMoon.position.z).toLong()
                    Vector3(x, y, z)
                }.reduce { acc, vector3 ->
                    acc + vector3
                }

                currentMoon.addVelocity(velocityDelta)
            }

            // Move the moons along based on their positions
            moons = moons.map { it.move() }

            val totalEnergy = moons.sumByDouble {moon ->
                moon.totalEnergy().toDouble()
            }

            println("After $step steps:")
            moons.forEach { println(it) }

            println(totalEnergy)
            totalEnergy
        }


        return energies.last()

    }

    private fun parseMoons(puzzleText: String): List<Moon> {
        var moons = puzzleText
                .replace("x=", "")
                .replace("y=", "")
                .replace("z=", "")
                .replace("<", "")
                .replace(">", "")
                .split("\n")
                .mapIndexed { index, line ->
                    val (x, y, z) = line.split(", ").map { it.toLong() }
                    val position = Vector3(x, y, z)
                    val velocity = Vector3(0, 0, 0)
                    Moon(index, position, velocity)
                }
        return moons
    }

    fun solveTwo(puzzleText: String): String {
        throw NotImplementedError()
    }
}

