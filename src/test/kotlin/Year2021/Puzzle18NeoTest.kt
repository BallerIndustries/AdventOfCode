package Year2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class Puzzle18NeoTest {
    val puzzleText = this::class.java.getResource("/2021/puzzle18.txt").readText().replace("\r", "")
    val puzzle = Puzzle18Neo()

    @Test
    fun `example part a`() {
        val result: Int = puzzle.solveOne("[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]\n" +
                "[[[5,[2,8]],4],[5,[[9,9],0]]]\n" +
                "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]\n" +
                "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]\n" +
                "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]\n" +
                "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]\n" +
                "[[[[5,4],[7,7]],8],[[8,3],8]]\n" +
                "[[9,3],[[9,9],[6,[4,9]]]]\n" +
                "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]\n" +
                "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]")
        assertEquals(4140, result)
    }

    @Test
    fun `puzzle part a`() {
        val result: Int = puzzle.solveOne(puzzleText)
        assertEquals(4140, result)
    }

    @Test
    fun `reduce and render 1`() {
        val result: String = puzzle.reduceAndRender("[[[[[9,8],1],2],3],4]")
        assertEquals("[[[[0,9],2],3],4]", result)
    }

    @Test
    fun `reduce and render 2`() {
        assertEquals("[7,[6,[5,[7,0]]]]", puzzle.reduceAndRender("[7,[6,[5,[4,[3,2]]]]]"))
    }

    @Test
    fun `reduce and render 3`() {
        assertEquals("[[6,[5,[7,0]]],3]", puzzle.reduceAndRender("[[6,[5,[4,[3,2]]]],1]"))
    }

    @Test
    fun `reduce and render 4`() {
        assertEquals("[[3,[2,[8,0]]],[9,[5,[7,0]]]]", puzzle.reduceAndRender("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"))
    }

    @Test
    fun `reduce and render 5`() {
        assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", puzzle.reduceAndRender("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]"))
    }

    @Test
    fun `reduce example part a 1`() {
        val result = puzzle.addAndRender("[1,1]\n" +
                "[2,2]\n" +
                "[3,3]\n" +
                "[4,4]")
        assertEquals("[[[[1,1],[2,2]],[3,3]],[4,4]]", result)
    }

    @Test
    fun `reduce example part a 2`() {
        val result = puzzle.addAndRender("[1,1]\n" +
                "[2,2]\n" +
                "[3,3]\n" +
                "[4,4]\n" +
                "[5,5]")
        assertEquals("[[[[3,0],[5,3]],[4,4]],[5,5]]", result)
    }

    @Test
    fun `reduce example part a 3`() {
        val result = puzzle.addAndRender("[1,1]\n" +
                "[2,2]\n" +
                "[3,3]\n" +
                "[4,4]\n" +
                "[5,5]\n" +
                "[6,6]")
        assertEquals("[[[[5,0],[7,4]],[5,5]],[6,6]]", result)
    }

    @Test
    fun `reduce example part a 4`() {
        val result = puzzle.addAndRender("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]\n" +
                "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]\n" +
                "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]\n" +
                "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]\n" +
                "[7,[5,[[3,8],[1,4]]]]\n" +
                "[[2,[2,2]],[8,[8,1]]]\n" +
                "[2,9]\n" +
                "[1,[[[9,3],9],[[9,0],[0,7]]]]\n" +
                "[[[5,[7,4]],7],1]\n" +
                "[[[[4,2],2],6],[8,7]]")
        assertEquals("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]", result)
    }

    @Test
    fun `parse line 1`() {
        val result = puzzle.parseLine("[1,2]")
        val n1 = Puzzle18Neo.Node(0, 1, null, null)
        val n2 = Puzzle18Neo.Node(1, 2, null, null)
        val n3 = Puzzle18Neo.Node(2, null, n1, n2)
        assertEquals(n3, result)
        assertEquals("[1,2]", puzzle.render(result))
    }

    @Test
    fun `parse line 2`() {
        val result = puzzle.parseLine("[[1,2],3]")
        val n1 = Puzzle18Neo.Node(0, 1, null, null)
        val n2 = Puzzle18Neo.Node(1, 2, null, null)
        val n3 = Puzzle18Neo.Node(2, null, n1, n2)
        val n4 = Puzzle18Neo.Node(3, 3, null, null)
        val n5 = Puzzle18Neo.Node(4, null, n3, n4)
        assertEquals(n5, result)
        assertEquals("[[1,2],3]", puzzle.render(result))
    }
}

class Puzzle18Neo {
    data class Node(
        val nodeId: Int,
        var value: Int?,
        var left: Node?,
        var right: Node?,
    )

    var nodeIdCounter = 0;

    fun parse(line: String, startIndex: Int): Pair<Node, Int> {
        var index = startIndex
        var left: Node? = null
        var right: Node? = null

        while (index < line.length) {
            val char = line[index];

            if (char == '[') {
                val (l, i1) = parse(line, index+1)
                val (r, i2) = parse(line, i1+1)

                left = l
                right = r
                index = i2
            }
            else if (char == ']') {
                val node = Node(
                    nodeIdCounter++,
                    null,
                    left,
                    right
                )

                return node to index
            }
            else if (char.isDigit()) {
                val node = Node(
                    nodeIdCounter++,
                    char.toString().toInt(),
                    null,
                    null,
                )

                return node to index
            }

            index += 1
        }

        throw RuntimeException("Should not have reached this line")
    }

