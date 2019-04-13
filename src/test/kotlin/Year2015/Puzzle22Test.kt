package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Test

typealias GameStateChange = (Puzzle22.Boss, Puzzle22.Player) -> Pair<Puzzle22.Boss, Puzzle22.Player>

class Puzzle22Test {
    val puzzle = Puzzle22()
    val puzzleText = this::class.java.getResource("/2015/puzzle22.txt").readText().replace("\r", "")
    val exampleText = """""".trimIndent()

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

class Puzzle22 {
    interface Effect {
        val remainingTurns: Int
        val gameStateChange: GameStateChange
    }

    interface Spell {
        val manaCost: Int
        val gameStateChange: GameStateChange
    }

    data class ShieldEffect(override val remainingTurns: Int = 6): Effect {
        override val gameStateChange = { boss: Boss, player: Player ->
            Pair(boss, player.setArmor(7))
        }
    }

    data class PoisonEffect(override val remainingTurns: Int = 6): Effect {
        override val gameStateChange = { boss: Boss, player: Player ->
            Pair(boss.receiveDamage(3), player)
        }
    }

    data class RechargeEffect(override val remainingTurns: Int = 6): Effect {
        override val gameStateChange = { boss: Boss, player: Player ->
            Pair(boss, player.gainMana(101))
        }
    }

    class MagicMissileSpell : Spell {
        override val manaCost = 53

        override val gameStateChange: GameStateChange = { boss, player ->
            Pair(boss.receiveDamage(4), player.useMana(manaCost))
        }
    }

    class DrainSpell : Spell {
        override val manaCost = 73

        override val gameStateChange: GameStateChange = { boss, player ->
            Pair(boss.receiveDamage(2), player.useMana(manaCost).heal(2))
        }
    }

    class ShieldSpell : Spell {
        override val manaCost = 113

        override val gameStateChange: GameStateChange = { boss, player ->
            Pair(boss, player.useMana(manaCost).addEffect(ShieldEffect()))
        }
    }

    class PoisonSpell : Spell {
        override val manaCost = 173

        override val gameStateChange: GameStateChange = { boss, player ->
            Pair(boss, player.useMana(manaCost).addEffect(PoisonEffect()))
        }
    }

    class RechargeSpell : Spell {
        override val manaCost = 229

        override val gameStateChange: GameStateChange = { boss, player ->
            Pair(boss, player.useMana(manaCost).addEffect(RechargeEffect()))
        }
    }

    data class Boss(val hp: Int, val damage: Int) {
        companion object {
            fun parse(puzzleText: String): Puzzle22.Boss {
                val (hitPoints, damage) = puzzleText.split("\n").map { line -> line.split(": ")[1].toInt() }
                return Boss(hitPoints, damage)
            }
        }

        fun receiveDamage(amount: Int) = this.copy(hp = this.hp - amount)

        override fun toString(): String {
            return "Boss has $hp hit points"
        }
    }

    data class Player(val hp: Int = 50, val armor: Int = 0, val mana: Int = 500, val effects: List<Effect> = emptyList()) {
        val maxHp: Int = 50
        val maxMana: Int = 500

        fun useMana(amount: Int) = this.copy(mana = this.mana - amount)

        fun heal(amount: Int) = this.copy(hp = Math.min(maxHp, this.hp + amount))

        fun addEffect(effect: Effect) = this.copy(effects = this.effects + effect)

        fun setArmor(amount: Int) = this.copy(armor = amount)

        fun gainMana(amount: Int) = this.copy(mana = Math.min(this.mana + amount, maxMana))

        fun receiveDamage(amount: Int): Player {
            val damageAmount = if (amount - armor >= 1) amount - armor else 1
            return this.copy(hp = this.hp - damageAmount)
        }

        override fun toString(): String {
            return "Player has $hp hit points, $armor armor, $mana mana effects = $effects"
        }
    }

    val allSpells = listOf(MagicMissileSpell(), DrainSpell(), ShieldSpell(), PoisonSpell(), RechargeSpell())

    fun solveOne(puzzleText: String): Int {
        var boss = Boss.parse(puzzleText)
        var player = Player()

        // pick a random spell
        val randomSpell = allSpells.random()

        println("-- Player Turn --")
        println("- $player")
        println("- $boss")
        println("- Player casts ${randomSpell::class.simpleName?.replace("Spell", "")}.")
        println()

        val (nextBoss, nextPlayer) = randomSpell.gameStateChange(boss, player)
        boss = nextBoss
        player = nextPlayer

        println("-- Player Turn --")
        println("- $player")
        println("- $boss")
        player = player.receiveDamage(boss.damage)


        return 100
    }

    fun solveTwo(puzzleText: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}