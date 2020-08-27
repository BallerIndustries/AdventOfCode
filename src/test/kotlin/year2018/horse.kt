import java.lang.Math.abs
import java.lang.Math.max


fun main(args: Array<String>) {
    //val input = File("data/23a").readLines().map { Bot.parse(it) }.toSet()

    val input = Coord::class.java.getResource("/2018/puzzle23.txt").readText().replace("\r", "").split("\n").map { Bot.parse(it) }.toSet()

    // Part 1
    val maxRadiusBot = input.maxBy { it.r } ?: return
    println(input.count { it.copy(r = 0).intersects(maxRadiusBot) })

    // Part 2
    val startPosition = Coord(0, 0, 0)

    var currentRadius = max(input.deltaBy { it.pos.x }, max(input.deltaBy { it.pos.y }, input.deltaBy { it.pos.z }))

    var currentBots = setOf(Bot(Coord(0, 0, 0), currentRadius))

    while (currentRadius > 0) {
        currentRadius = (currentRadius / 2) + if (currentRadius > 2) 1 else 0

        val newGeneration = currentBots.flatMap { bot ->
            bot.pos.neighbors(currentRadius).map { c ->
                bot.copy(pos = c, r = currentRadius).let { newBot ->
                    newBot to input.count {
                        newBot.intersects(it)
                    }
                }
            }
        }
        val maxDistance = newGeneration.map { it.second }.max() ?: 0

        currentBots = newGeneration.filter { it.second == maxDistance }.map { it.first }.toSet()
    }

    println(currentBots.minBy { startPosition.distanceTo(it.pos) }?.pos?.distanceTo(startPosition))
}

inline fun <T> Iterable<T>.deltaBy(block: (T) -> Long): Long {
    val values = map(block)
    return abs((values.max() ?: 0L) - (values.min() ?: 0L))
}

data class Coord(val x: Long, val y: Long, val z: Long) {
    companion object {
        fun parse(input: String): Coord {
            val (x, y, z) = input
                    .split(",")
                    .map { it.filter { c -> c == '-' || c in '0'..'9' }.toLong() }

            return Coord(x, y, z)
        }

    }

    fun distanceTo(other: Coord): Long = abs(x - other.x) + abs(y - other.y) + abs(z - other.z)

    fun neighbors(delta: Long): Iterable<Coord> =
            (-1L..1L).flatMap { xd ->
                (-1L..1L).flatMap { yd ->
                    (-1L..1L).map { zd ->
                        this.copy(
                                x = this.x + xd * delta,
                                y = this.y + yd * delta,
                                z = this.z + zd * delta
                        )
                    }
                }
            }

    override fun toString(): String = "<$x,$y,$z>"
}

data class Bot(val pos: Coord, val r: Long) {
    companion object {
        fun parse(input: String): Bot {
            val (coord, r) = input.split(", ")
            return Bot(Coord.parse(coord), r.split("=")[1].toLong())
        }
    }

    fun intersects(other: Bot) =
            pos.distanceTo(other.pos) <= r + other.r
}