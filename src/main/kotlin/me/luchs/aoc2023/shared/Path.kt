package me.luchs.aoc2023.shared

import java.util.*
import kotlin.collections.ArrayDeque


class Path<N>(val destination: N, val pathLength: Int, val parent: Path<N>?) : Comparable<Path<N>> {
    val nodeCount: Int by lazy { if (parent == null) 1 else parent.nodeCount + 1 }

    override fun compareTo(other: Path<N>): Int {
        return this.pathLength.compareTo(other.pathLength)
    }

    operator fun contains(node: N): Boolean {
        return any { it == node }
    }

    inline fun any(where: (N) -> Boolean): Boolean {
        var current: Path<N>? = this
        while (current != null) {
            if (where.invoke(current.destination)) return true
            current = current.parent
        }
        return false
    }

    inline fun nodes(where: (N) -> Boolean): Collection<N> {
        val result = ArrayDeque<N>()
        any {
            if (where(it)) result.addFirst(it)
            false
        }
        return result
    }

    val allNodes: Collection<N>
        get() {
            return nodes { true }
        }
}

/**
 * Dijkstra's algorithm to find the shortest path between weighted nodes.
 */
inline fun <N, T> shortestWeightedPath(
    from: N,
    neighbours: (Path<N>) -> Collection<Pair<N, Int>>,
    process: (Path<N>) -> T?
): T? {
    val pending = PriorityQueue<Path<N>>()
    pending.add(Path(from, 0, null))
    val settled = mutableSetOf<N>()
    while (true) {
        val current = pending.poll() ?: break
        if (settled.contains(current.destination)) continue
        process(current)?.let {
            return it
        }
        val currentNode = current.destination
        settled.add(currentNode)
        for ((neighbour, neighbourWeight) in neighbours(current)) {
            val newDistance = current.pathLength + neighbourWeight
            pending.add(Path(neighbour, newDistance, current))
        }
    }
    return null
}