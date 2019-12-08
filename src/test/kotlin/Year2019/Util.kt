package Year2019

fun<T> List<T>.withoutElementAt(index: Int): List<T> {
    return this.filterIndexed { anotherIndex, _ -> index != anotherIndex }
}

fun<T> List<T>.permutations(): List<List<T>> {
    val initialList = this

    if (initialList.isEmpty()) {
        return listOf()
    }

    var wip = this.mapIndexed { index, element ->
        val listInProgress = listOf(element)
        val remainingList = initialList.withoutElementAt(index)
        listInProgress to remainingList
    }

    while (wip.first().second.isNotEmpty()) {
        wip = wip.flatMap { (listInProgress, remainingList) ->
            remainingList.mapIndexed { index, element ->
                val newListInProgress = listInProgress + element
                val remainingList = remainingList.withoutElementAt(index)
                newListInProgress to remainingList
            }
        }
    }

    return wip.map { it.first }
}