package Year2020

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle23Test {
    val puzzleText = this::class.java.getResource("/2020/puzzle23.txt").readText().replace("\r", "")
    val puzzle = Puzzle23()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText, 100)
        assertEquals("29385746", result)
    }

    @Test
    fun `puzzle part b`() {
        val result = puzzle.solveTwo(puzzleText, 10000000)
        assertEquals(680435423892, result)
    }

    @Test
    fun `example part a1`() {
        val puzzleText = "389125467"
        val result = puzzle.solveOne(puzzleText, moves = 10)
        assertEquals("92658374", result)
    }

    @Test
    fun `example part a2`() {
        val puzzleText = "389125467"
        val result = puzzle.solveOne(puzzleText, moves = 100)
        assertEquals("67384529", result)
    }

    @Test
    fun `example part b`() {
        val puzzleText = "389125467"
        val result = puzzle.solveTwo(puzzleText, 10000000)
        assertEquals(149245887792, result)
    }
}

class Puzzle23 {

    data class Node(var next: Node?, val value: Int) {

        fun insertAfter(value: Int): Node {
            val newNode = Node(null, value)
            val next = this.next

            this.next = newNode
            newNode.next = next
            return newNode
        }

        fun insertAfter(subList: List<Node>) {
            val before = this
            val after = this.next

            val head = subList.first()
            val tail = subList.last()
            tail.next = after
            before.next = head
        }

        override fun toString(): String {
            return value.toString()
        }


    }

    fun solveOne(puzzleText: String, moves: Int): String {
        val cups = puzzleText.map { it.toString().toInt() }
        val head = Node( null, cups[0])
        head.next = head
        var tail = head

        (1 until cups.size).forEach {
            tail.insertAfter(cups[it])
            tail = tail.next!!
        }

        var currentCup = head
        val max = cups.max()!!

        (1 .. moves).forEach { moveNumber ->
            val threeCups: List<Node> = listOf(currentCup.next!!, currentCup.next!!.next!!, currentCup.next!!.next!!.next!!)
            val destinationCupNumber = getDestinationCupNumber(currentCup.value, threeCups.map { it.value }, max)

            println(" --- move $moveNumber --- ")
            println("cups: ${printCups(head, currentCup.value)}")
            println("pick up: ${threeCups.map { it.value }}")
            println("destination: $destinationCupNumber")


            currentCup.next = threeCups.last().next
            val destinationCup = getNode(head, destinationCupNumber)
            destinationCup.insertAfter(threeCups)
//            println(printCups(head))
//            println()

            currentCup = currentCup.next!!
        }

        val result = printCups2(getNode(head, 1))
        return result
    }
    private fun getNode(head: Node, target: Int): Node {
        var current = head

        while (current.value != target) {
            if (current.next == null) {
                println()
            }

            current = current.next!!
        }

        return current
    }

    private fun printCups(head: Node): String {
        return printCups(head, Int.MAX_VALUE)
    }

    private fun printCups(head: Node, currentCup: Int): String {
        val jur = mutableListOf(head.value)
        var current = head.next!!

        while (current.value != head.value) {
            jur.add(current.value)
            current = current.next!!
        }

        return jur.map { if (it == currentCup) "($it)" else it.toString() }.joinToString(" ")
    }

    private fun printCups2(head: Node): String {
        val jur = mutableListOf<Int>()
        var current = head.next!!

        while (current.value != head.value) {
            jur.add(current.value)
            current = current.next!!
        }


        return jur.joinToString("")
        //return jur.map { if (it == currentCup) "($it)" else it.toString() }.joinToString(" ")
    }

    private fun getDestinationCupNumber(currentCup: Int, threeCups: List<Int>, max: Int): Int {
        var destinationCupNum = currentCup - 1

        while (destinationCupNum in threeCups || destinationCupNum <= 0) {
            if (destinationCupNum <= 0) {
                destinationCupNum = max
            }
            else {
                destinationCupNum--
            }
        }

        return destinationCupNum
    }

    fun solveTwo(puzzleText: String, moves: Int): Long {
        val cups = puzzleText.map { it.toString().toInt() }
        val head = Node( null, cups[0])
        head.next = head
        var tail = head
        val numberToNode = mutableMapOf<Int, Node>(cups[0] to head)

        (1 until cups.size).forEach {
            val cupNumber = cups[it]
            val node = tail.insertAfter(cupNumber)
            tail = tail.next!!
            numberToNode[cupNumber] = node
        }

        var currentNumber = cups.max()!! + 1

        (0 until 1_000_000 - cups.size).forEach {
            val cupNumber = currentNumber
            val node = tail.insertAfter(cupNumber)
            tail = tail.next!!

            numberToNode[cupNumber] = node

            currentNumber++
        }



        println(currentNumber)
        val max = currentNumber - 1

        var currentCup = head

        (1 .. moves).forEach { moveNumber ->
            val threeCups: List<Node> = listOf(currentCup.next!!, currentCup.next!!.next!!, currentCup.next!!.next!!.next!!)
            val destinationCupNumber = getDestinationCupNumber(currentCup.value, threeCups.map { it.value }, max)

            if (moveNumber % 1000 == 0) {
                println(moveNumber)
            }

            currentCup.next = threeCups.last().next
            val destinationCup = numberToNode[destinationCupNumber] ?:
                throw RuntimeException()
            destinationCup.insertAfter(threeCups)
//            println(printCups(head))
//            println()

            currentCup = currentCup.next!!
        }

        val cupTwo = getNode(head, 1).next!!.value.toLong()
        val cupThree = getNode(head, 1).next!!.next!!.value.toLong()

        println(cupTwo)
        println(cupThree)

        return cupTwo * cupThree
    }
}

