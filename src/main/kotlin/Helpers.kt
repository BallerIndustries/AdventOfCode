
fun<E> generateGrid(width: Int, height: Int, initialValue: E): MutableList<MutableList<E>> {
    return (0 until height).map { y -> (0 until width).map  { x -> initialValue }.toMutableList() }.toMutableList()
}