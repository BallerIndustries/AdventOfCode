package Year2017

import junit.framework.Assert.assertEquals
import org.junit.Test

class Puzzle20Test {
    val puzzle = Puzzle20()
    val puzzleText = this::class.java.getResource("/2017/puzzle20.txt").readText().replace("\r", "")

    @Test
    fun `puzzle part a`() {
        // 854 too something
        // 119 too low!
        val result = puzzle.solveOne(puzzleText)
        assertEquals(170, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(571, result)
    }
}

class Puzzle20 {

    data class Vector3(val x: Int, val y: Int, val z: Int) {
        fun manhattan() = Math.abs(x) + Math.abs(y) + Math.abs(z)

        operator fun plus(b: Vector3): Vector3 {
            return this.copy(x = x + b.x, y = y + b.y, z = z + b.z)
        }

    }

    data class Particle(val particleNumber: Int, val position: Vector3, val velocity: Vector3, val acceleration: Vector3) {
        fun tick(): Particle {
            val newVelocity = this.velocity + this.acceleration
            val newPosition = this.position + newVelocity

            return this.copy(velocity = newVelocity, position = newPosition)
        }
    }

    fun solveOne(puzzleText: String): Int {
        val particles = parseParticles(puzzleText)
        val minAccell = particles.map { it.acceleration.manhattan() }.min()!!
        val apple = particles.filter { it.acceleration.manhattan() == minAccell }
        val minVelo = apple.map { it.velocity.manhattan() }.min()!!
        val bananas = apple.filter { it.velocity.manhattan() == minVelo }

        return bananas.first().particleNumber
    }

    private fun parseParticles(puzzleText: String): List<Particle> {
        val particles = puzzleText.split("\n").mapIndexed { index, it ->
            val jur = it.split(", ")
            val (position, velocity, acceleration) = jur.map { vector ->
                val dog = vector.substring(3, vector.length - 1).split(",").map { it.toInt() }
                Vector3(dog[0], dog[1], dog[2])
            }

            Particle(index, position, velocity, acceleration)
        }
        return particles
    }

    fun solveTwo(puzzleText: String): Int {
        var particles = parseParticles(puzzleText)

        (0 until 1000).forEach { time ->
            particles = particles.map { it.tick() }
            val collidedParticles = particles.groupBy { it.position }.filter { it.value.size > 1 }.flatMap { it.value }.map { it.particleNumber }.toSet()
            particles = particles.filter { !collidedParticles.contains(it.particleNumber) }
        }

        return particles.count()
    }
}