package Year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Puzzle18Test {
    val puzzleText = this::class.java.getResource("/2021/puzzle18.txt").readText().replace("\r", "")
    val puzzle = Puzzle18()

    @Test
    fun `puzzle part a`() {
        val result = puzzle.solveOne(puzzleText)
        assertEquals(11288669, result)
    }

    @Test
    fun `example part a 1`() {
        val result = puzzle.solveOne("[1,1]\n" +
                "[2,2]\n" +
                "[3,3]\n" +
                "[4,4]\n" +
                "[5,5]\n" +
                "[6,6]")
        assertEquals(11288669, result)
    }

    @Test
    fun `example part a 2`() {
        val result = puzzle.addAndRender("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]\n" +
                "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]")
        assertEquals("[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]", result)
    }

    @Test
    fun `example part a 3`() {
        val result = puzzle.addAndRender("[[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]]\n" +
                "[2,9]")
        assertEquals("", result)
    }







    @Test
    fun `parse line 1`() {
        val result = puzzle.parseLine("[1,2]")
        puzzle.render(result)
        assertEquals(Puzzle18.Node(0, null, 1, null, 2), result)
    }

    @Test
    fun `parse line 2`() {
        val result = puzzle.parseLine("[[1,2],3]")
        println(puzzle.render(result))

        val sub = Puzzle18.Node(0, null, 1, null, 2)
        val node = Puzzle18.Node(1, sub, null, null, 3)
        assertEquals(node, result)
    }
}

class Puzzle18 {
    data class Node(
        val nodeId: Int,
        var left: Node?,
        var leftValue: Int?,
        var right: Node?,
        var rightValue: Int?
    )

    var nodeIdCounter = 0;

    fun parse(line: String, startIndex: Int): Pair<Node, Int> {
        var index = startIndex
        var hitComma = false
        var leftValue: Int? = null
        var leftNode: Node? = null
        var rightValue: Int? = null
        var rightNode: Node? = null

        while (index < line.length) {
            val char = line[index];

            if (char == '[') {
                val (node, newIndex) = parse(line, index+1)

                if (hitComma) {
                    rightNode = node
                }
                else {
                    leftNode = node
                }

                index = newIndex
            }
            else if (char == ',') {
                hitComma = true
            }
            else if (char == ']') {
                val node = Node(nodeIdCounter++, leftNode, leftValue, rightNode, rightValue)
                return node to index
            }
            else if (char.isDigit()) {
                if (hitComma) {
                    rightValue = char.toString().toInt()
                }
                else {
                    leftValue = char.toString().toInt()
                }
            }

            index += 1
        }

        throw RuntimeException("HEY!");
    }

    fun parseLine(line: String): Node {
        return parse(line, 1).first;
    }

    fun addAndRender(text: String): String {
        val rows = text.split("\n").map { parseLine(it) }
        val sum = addNumber(rows[0], rows[1])
        reduce(sum)
        return render(sum)
    }

    fun solveOne(puzzleText: String): Int {
        val nodes = puzzleText.split("\n").map { parseLine(it) }
        var sum = nodes[0]

        for (index in 1 until nodes.count()) {
            sum = addNumber(sum, nodes[index])
            reduce(sum)
            println(render(sum))
        }


        println()

        TODO("Not yet implemented")
    }

    private fun reduce(head: Node) {
        println(render(head))

        while (true) {
            val heavilyNested = findHeavilyNested(head)

            if (heavilyNested != null) {
                println("Going to explode $heavilyNested")
                //explode_v2(head, heavilyNested)
                println(render(head))
                println()
                continue
            }

            val bigNumber = findBigNumber(head)

            if (bigNumber != null) {
                println("Going to split $bigNumber")
                split(bigNumber)
                println(render(head))
                println()
                continue
            }

            break
        }
    }

    fun render(head: Node, buffer: StringBuffer = StringBuffer()): String {
        buffer.append('[')

        if (head.leftValue != null) {
            buffer.append(head.leftValue)
        }
        else {
            render(head.left!!, buffer)
        }

        buffer.append(',')

        if (head.rightValue != null) {
            buffer.append(head.rightValue)
        }
        else {
            render(head.right!!, buffer)
        }

        buffer.append(']')
        return buffer.toString()
    }


    private fun findParent(node: Node, target: Node): Node? {
        if (node.left?.nodeId == target.nodeId || node.right?.nodeId == target.nodeId) {
            return node
        }

        if (node.left != null) {
            val result = findParent(node.left!!, target)

            if (result != null) {
                return result
            }
        }

        if (node.right != null) {
            val result = findParent(node.right!!, target)

            if (result != null) {
                return result
            }
        }

        return null

    }

    private fun split(node: Node) {
        val leftValue = node.leftValue
        val rightValue = node.rightValue

        if (leftValue != null && leftValue >= 10 && node.left == null) {
            node.leftValue = null
            val l = leftValue / 2
            val r = (leftValue / 2) + (leftValue % 2)
            node.left = Node(nodeIdCounter++, null, l, null, r)
        }
        else if (rightValue != null && rightValue >= 10 && node.right == null) {
            node.rightValue = null
            val l = rightValue / 2
            val r = (rightValue / 2) + (rightValue % 2)
            node.right = Node(nodeIdCounter++, null, l, null, r)
        }
        else {
            throw RuntimeException()
        }
    }

    private fun findBigNumber(node: Node): Node? {
        if ((node.leftValue ?: 0) >= 10) {
            return node
        }
        if ((node.rightValue ?: 0) >= 10) {
            return node
        }

        if (node.left != null) {
            val result = findBigNumber(node.left!!)

            if (result != null) {
                return result
            }
        }

        if (node.right != null) {
            val result = findBigNumber(node.right!!)

            if (result != null) {
                return result
            }
        }

        return null
    }

    private fun findHeavilyNested(node: Node, level: Int = 0): Node? {
        if (level == 4) {
            return node;
        }

        if (node.left != null) {
            val result = findHeavilyNested(node.left!!, level + 1);

            if (result != null) {
                return result;
            }
        }

        if (node.right != null) {
            val result = findHeavilyNested(node.right!!, level + 1);

            if (result != null) {
                return result;
            }
        }

        return null
    }

    private fun addNumber(left: Node, right: Node): Node {
        return Node(nodeIdCounter++, left, null, right, null)
    }
}

