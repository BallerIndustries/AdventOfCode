package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.random.Random

class Puzzle21Test {
    val puzzle = Puzzle21()
    val puzzleText = this::class.java.getResource("/2015/puzzle21.txt").readText().replace("\r", "")
    val exampleText = """
    """.trimIndent()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(111, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(188, result)
    }
}

class Puzzle21 {
    data class Player(val hp: Int, val items: List<Item>) {
        fun damage(): Int {
            return items.sumBy { it.damage }
        }

        fun cost(): Int {
            return items.sumBy { it.cost }
        }

        fun armor(): Int {
            return items.sumBy { it.armor }
        }

        fun receiveDamage(damage: Int): Player {
            val damageReceived = if (damage - armor() <= 0) 1 else damage - armor()
            return this.copy(hp = this.hp - damageReceived)
        }
    }

    data class Boss(val hp: Int, val damage: Int, val armor: Int) {
        fun receiveDamage(damage: Int): Boss {
            val damageReceived = if (damage - armor <= 0) 1 else damage - armor
            return this.copy(hp = this.hp - damageReceived)
        }
    }

    val rings: List<Item>
    val weapons: List<Item>
    val armors: List<Item>

    data class Item(val name: String, val cost: Int, val damage: Int, val armor: Int)

    fun parse(text: String): List<Item> {
        return text.split("\n")
            .map { line ->
                val tmp = line.split(Regex("\\s+"))
                val name = tmp[0]
                val cost = tmp[1].toInt()
                val damage = tmp[2].toInt()
                val armor = tmp[3].toInt()

                Item(name, cost, damage, armor)
            }
    }

    init {
        val weaponText = """
        Dagger        8     4       0
        Shortsword   10     5       0
        Warhammer    25     6       0
        Longsword    40     7       0
        Greataxe     74     8       0""".trimIndent()


        val armorText = """
        Leather      13     0       1
        Chainmail    31     0       2
        Splintmail   53     0       3
        Bandedmail   75     0       4
        Platemail   102     0       5""".trimIndent()

        val ringText = """
        Damage+1    25     1       0
        Damage+2    50     2       0
        Damage+3   100     3       0
        Defense+1   20     0       1
        Defense+2   40     0       2
        Defense+3   80     0       3""".trimIndent()

        rings = parse(ringText)
        armors = parse(armorText)
        weapons = parse(weaponText)
    }

    data class Result(val playerWon: Boolean, val itemCost: Int)

    fun solveOne(puzzleText: String): Int {
        val initialBoss = parseBoss(puzzleText)
        val simulationResults = simulate1000Rounds(initialBoss)
        return simulationResults.filter { it.playerWon }.minBy { it.itemCost }!!.itemCost
    }

    fun solveTwo(puzzleText: String): Int {
        val initialBoss = parseBoss(puzzleText)
        val simulationResults = simulate1000Rounds(initialBoss)
        return simulationResults.filter { !it.playerWon }.maxBy { it.itemCost }!!.itemCost
    }

    private fun simulate1000Rounds(initialBoss: Boss): List<Result> {
        // You must buy exactly one weapon; no dual-wielding. Armor is optional, but you can't use more than one.
        // You can buy 0-2 rings (at most one for each hand). You must use any items you buy. The shop only has one
        // of each item, so you can't buy, for example, two rings of Damage +3.
        val random = Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
        return list(random, initialBoss)
    }

    private fun list(random: Random, initialBoss: Boss): List<Result> {
        return (0 until 1000).map {
            val player = createRandomPlayer(random)
            runRound(player, initialBoss)
        }
    }

    private fun runRound(playerYo: Player, bossYo: Boss): Result {
        var player = playerYo
        var boss = bossYo

        while (true) {
            boss = boss.receiveDamage(player.damage())

            if (boss.hp <= 0) {
                return Result(true, player.cost())
            }
            
            player = player.receiveDamage(boss.damage)
            
            if (player.hp <= 0) {
                return Result(false, player.cost())
            }
        }
    }

    private fun createRandomPlayer(random: Random): Player {
        val weapon = weapons.random(random)
        val ringCount = random.nextInt(3)
        val rings = rings.shuffled(random).subList(0, ringCount)
        val shouldBuyArmor = random.nextBoolean()

        val armor = if (shouldBuyArmor) listOf(armors.random(random)) else emptyList()
        val allItems = armor + rings + weapon

        return Player(100, allItems)
    }

    private fun parseBoss(puzzleText: String): Boss {
        val (hitPoints, damage, armour) = puzzleText.split("\n").map { line -> line.split(": ")[1].toInt() }
        return Boss(hitPoints, damage, armour)
    }
}