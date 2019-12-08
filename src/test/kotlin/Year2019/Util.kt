package Year2019

fun<T> List<T>.permutations(): List<List<T>> {
    val initialList = this

    if (initialList.isEmpty()) {
        return listOf()
    }

    fun listWithoutElementAtIndex(list: List<T>, index: Int): List<T> {
        return list.filterIndexed { anotherIndex, _ -> index != anotherIndex }
    }

    var wip = this.mapIndexed { index, element ->
        val listInProgress = listOf(element)
        val remainingList = listWithoutElementAtIndex(initialList, index)
        listInProgress to remainingList
    }

    while (wip.first().second.isNotEmpty()) {
        wip = wip.flatMap { (listInProgress, remainingList) ->
            remainingList.mapIndexed { index, element ->
                val newListInProgress = listInProgress + element
                val remainingList = listWithoutElementAtIndex(remainingList, index)
                newListInProgress to remainingList
            }
        }
    }

    return wip.map { it.first }
}