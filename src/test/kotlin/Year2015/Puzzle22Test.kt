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

    data class Shield(override val remainingTurns: Int = 6): Effect {
        override val gameStateChange = { boss: Boss, player: Player ->
            Pair(boss, player.setArmor(7))
        }
    }

    data class Poison(override val remainingTurns: Int = 6): Effect {
        override val gameStateChange = { boss: Boss, player: Player ->
            Pair(boss.receiveDamage(3), player)
        }
    }

    data class Recharge(override val remainingTurns: Int = 6): Effect {
        override val gameStateChange = { boss: Boss, player: Player ->
            Pair(boss, player.gainMana(101))
        }
    }

//    data class Magic




    val magicMissile: GameStateChange = { boss, player ->
        Pair(boss.receiveDamage(4), player.useMana(53))
    }

    val drain: GameStateChange = { boss, player ->
        Pair(boss.receiveDamage(2), player.useMana(73).heal(2))
    }

    val shield: GameStateChange = { boss, player ->
        Pair(boss, player.useMana(113).startEffect(Shield()))
    }

    val poison: GameStateChange = { boss, player ->
        Pair(boss, player.useMana(173).startEffect(Poison()))
    }

    val recharge: GameStateChange = { boss, player ->
        Pair(boss, player.useMana(229).startEffect(Recharge()))
    }

    data class Boss(val hp: Int, val damage: Int) {
        companion object {
            fun parse(puzzleText: String): Puzzle22.Boss {
                val (hitPoints, damage) = puzzleText.split("\n").map { line -> line.split(": ")[1].toInt() }
                return Boss(hitPoints, damage)
            }
        }

        fun receiveDamage(amount: Int) = this.copy(hp = this.hp - amount)
    }

    data class Player(val hp: Int = 50, val armor: Int = 0, val mana: Int = 500, val effects: List<Effect> = emptyList()) {
        val maxHp: Int = 50
        val maxMana: Int = 500

        fun useMana(amount: Int) = this.copy(mana = this.mana - amount)

        fun heal(amount: Int) = this.copy(hp = Math.min(maxHp, this.hp + amount))

        fun startEffect(effect: Effect) = this.copy(effects = this.effects + effect)

        fun setArmor(amount: Int) = this.copy(armor = amount)

        fun gainMana(amount: Int) = this.copy(mana = Math.min(this.mana + amount, maxMana))
    }

    fun solveOne(puzzleText: String): Int {
        val boss = Boss.parse(puzzleText)



        return 100
    }

    fun solveTwo(puzzleText: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}