    fun parseLine(line: String): Node {
        val head = parse(line, 0).first
        return head
    }

//    fun addAndRender(text: String): String {
//        val rows = text.split("\n").map { parseLine(it) }
//        val sum = addNumber(rows[0], rows[1])
//        reduce(sum)
//        return render(sum)
//    }

    private fun magnitude(node: Node): Int {
        if (node.value != null) {
            return node.value!!
        }

        return (magnitude(node.left!!) * 3) + (magnitude(node.right!!) * 2)
    }

    fun addAndRender(puzzleText: String): String {
        val sum = addNumbers(puzzleText)
        return render(sum)
    }

    fun solveOne(puzzleText: String): Int {
        val sum = addNumbers(puzzleText)
        return magnitude(sum)
    }

    private fun addNumbers(puzzleText: String): Node {
        val nodes = puzzleText.split("\n").map { parseLine(it) }
        var sum = nodes[0]

        for (index in 1 until nodes.count()) {
            sum = addNumber(sum, nodes[index])
            reduce(sum)
        }
        return sum
    }

    fun addNumber(left: Node, right: Node): Node {
        return Node(nodeIdCounter++, null, left, right)
    }

    fun reduce(head: Node) {
        println(render(head))

        while (true) {
            val heavilyNested = findHeavilyNested(head)

            if (heavilyNested != null) {
                explode(head, heavilyNested.left!!, heavilyNested.right!!)
                continue
            }

            val bigNumber = findBigNumber(head)

            if (bigNumber != null) {
                split(bigNumber)
                continue
            }

            break
        }
    }

    fun explode(head: Node, leftTarget: Node, rightTarget: Node) {
        val stack = Stack<Node>()
        var node: Node? = head
        var valueBefore: Node? = null
        var hitLeft = false
        var hitRight = false
        val leftValue = leftTarget.value!!
        val rightValue = rightTarget.value!!

        fun visit(node: Node): Boolean {
            if (node.nodeId == leftTarget.nodeId) {
                hitLeft = true

                // Add leftValue to valueBefore
                if (valueBefore != null) {
                    valueBefore!!.value = valueBefore!!.value!! + leftValue
                }
            }
            else if (node.nodeId == rightTarget.nodeId) {
                hitRight = true
            }
            else if (node.value != null && !hitLeft) {
                // Track the last value node before the target
                valueBefore = node
            }
            else if (node.value != null && hitRight) {
                // Add rightValue to node
                node!!.value = node!!.value!! + rightValue
                return true
            }

            return false
        }

        while (stack.isNotEmpty() || node != null) {
            if (node != null) {
                stack.push(node)
                node = node.left
            }
            else {
                node = stack.pop()

                if (visit(node)) {
                    break
                }

                node = node.right
            }
        }

        // Now zero out the parent of the target
        val parent = findParent(head, leftTarget)!!
        parent.value = 0
        parent.left = null
        parent.right = null
    }

    fun render(head: Node, buffer: StringBuffer = StringBuffer()): String {
        if (head.value != null) {
            buffer.append(head.value)
            return buffer.toString()
        }

        buffer.append('[')

        if (head.left != null) {
            render(head.left!!, buffer)
        }

        buffer.append(',')

        if (head.right != null) {
            render(head.right!!, buffer)
        }

        buffer.append(']')

        return buffer.toString()
    }

    private fun split(node: Node) {
        val value = node.value!!
        val left = value / 2;
        val right = (value / 2) + (value % 2)
        node.value = null
        node.left = Node(nodeIdCounter++, left, null, null)
        node.right = Node(nodeIdCounter++, right, null, null)

//        val leftValue = node.leftValue
//        val rightValue = node.rightValue
//
//        if (leftValue != null && leftValue >= 10 && node.left == null) {
//            node.leftValue = null
//            val l = leftValue / 2
//            val r = (leftValue / 2) + (leftValue % 2)
//            node.left = Node(nodeIdCounter++, null, l, null, r)
//        }
//        else if (rightValue != null && rightValue >= 10 && node.right == null) {
//            node.rightValue = null
//            val l = rightValue / 2
//            val r = (rightValue / 2) + (rightValue % 2)
//            node.right = Node(nodeIdCounter++, null, l, null, r)
//        }
//        else {
//            throw RuntimeException()
//        }
    }

    private fun findBigNumber(node: Node): Node? {
        if ((node.value ?: 0) >= 10) {
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

    private fun findParent(node: Node, target: Node): Node? {
        if (node.left?.nodeId == target.nodeId || node.right?.nodeId == target.nodeId) {
            return node
        }

        if (node.left != null) {
            val result = findParent(node.left!!, target)

            if (result != null) {
                return result;
            }
        }

        if (node.right != null) {
            val result = findParent(node.right!!, target)

            if (result != null) {
                return result;
            }
        }

        return null
    }

    private fun findHeavilyNested(node: Node, level: Int = 0): Node? {
        if (level == 4 && node.value == null) {
            return node;
        }

        if (node.left != null) {
            val result = findHeavilyNested(node.left!!, level + 1);

            if (result != null) {
                return result
            }
        }

        if (node.right != null) {
            val result = findHeavilyNested(node.right!!, level + 1);

            if (result != null) {
                return result
            }
        }

        return null
    }

    fun reduceAndRender(s: String): String {
        val node = parseLine(s);
        reduce(node)
        return render(node)
    }

//    private fun addNumber(left: Node, right: Node): Node {
//        return Node(nodeIdCounter++, left, null, right, null)
//    }
}

