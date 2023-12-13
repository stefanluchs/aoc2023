package me.luchs.aoc2023.shared

/**
 * Returns a set of distinct pairs of elements of the list.
 *
 * @return the set of distinct pairs where each pair consists of two distinct elements from the
 * list.
 *
 * @param T the type of elements in the list.
 */
fun <T> List<T>.distinctPairs(): Set<Pair<T, T>> {
    val distinctPairs = mutableSetOf<Pair<T, T>>()
    for (i in 0 until this.size - 1) {
        for (j in i + 1 until this.size) {
            val pair = Pair(this[i], this[j])
            distinctPairs.add(pair)
        }
    }
    return distinctPairs
}
