package me.luchs.aoc2023.shared

fun <T> findCycleInTransitions(
    initial: T,
    transition: (T) -> T
): Pair<List<T>, Int> {

    var state: T = initial
    val transitions = mutableMapOf<T, T>()
    while (transitions.findCycle() == null) {
        state = transitions[state] ?: run {
            val next = transition(state)
            transitions[state] = next
            next
        }
    }

    // we know at this point that a cycle exists!
    val cycle = transitions.findCycle()!!

    val cycleStartIndex = transitions.keys.indexOf(cycle.first())

    return cycle to cycleStartIndex
}

fun <T> Map<T, T>.findCycle(): List<T>? {

    val visited = mutableSetOf<T>()

    fun dfs(node: T, path: MutableList<T>): List<T>? {
        if (node in visited) {
            // Cycle detected, extract the cycle path
            val startIndex = path.indexOf(node)
            return path.subList(startIndex, path.size)
        }

        visited.add(node)
        path.add(node)

        val neighbor = this[node]
        if (neighbor != null) {
            return dfs(neighbor, path)
        }

        path.removeAt(path.size - 1)
        return null
    }

    for (node in this.keys) {
        if (node !in visited) {
            val cycle = dfs(node, mutableListOf())
            if (cycle != null) {
                return cycle
            }
        }
    }

    return null
}
