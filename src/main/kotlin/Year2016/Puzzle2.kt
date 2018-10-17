package Year2016

class Puzzle2 {
    fun decipher(input: String): String {
        var currentDigit = 5

        val horse = input.split("\n").map { digitCipherText ->
            digitCipherText.forEach { command ->
                currentDigit = nextDigit(currentDigit, command)
            }

            currentDigit.toString()
        }

        return horse.joinToString("")
    }

    private fun nextDigit(currentDigit: Int, command: Char): Int {
        val leftEdge = setOf(1, 4, 7)
        val rightEdge = setOf(3, 6, 9)

        if (command == 'U') {
            return if (currentDigit > 3) currentDigit - 3 else currentDigit
        }
        if (command == 'D') {
            return if (currentDigit <= 6) currentDigit + 3 else currentDigit
        }
        if (command == 'L') {
            return if (!leftEdge.contains(currentDigit)) currentDigit - 1 else currentDigit
        }
        if (command == 'R') {
            return if (!rightEdge.contains(currentDigit)) currentDigit + 1 else currentDigit
        }
        else {
            throw RuntimeException("Do not know to handle command = $command")
        }
    }

//      1
//    2 3 4
//  5 6 7 8 9
//    A B C
//      D

    fun decipherWithCrazyPinpad(input: String): String {
        val graph = mapOf(
            '1' to Node('1', down = '3'),
            '2' to Node('2', down = '6', right = '3'),
            '3' to Node('3', up = '1', right = '4', down = '7', left = '2'),
            '4' to Node('4', down = '8', left = '3'),
            '5' to Node('5', right = '6'),
            '6' to Node('6', up = '2', right = '7', down = 'A', left = '5'),
            '7' to Node('7', up = '3', right = '8', down = 'B', left = '6'),
            '8' to Node('8', up = '4', right = '9', down = 'C', left = '7'),
            '9' to Node('9', left = '8'),
            'A' to Node('A', up = '6', right = 'B'),
            'B' to Node('B',  up = '7', right= 'C', down = 'D', left = 'A'),
            'C' to Node('C', up = '8', left = 'B'),
            'D' to Node('D', up = 'B')
        )

        var currentDigit = '5'

        val horse = input.split("\n").map { digitCipherText ->
            digitCipherText.forEach { command ->
                currentDigit = nextDigitUsingGraph(graph, currentDigit, command)
            }

            currentDigit.toString()
        }

        return horse.joinToString("")
    }

    private fun nextDigitUsingGraph(graph: Map<Char, Node>, currentDigit: Char, command: Char): Char {
        val currentNode = graph[currentDigit] ?: throw RuntimeException("Unable to find node for digit = $currentDigit")

        val nextDigit = when(command) {
            'U' -> currentNode.up
            'R' -> currentNode.right
            'D' -> currentNode.down
            'L' -> currentNode.left
            else -> throw RuntimeException("Do not know how to deal with command = $command")
        }

        return nextDigit ?: currentDigit
    }
}

data class Node(val name: Char, val up: Char? = null, val right: Char? = null, val down: Char? = null, val left: Char? = null)