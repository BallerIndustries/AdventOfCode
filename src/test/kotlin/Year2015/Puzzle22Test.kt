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
        // 992 too high
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
    enum class BattleResult { PLAYER_WON, PLAYER_KILLED_BY_BOSS, PLAYER_CANNOT_CAST_SPELL }

    interface Effect {
        val remainingTurns: Int
        val gameStateChange: GameStateChange
        fun decrementTurnCount(): Effect
        fun describe(): String
    }

    interface Spell {
        val manaCost: Int
        val gameStateChange: GameStateChange
    }

    data class ShieldEffect(override val remainingTurns: Int = 6): Effect {
        override fun describe(): String {
            return "Shields timer is now ${remainingTurns - 1}."
        }

        override fun decrementTurnCount(): Effect {
            return this.copy(remainingTurns = this.remainingTurns - 1)
        }

        override val gameStateChange = { boss: Boss, player: Player ->
            Pair(boss, player.setArmor(7))
        }
    }

    data class PoisonEffect(override val remainingTurns: Int = 6): Effect {
        override fun describe(): String {
            return "Poison deals 3 damage; its timer is now ${remainingTurns - 1}."
        }

        override fun decrementTurnCount(): Effect {
            return this.copy(remainingTurns = this.remainingTurns - 1)
        }

        override val gameStateChange = { boss: Boss, player: Player ->
            Pair(boss.receiveDamage(3), player)
        }
    }

    data class RechargeEffect(override val remainingTurns: Int = 6): Effect {
        override fun describe(): String {
            return "Recharge provides 101 mana; its timer is now ${remainingTurns - 1}."
        }

        override fun decrementTurnCount(): Effect {
            return this.copy(remainingTurns = this.remainingTurns - 1)
        }

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

        fun isDead() = this.hp <= 0

        override fun toString(): String {
            return "Boss has $hp hit points"
        }
    }

    data class Player(val hp: Int = 50, val armor: Int = 0, val mana: Int = 500, val effects: List<Effect> = emptyList(), val manaSpent: Int = 0) {
        val maxHp: Int = 50
        val maxMana: Int = 500

        fun useMana(amount: Int) = this.copy(mana = this.mana - amount, manaSpent =  this.manaSpent + amount)

        fun heal(amount: Int) = this.copy(hp = Math.min(maxHp, this.hp + amount))

        fun addEffect(effect: Effect) = this.copy(effects = this.effects + effect)

        fun setArmor(amount: Int) = this.copy(armor = amount)

        fun gainMana(amount: Int) = this.copy(mana = Math.min(this.mana + amount, maxMana))

        fun isDead() = this.hp <= 0

        fun receiveDamage(amount: Int): Player {
            val damageAmount = if (amount - armor >= 1) amount - armor else 1
            return this.copy(hp = this.hp - damageAmount)
        }

        fun canCastSpell(allSpells: List<Spell>) = getCastableSpells(allSpells).isNotEmpty()

        fun getCastableSpells(allSpells: List<Spell>): List<Spell> {
            // TODO: Super shit, do this better you asshole
            val liveEffectNames = effects.map { it::class.simpleName!!.replace("Effect", "") }.toSet()

            return allSpells.filter { spell ->
                val spellName = spell::class.simpleName!!.replace("Spell", "")
                !liveEffectNames.contains(spellName)
            }.filter { spell ->
                mana > spell.manaCost
            }
        }

        override fun toString(): String {
            return "Player has $hp hit points, $armor armor, $mana mana"
        }

        fun getRandomSpell(allSpells: List<Spell>): Spell? {
            val castableSpells = getCastableSpells(allSpells)
            return if (castableSpells.isEmpty()) null else castableSpells.random()
        }

        // TODO: Not nice
        fun triggerEffects(boss: Boss): Pair<Boss, Player> {
            var b = boss
            var p = this

            p.effects.forEach { effect ->
                println("${effect.describe()}")
                val dog = effect.gameStateChange(boss, this)
                b = dog.first
                p = dog.second
            }

            val newEffects = p.effects
                .map { effect -> effect.decrementTurnCount() }
                .filter { it.remainingTurns > 0 }

            // TODO: Super not nice! Fix this you dick
            if (!newEffects.map { it::class.simpleName }.contains("ShieldEffect")) {
                p = p.setArmor(0)
            }

            p = p.copy(effects =  newEffects)
            return b to p
        }
    }

    val allSpells = listOf(MagicMissileSpell(), DrainSpell(), ShieldSpell(), PoisonSpell(), RechargeSpell())

    fun solveOne(puzzleText: String): Int {
        val boss = Boss.parse(puzzleText)
        val player = Player()

        val playerWins = (0 until 1)
            .map { runBattle(boss, player) }
            .filter { it.first == BattleResult.PLAYER_WON }
            .sortedBy { it.second.manaSpent }

        println(playerWins)
        println(playerWins.count())
        return 22
    }

    private fun runBattle(boss: Boss, player: Player): Pair<BattleResult, Player> {
        var boss = boss
        var player = player

        while (true) {
            val turnResult = runATurn(player, boss)
            boss = turnResult.first
            player = turnResult.second

            if (boss.isDead()) return BattleResult.PLAYER_WON to player
            else if (!player.canCastSpell(allSpells)) return BattleResult.PLAYER_CANNOT_CAST_SPELL to player
            else if (player.isDead()) return BattleResult.PLAYER_KILLED_BY_BOSS to player
        }
    }

    private fun runATurn(player: Player, boss: Boss): Pair<Boss, Player> {
        var player = player
        var boss = boss

        println("-- Player Turn --")
        println("- $player")
        println("- $boss")

        // TODO: Trigger effects at the start of the turns
        var dog: Pair<Boss, Player> = player.triggerEffects(boss)
        boss = dog.first
        player = dog.second

        if (player.isDead() || boss.isDead()) {
            return boss to player
        }

        val randomSpell: Spell = player.getRandomSpell(allSpells) ?: return boss to player
        println("- Player casts ${randomSpell::class.simpleName?.replace("Spell", "")}.")
        println()

        if (player.isDead() || boss.isDead()) {
            return boss to player
        }

        val (nextBoss, nextPlayer) = randomSpell.gameStateChange(boss, player)
        boss = nextBoss
        player = nextPlayer

        // TODO: Trigger effects at the start of the turns
        dog = player.triggerEffects(boss)
        boss = dog.first
        player = dog.second

        if (player.isDead() || boss.isDead()) {
            return boss to player
        }

        println("-- Boss Turn --")
        println("- $player")
        println("- $boss")
        println("- Boss attacks for ${boss.damage - player.armor} damage")
        println()

        player = player.receiveDamage(boss.damage)
        return boss to player
    }

    fun solveTwo(puzzleText: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}