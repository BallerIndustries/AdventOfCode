package Year2015

import junit.framework.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

typealias GameStateChange = (Puzzle22.Boss, Puzzle22.Player) -> Pair<Puzzle22.Boss, Puzzle22.Player>

class Puzzle22Test {
    private val puzzle = Puzzle22()

    private val puzzleText = this::class.java.getResource("/2015/puzzle22.txt").readText().replace("\r", "")

    private val debugLines = mutableListOf<String>()

    private val consoleSavingLogger : (String) -> Unit = { text ->
        debugLines.add(text)
        println(text)
    }

    private fun simulateBattle(boss: Puzzle22.Boss, player: Puzzle22.Player, spells: List<Puzzle22.Spell>): String {
        var mutBoss = boss
        var mutPlayer = player

        spells.forEach { spell ->

            val spellGetter = { spell }
            val (boasss, playa) = puzzle.runATurn(mutPlayer, mutBoss, spellGetter, consoleSavingLogger)

            mutBoss = boasss
            mutPlayer = playa
        }

        return debugLines.joinToString("\n")
    }

    @Test
    fun `example one`() {
        val player = Puzzle22.Player(hp = 10, mana = 250)
        val boss = Puzzle22.Boss(hp = 13, damage = 8)

        val expectedOutput = """
            -- Player turn --
            - Player has 10 hit points, 0 armor, 250 mana
            - Boss has 13 hit points
            Player casts Poison.

            -- Boss turn --
            - Player has 10 hit points, 0 armor, 77 mana
            - Boss has 13 hit points
            Poison deals 3 damage; its timer is now 5.
            Boss attacks for 8 damage.

            -- Player turn --
            - Player has 2 hit points, 0 armor, 77 mana
            - Boss has 10 hit points
            Poison deals 3 damage; its timer is now 4.
            Player casts Magic Missile, dealing 4 damage.

            -- Boss turn --
            - Player has 2 hit points, 0 armor, 24 mana
            - Boss has 3 hit points
            Poison deals 3 damage. This kills the boss, and the player wins.
        """.trimIndent()

        val consoleOutput = simulateBattle(boss, player, listOf(Puzzle22.PoisonSpell(), Puzzle22.MagicMissileSpell()))
        assertEquals(expectedOutput, consoleOutput)
    }

    @Test
    fun `example two`() {
        val player = Puzzle22.Player(hp = 10, mana = 250)
        val boss = Puzzle22.Boss(hp = 14, damage = 8)

        val expectedOutput = """
            -- Player turn --
            - Player has 10 hit points, 0 armor, 250 mana
            - Boss has 14 hit points
            Player casts Recharge.

            -- Boss turn --
            - Player has 10 hit points, 0 armor, 21 mana
            - Boss has 14 hit points
            Recharge provides 101 mana; its timer is now 4.
            Boss attacks for 8 damage.

            -- Player turn --
            - Player has 2 hit points, 0 armor, 122 mana
            - Boss has 14 hit points
            Recharge provides 101 mana; its timer is now 3.
            Player casts Shield, increasing armor by 7.

            -- Boss turn --
            - Player has 2 hit points, 7 armor, 110 mana
            - Boss has 14 hit points
            Shield's timer is now 5.
            Recharge provides 101 mana; its timer is now 2.
            Boss attacks for 8 - 7 = 1 damage.

            -- Player turn --
            - Player has 1 hit point, 7 armor, 211 mana
            - Boss has 14 hit points
            Shield's timer is now 4.
            Recharge provides 101 mana; its timer is now 1.
            Player casts Drain, dealing 2 damage, and healing 2 hit points.

            -- Boss turn --
            - Player has 3 hit points, 7 armor, 239 mana
            - Boss has 12 hit points
            Shield's timer is now 3.
            Recharge provides 101 mana; its timer is now 0.
            Recharge wears off.
            Boss attacks for 8 - 7 = 1 damage.

            -- Player turn --
            - Player has 2 hit points, 7 armor, 340 mana
            - Boss has 12 hit points
            Shield's timer is now 2.
            Player casts Poison.

            -- Boss turn --
            - Player has 2 hit points, 7 armor, 167 mana
            - Boss has 12 hit points
            Shield's timer is now 1.
            Poison deals 3 damage; its timer is now 5.
            Boss attacks for 8 - 7 = 1 damage.

            -- Player turn --
            - Player has 1 hit point, 7 armor, 167 mana
            - Boss has 9 hit points
            Shield's timer is now 0.
            Shield wears off, decreasing armor by 7.
            Poison deals 3 damage; its timer is now 4.
            Player casts Magic Missile, dealing 4 damage.

            -- Boss turn --
            - Player has 1 hit point, 0 armor, 114 mana
            - Boss has 2 hit points
            Poison deals 3 damage. This kills the boss, and the player wins.
        """.trimIndent()

        val spells = listOf(
            Puzzle22.RechargeSpell(),
            Puzzle22.ShieldSpell(),
            Puzzle22.DrainSpell(),
            Puzzle22.PoisonSpell(),
            Puzzle22.MagicMissileSpell()
        )

        val consoleOutput = simulateBattle(boss, player, spells)
        assertEquals(expectedOutput, consoleOutput)

    }

