package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle22Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle22.txt").readText().replace("\r", "")
    val puzzle = Puzzle22()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(33393, result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(31963, result)
    }

    @Test
    fun `example part a`() {
        val puzzleText = "Player 1:\n" +
                "9\n" +
                "2\n" +
                "6\n" +
                "3\n" +
                "1\n" +
                "\n" +
                "Player 2:\n" +
                "5\n" +
                "8\n" +
                "4\n" +
                "7\n" +
                "10"
        val result = puzzle.solveOne(puzzleText)
        assertEquals(306, result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = "Player 1:\n" +
                "9\n" +
                "2\n" +
                "6\n" +
                "3\n" +
                "1\n" +
                "\n" +
                "Player 2:\n" +
                "5\n" +
                "8\n" +
                "4\n" +
                "7\n" +
                "10"
        val result = puzzle.solveTwo(puzzleText)
        assertEquals(291, result)
    }
}

class Puzzle22 {
    fun solveOne(puzzleText: String): Int {
        val (player1Deck, player2Deck) = parseDecks(puzzleText)

        while (player1Deck.isNotEmpty() && player2Deck.isNotEmpty()) {

            val player1Card = player1Deck.removeAt(0)
            val player2Card = player2Deck.removeAt(0)

            if (player1Card > player2Card) {
                player1Deck.add(player1Card)
                player1Deck.add(player2Card)
            }
            else {
                player2Deck.add(player2Card)
                player2Deck.add(player1Card)
            }
        }

        val winningDeck = if (player1Deck.isEmpty()) player2Deck else player1Deck
        return winningDeck.reversed().mapIndexed { index, value -> (index + 1) * value }.sum()
    }

    private fun parseDecks(puzzleText: String): Pair<MutableList<Int>, MutableList<Int>> {
        val (p1Tmp, p2Tmp) = puzzleText.split("\n\n").map { it.split("\n") }

        val player1Deck = p1Tmp.subList(1, p1Tmp.size).map { it.toInt() }.toMutableList()
        val player2Deck = p2Tmp.subList(1, p2Tmp.size).map { it.toInt() }.toMutableList()
        return Pair(player1Deck, player2Deck)
    }

    fun solveTwo(puzzleText: String): Int {
        val (player1Deck, player2Deck) = parseDecks(puzzleText)

        val (_, gameState) = runAGame(1, player1Deck, player2Deck)

        val winningDeck = if (gameState.player1Deck.isEmpty()) gameState.player2Deck else gameState.player1Deck
        return winningDeck.reversed().mapIndexed { index, value -> (index + 1) * value }.sum()
    }

    data class GameState(val player1Deck: List<Int>, val player2Deck: List<Int>)

    private fun runAGame(gameNumber: Int, _player1Deck: List<Int>, _player2Deck: List<Int>): Pair<Int, GameState> {
        // "Otherwise, at least one player must not have enough cards left in their deck to recurse; the winner of the"
        // "round is the player with the higher-value card."

        var roundNumber = 1
        val gameStates = mutableSetOf<GameState>()


        val player1Deck = _player1Deck.map { it }.toMutableList()
        val player2Deck = _player2Deck.map { it }.toMutableList()
        var latestGameState = GameState(player1Deck, player2Deck)

        while (player1Deck.isNotEmpty() && player2Deck.isNotEmpty()) {
            latestGameState = GameState(player1Deck, player2Deck)

            // "if there was a previous round in this game that had exactly the same cards in the same order in the same"
            // "players' decks, the game instantly ends in a win for player 1."
            if (hasThisHappenedBefore(gameStates, player1Deck, player2Deck)) {
                return 1 to latestGameState
            }

            gameStates.add(latestGameState)

            val player1Card = player1Deck.removeAt(0)
            val player2Card = player2Deck.removeAt(0)

            // "If both players have at least as many cards remaining in their deck as the value of the card they just drew,"
            // "the winner of the round is determined by playing a new game of Recursive Combat"

            // Player 1 draws card with value = 3. Player 1 has 5 cards remaining.
            // Player 2 draws card with value = 4. Player 1 has 5 cards remaining.
            val player1HasMoreCardsThanValueOfDrawnCard = player1Deck.size >= player1Card
            val player2HasMoreCardsThanValueOfDrawnCard = player2Deck.size >= player2Card

            val winnerOfRound =
                if (player1HasMoreCardsThanValueOfDrawnCard && player2HasMoreCardsThanValueOfDrawnCard) {
                    // RECURSIVE COMBAT
                    val (roundWinner, _) = runAGame(gameNumber + 1, player1Deck.subList(0, player1Card), player2Deck.subList(0, player2Card))
                    roundWinner
                } else {
                    if (player1Card > player2Card) 1 else 2
                }

            if (winnerOfRound == 1) {
                player1Deck.add(player1Card)
                player1Deck.add(player2Card)
            } else {
                player2Deck.add(player2Card)
                player2Deck.add(player1Card)
            }

            roundNumber++
        }

        val winningPlayer = if (player1Deck.isEmpty()) 2 else 1
        return winningPlayer to latestGameState
    }

    private fun hasThisHappenedBefore(
        gameStates: MutableSet<GameState>,
        player1Deck: List<Int>,
        player2Deck: List<Int>
    ): Boolean {
        return GameState(player1Deck, player2Deck) in gameStates
    }
}

