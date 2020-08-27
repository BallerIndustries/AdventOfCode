package Year2019

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.abs

class Puzzle12Test {
    val puzzleText = this::class.java.getResource("/2019/puzzle12.txt").readText().replace("\r", "")
    val puzzle = Puzzle12()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(13399.0, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(312992287193064, result)
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
        assertEquals(183.0, result)
    }
}

class Puzzle12 {
    data class Vector3(val x: Long, val y: Long, val z: Long) {
        operator fun plus(vector3: Vector3): Vector3 {
            return Vector3(this.x + vector3.x, this.y + vector3.y, this.z + vector3.z)
        }

        fun sumOfAbsoluteValues(): Long {
            return abs(x) + abs(y) + abs(z)
        }

        override fun toString(): String {
            return "<x=$x, y=$y, z=$z>"
        }
    }

    data class Moon(val position: Vector3, val velocity: Vector3) {
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

        fun xOnly() = this.position.x to this.velocity.x
        fun yOnly() = this.position.y to this.velocity.y
        fun zOnly() = this.position.z to this.velocity.z
    }

    fun solveOne(puzzleText: String): Double {
        var moons = parseMoons(puzzleText)

        // Move the moons along for 1000 steps
        val energies = (1..1000).map {
            // Figure out the new velocity of each moon pased on the gravity
            moons = moveMoonsAlong(moons)
            val totalEnergy = moons.sumByDouble { moon -> moon.totalEnergy().toDouble()  }
            totalEnergy
        }

        return energies.last()
    }

    private fun moveMoonsAlong(moons: List<Moon>): List<Moon> {
        var moons1 = moons
        moons1 = moons1.map { currentMoon ->
            val otherMoons = moons1.filter { it != currentMoon }

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
        moons1 = moons1.map { it.move() }
        return moons1
    }

    private fun parseMoons(puzzleText: String): List<Moon> {
        return puzzleText
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
                Moon(position, velocity)
            }
    }

    fun solveTwo(puzzleText: String): Long {
        var moons = parseMoons(puzzleText)
        val xPositionsAndVelocity = mutableSetOf(moons.map { it.xOnly() })
        val yPositionsAndVelocity = mutableSetOf(moons.map { it.yOnly() })
        val zPositionsAndVelocity = mutableSetOf(moons.map { it.zOnly() })

        var xCycle = -1L
        var yCycle = -1L
        var zCycle = -1L

        for (step in 1 .. Long.MAX_VALUE) {
            // Figure out the new velocity of each moon pased on the gravity
            moons = moveMoonsAlong(moons)

            val xPoints = moons.map { it.xOnly() }
            val yPoints = moons.map { it.yOnly() }
            val zPoints = moons.map { it.zOnly() }

            if (xPositionsAndVelocity.contains(xPoints) && xCycle == -1L) {
                xCycle = step
            }
            if (yPositionsAndVelocity.contains(yPoints) && yCycle == -1L) {
                yCycle = step
            }
            if (zPositionsAndVelocity.contains(zPoints) && zCycle == -1L) {
                zCycle = step
            }

            zPositionsAndVelocity.add(zPoints)
            xPositionsAndVelocity.add(xPoints)
            yPositionsAndVelocity.add(yPoints)

            if (zCycle > 0 && yCycle > 0 && xCycle > 0) {
                break
            }
        }

        return lcm(listOf(xCycle, yCycle, zCycle))
    }


    private fun lcm(a: Long, b: Long): Long {
        return a * (b / gcd(a, b))
    }

    private fun lcm(input: List<Long>): Long {
        var result = input[0]
        for (i in 1 until input.size) result = lcm(result, input[i])
        return result
    }

    private fun gcd(a: Long, b: Long): Long {
        var a = a
        var b = b
        while (b > 0) {
            val temp = b
            b = a % b // % is remainder
            a = temp
        }
        return a
    }


}