    @Test
    @Ignore
    fun `puzzle part a`() {
        // 992 too high
        // 973 too high
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
        fun describe(): String
    }

    data class ShieldEffect(override val remainingTurns: Int = 6): Effect {
        override fun describe(): String {
            return "Shield's timer is now ${remainingTurns - 1}."
        }

        override fun decrementTurnCount(): Effect {
            return this.copy(remainingTurns = this.remainingTurns - 1)
        }

        override val gameStateChange = { boss: Boss, player: Player ->
            Pair(boss, player)
        }
    }

    data class PoisonEffect(override val remainingTurns: Int = 6): Effect {
        override fun describe(): String {
            return "Poison deals 3 damage"
        }

        override fun decrementTurnCount(): Effect {
            return this.copy(remainingTurns = this.remainingTurns - 1)
        }

        override val gameStateChange = { boss: Boss, player: Player ->
            Pair(boss.receiveDamage(3), player)
        }
    }

    data class RechargeEffect(override val remainingTurns: Int = 5): Effect {
        override fun describe(): String {
            return "Recharge provides 101 mana"
        }

        override fun decrementTurnCount(): Effect {
            return this.copy(remainingTurns = this.remainingTurns - 1)
        }

        override val gameStateChange = { boss: Boss, player: Player ->
            Pair(boss, player.gainMana(101))
        }
    }

    class MagicMissileSpell : Spell {
        override fun describe(): String {
            return "Player casts Magic Missile, dealing 4 damage."
        }

        override val manaCost = 53

        override val gameStateChange: GameStateChange = { boss, player ->
            Pair(boss.receiveDamage(4), player.useMana(manaCost))
        }
    }

    class DrainSpell : Spell {
        override fun describe(): String {
            return "Player casts Drain, dealing 2 damage, and healing 2 hit points."
        }

        override val manaCost = 73

        override val gameStateChange: GameStateChange = { boss, player ->
            Pair(boss.receiveDamage(2), player.useMana(manaCost).heal(2))
        }
    }

    class ShieldSpell : Spell {
        override fun describe(): String {
            return "Player casts Shield, increasing armor by 7."
        }

        override val manaCost = 113

        override val gameStateChange: GameStateChange = { boss, player ->
            Pair(boss, player.useMana(manaCost).addEffect(ShieldEffect()))
        }
    }

    class PoisonSpell : Spell {
        override fun describe(): String {
            return "Player casts Poison."
        }

        override val manaCost = 173

        override val gameStateChange: GameStateChange = { boss, player ->
            Pair(boss, player.useMana(manaCost).addEffect(PoisonEffect()))
        }
    }

    class RechargeSpell : Spell {
        override fun describe(): String {
            return "Player casts Recharge."
        }

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

    data class Player(val hp: Int = 50, val mana: Int = 500, val effects: List<Effect> = emptyList(), val manaSpent: Int = 0) {
        val maxHp: Int = 50
        val maxMana: Int = 500

        fun useMana(amount: Int) = this.copy(mana = this.mana - amount, manaSpent =  this.manaSpent + amount)

        fun heal(amount: Int) = this.copy(hp = Math.min(maxHp, this.hp + amount))

        fun addEffect(effect: Effect) = this.copy(effects = this.effects + effect)

        fun getArmor(): Int {
            val hasArmor = this.effects.any { it as? ShieldEffect != null }
            return if (hasArmor) 7 else 0
        }

        fun gainMana(amount: Int) = this.copy(mana = Math.min(this.mana + amount, maxMana))

        fun isDead() = this.hp <= 0

