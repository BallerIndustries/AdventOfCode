package Year2016

class Puzzle8 {
    var board = createBoard()

    private fun createBoard(): MutableList<MutableList<Char>> {
        return mutableListOf(
            (1..50).map { '.' }.toMutableList(),
            (1..50).map { '.' }.toMutableList(),
            (1..50).map { '.' }.toMutableList(),
            (1..50).map { '.' }.toMutableList(),
            (1..50).map { '.' }.toMutableList(),
            (1..50).map { '.' }.toMutableList()
        )
    }

    fun sendCommand(command: String) {
        val parts = command.split(' ')

        when (parts[0]) {
            "rect" -> {
                val (x, y) = parts[1].split('x').map { it.toInt() }
                handleRectCommand(x, y)
            }
            "rotate" -> {
                val isColumn = parts[1] == "column"

                if (isColumn) {
                    val column = parts[2].replace("x=", "").toInt()
                    val shiftAmount = parts[4].toInt()
                    rotateColumn(column, shiftAmount)
                }
                else {
                    val row = parts[2].replace("y=", "").toInt()
                    val shiftAmount = parts[4].toInt()
                    rotateRow(row, shiftAmount)
                }
            }
            else -> throw RuntimeException("$${parts[0]} command is not supported")
        }
    }

    private fun rotateRow(rowNumber: Int, shiftAmount: Int) {
        val row = board.get(rowNumber)
        val buffer = row.map { ' ' }.toMutableList()

        row.forEachIndexed { index, c ->
            val shiftedIndex = (index + shiftAmount) % row.size
            buffer.set(shiftedIndex, c)
        }

        buffer.forEachIndexed { index, c -> setPixel(index, rowNumber, c)}
    }

    private fun rotateColumn(columnNumber: Int, shiftAmount: Int) {
        val column = board.map { it.get(columnNumber) }
        val buffer = column.map { ' ' }.toMutableList()

        column.forEachIndexed { index, c ->
            val shiftedIndex = (index + shiftAmount) % column.size
            buffer.set(shiftedIndex, c)
        }

        buffer.forEachIndexed { index, c -> setPixel(columnNumber, index, c)}
    }

    private fun handleRectCommand(width: Int, height: Int) {
        (0 until width).forEach { x -> (0 until height).forEach { y -> setPixel(x, y, '#')}}
    }

    private fun setPixel(x: Int, y: Int, c: Char) {
        board.get(y).set(x, c)
    }

    override fun toString(): String {
        return board.map { it.joinToString("") }.joinToString("\n")
    }

    fun countLitPixels(): Int {
        return toString().count { it == '#' }
    }
}