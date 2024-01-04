package me.luchs.aoc2023

import me.luchs.aoc2023.shared.distinctPairs
import kotlin.time.times

data class DayTwentyFive(val input: String) : Day<Int> {

    val wiring = input.lines().flatMap {
        val (from, to) = it.split(": ")
        to.split(" ").map { from to it }
    }

    override fun partOne(): Int {

        val dot = wiring.map { "${it.first} -- ${it.second}" }.joinToString("\n")
        println(dot)

        val vertices: Set<String> = wiring.flatMap { setOf(it.first, it.second) }.toSet()

        val edges: Map<String, Set<String>> = vertices.associateWith { node ->
            wiring.filter { it.first == node || it.second == node }
                .map { if (it.first == node) it.second else it.first }.toSet()
        }

        val graph = Graph(vertices, edges)

        val (_, group1) = minimumCut(graph)

        // https://www.reddit.com/r/adventofcode/comments/18qbsxs/comment/kftp4jr/?utm_source=share&utm_medium=web3x&utm_name=web3xcss&utm_term=1&utm_content=share_button

        return group1.size * (graph.vertices.size - group1.size)
    }

    // https://github.com/chriswaters78/AdventOfCode2023/blob/main/2023_25/CountCrossings.cs
    fun minimumCut(
        graph: Graph<String>,
        noCrossings: Int = 50,
        k: Int = 3
    ): Pair<Int, List<String>> {

        var canReach = setOf<String>()
        val crossingCounts = mutableMapOf<Pair<String, String>, Int>().withDefault { 0 }

        do {

            repeat(noCrossings) {

                var v1 = graph.vertices.random()
                var v2 = graph.vertices.filter { it != v1 }.random()

                val shortestPathTree = graph.dijkstra(v1)
                val path = shortestPath(shortestPathTree, v1, v2)

                for (p in 1..<path.size) {
                    val key = if (path[p - 1].compareTo(path[p]) == -1) path[p - 1] to path[p]
                    else path[p] to path[p - 1]
                    crossingCounts[key] = crossingCounts.getValue(key) + 1
                }

            }

            val topK = crossingCounts.entries.sortedByDescending { it.value }.take(k)

            //remove the 3 edges that we are guessing make the min cut

            var reducedGraph: Graph<String> = graph
            topK.take(k).forEach {
                reducedGraph = reducedGraph.remove(it.key)
            }

            canReach = reducedGraph.canReach(graph.vertices.random())

            //canReach = Graph.Reachable(g2, graph.Keys.First());
        } while (canReach.size == graph.vertices.size);

        return k to canReach.toList()
    }

    override fun partTwo(): Int {
        TODO("Not yet implemented")
    }

}


data class Graph<T>(
    val vertices: Set<T>,
    val edges: Map<T, Set<T>>,
    val weights: Map<Pair<T, T>, Int> = mapOf<Pair<T, T>, Int>().withDefault { 1 }
) {

    fun remove(edge: Pair<T, T>): Graph<T> {
        val reducedEdges = edges.toMutableMap()

        val fromEdges = reducedEdges[edge.first]!!.toMutableSet()
        fromEdges.remove(edge.second)
        reducedEdges[edge.first] = fromEdges.toSet()

        val toEdges = reducedEdges[edge.second]!!.toMutableSet()
        toEdges.remove(edge.first)
        reducedEdges[edge.second] = toEdges.toSet()

        return Graph(vertices, reducedEdges, weights)
    }

}

fun <T> Graph<T>.canReach(from: T): Set<T> {
    val shortestPathTree = this.dijkstra(from)
    return this.vertices.filter {
        if (from == it) return@filter true
        val path = shortestPath(shortestPathTree, from, it)
        path.size > 1
    }.toSet()
}

fun <T> Graph<T>.dijkstra(start: T): Map<T, T?> {
    val S: MutableSet<T> =
        mutableSetOf() // a subset of vertices, for which we know the true distance

    val delta = this.vertices.map { it to Int.MAX_VALUE }.toMap().toMutableMap()
    delta[start] = 0

    val previous: MutableMap<T, T?> = this.vertices.associateWith { null }.toMutableMap()

    while (S != this.vertices) {
        val v: T = delta
            .filter { !S.contains(it.key) }
            .minBy { it.value }!!
            .key

        // node is not connected to start!
        if (Int.MAX_VALUE == delta.getValue(v)) return previous.toMap()

        this.edges.getValue(v).minus(S).forEach { neighbor ->
            val newPath = delta.getValue(v) + this.weights.getValue(Pair(v, neighbor))

            if (newPath < delta.getValue(neighbor)) {
                delta[neighbor] = newPath
                previous[neighbor] = v
            }
        }

        S.add(v)
    }

    return previous.toMap()
}

fun <T> shortestPath(shortestPathTree: Map<T, T?>, start: T, end: T): List<T> {
    fun pathTo(start: T, end: T): List<T> {
        if (shortestPathTree[end] == null) return listOf(end)
        return listOf(pathTo(start, shortestPathTree[end]!!), listOf(end)).flatten()
    }

    return pathTo(start, end)
}
