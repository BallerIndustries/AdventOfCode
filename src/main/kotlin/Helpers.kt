
fun<E> generateGrid(width: Int, height: Int, initialValue: E): MutableList<MutableList<E>> {
    return (0 until height).map { y -> (0 until width).map  { x -> initialValue }.toMutableList() }.toMutableList()
}

//fun <A, B>Iterable<A>.pmap(f: suspend (A) -> B): List<B> = runBlocking {
//    map { async { f(it) } }.map { it.await() }
//}