        fun receiveDamage(amount: Int): Player {
            val armor = getArmor()
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
            if (hp == 1) {
                return "Player has $hp hit point, ${getArmor()} armor, $mana mana"
            }
            else {
                return "Player has $hp hit points, ${getArmor()} armor, $mana mana"
            }
        }

        fun getRandomSpell(allSpells: List<Spell>): Spell? {
            val castableSpells = getCastableSpells(allSpells)
            return if (castableSpells.isEmpty()) null else castableSpells.random()
        }

        // TODO: Not nice
        fun triggerEffects(boss: Boss, logger: (String) -> Unit): Pair<Boss, Player> {
            var b = boss
            var p = this

            p.effects.sortedByDescending { it.remainingTurns }
                .forEach { effect ->

                val dog = effect.gameStateChange(b, this)
                b = dog.first
                p = dog.second

                if (b.isDead()) {
                    logger("${effect.describe()}. This kills the boss, and the player wins.")
                }
                else if (effect as? ShieldEffect != null) {
                    logger(effect.describe())
                }
                else {
                    logger("${effect.describe()}; its timer is now ${effect.remainingTurns - 1}.")
                }
            }

            val newEffects = p.effects
                .map { effect -> effect.decrementTurnCount() }
                .filter { it.remainingTurns > 0 }

            p = p.copy(effects =  newEffects)
            return b to p
        }
    }

    val allSpells = listOf(MagicMissileSpell(), DrainSpell(), ShieldSpell(), PoisonSpell(), RechargeSpell())

    fun solveOne(puzzleText: String): Int {
        val boss = Boss.parse(puzzleText)
        val player = Player()

        val playerWins = (0 until 100000)
            .map { runBattle(boss, player) }
            .filter { it.first == BattleResult.PLAYER_WON }
            .sortedBy { it.second.manaSpent }

        return playerWins.minBy { it.second.manaSpent }!!.second.manaSpent
    }

    private fun runBattle(boss: Boss, player: Player): Pair<BattleResult, Player> {
        var boss = boss
        var player = player

        while (true) {
            val playerSpellGetter = { player.getRandomSpell(allSpells) }
            val turnResult = runATurn(player, boss, playerSpellGetter, { })
            boss = turnResult.first
            player = turnResult.second

            if (boss.isDead()) {
                println("Player won! manaSpent = ${player.manaSpent}")
                return BattleResult.PLAYER_WON to player
            } else if (!player.canCastSpell(allSpells)) return BattleResult.PLAYER_CANNOT_CAST_SPELL to player
            else if (player.isDead()) return BattleResult.PLAYER_KILLED_BY_BOSS to player
        }
    }

    val consoleLogger : (String) -> Unit = { text -> println(text) }

    fun runATurn(player: Player, boss: Boss, playerSpellGetter: () -> Spell?, logger: (String) -> Unit = consoleLogger): Pair<Boss, Player> {
        var player = player
        var boss = boss

        logger("-- Player turn --")
        logger("- $player")
        logger("- $boss")

        // TODO: Trigger effects at the start of the turns
        var dog: Pair<Boss, Player> = player.triggerEffects(boss, logger)
        boss = dog.first
        player = dog.second

        if (player.isDead() || boss.isDead()) {
            return boss to player
        }

        val randomSpell: Spell = playerSpellGetter() ?: return boss to player
        logger(randomSpell.describe())
        logger("")

        if (player.isDead() || boss.isDead()) {
            return boss to player
        }

        val (nextBoss, nextPlayer) = randomSpell.gameStateChange(boss, player)
        boss = nextBoss
        player = nextPlayer

        if (player.isDead() || boss.isDead()) {
            return boss to player
        }

        logger("-- Boss turn --")
        logger("- $player")
        logger("- $boss")

        // TODO: Trigger effects at the start of the turns
        dog = player.triggerEffects(boss, logger)
        boss = dog.first
        player = dog.second

        if (player.isDead() || boss.isDead()) {
            return boss to player
        }

        val armor = player.getArmor()
        if (armor > 0) {
            logger("Boss attacks for ${boss.damage} - $armor = ${boss.damage - armor} damage.")
        }
        else {
            logger("Boss attacks for ${boss.damage - armor} damage.")
        }

        logger("")
        player = player.receiveDamage(boss.damage)
        return boss to player
    }

    fun solveTwo(puzzleText: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}