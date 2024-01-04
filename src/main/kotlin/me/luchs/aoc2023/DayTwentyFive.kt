package me.luchs.aoc2023

data class DayTwentyFive(val input: String) : Day<Int> {

    private val wiring: List<Pair<String, String>> = input.lines().flatMap { row ->
        val (from, to) = row.split(": ")
        to.split(" ").map { from to it }
    }

    override fun partOne(): Int {

        // create dot file style console output for visualization in external tool
        val dot = wiring.joinToString("\n") { "${it.first} -- ${it.second}" }
        println(dot)

        val graph = graphFromWiring(wiring)

        // compute minimum cut for graph into two disjunkt graphs
        // with 3 edges to remove (known from visualization)
        val (vertices1, vertices2) = graph.minimumCut(edgesToRemove = 3)

        return vertices1.size * vertices2.size
    }

    private fun graphFromWiring(wiring: List<Pair<String, String>>): Graph<String> {
        val vertices: Set<String> = wiring.flatMap { setOf(it.first, it.second) }.toSet()
        val edges: Map<String, Set<String>> = vertices.associateWith { node ->
            wiring.filter { it.first == node || it.second == node }
                .map { if (it.first == node) it.second else it.first }.toSet()
        }
        return Graph(vertices, edges)
    }

    fun Graph<String>.minimumCut(
        numberOfCrossings: Int = 50,
        edgesToRemove: Int
    ): Pair<Set<String>,Set<String>> {

        var canReach: Set<String>
        val crossingCounts = mutableMapOf<Pair<String, String>, Int>().withDefault { 0 }

        do { // while the graph is connected count crossings from one random vertex to others

            repeat(numberOfCrossings) {

                val v1 = this.vertices.random()
                val v2 = this.vertices.filter { it != v1 }.random()

                val shortestPathMap = this.dijkstra(v1)
                val path = shortestPath(shortestPathMap, v1, v2)

                for (p in 1..<path.size) {
                    val key = if (path[p - 1].compareTo(path[p]) == -1) path[p - 1] to path[p]
                    else path[p] to path[p - 1]
                    crossingCounts[key] = crossingCounts.getValue(key) + 1
                }

            }

            // edges with most crossings
            val mostCrossedEdges = crossingCounts.entries
                .sortedByDescending { it.value }
                .take(edgesToRemove)
                .map { it.key }

            // remove the edges that we are guessing make the min cut
            var reducedGraph: Graph<String> = this
            mostCrossedEdges.take(edgesToRemove).forEach {
                reducedGraph = reducedGraph.remove(edge = it)
            }

            // update the list of nodes that can be reached from a random vertex
            canReach = reducedGraph.reachableVertices(from = this.vertices.random())

        } while (canReach.size == this.vertices.size);

        // return disjunkt sets of vertices
        return canReach to this.vertices.minus(canReach)
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

        val fromEdges = reducedEdges[edge.first]?.toMutableSet() ?: mutableSetOf()
        fromEdges.remove(edge.second)
        reducedEdges[edge.first] = fromEdges.toSet()

        val toEdges = reducedEdges[edge.second]?.toMutableSet() ?: mutableSetOf()
        toEdges.remove(edge.first)
        reducedEdges[edge.second] = toEdges.toSet()

        return Graph(vertices, reducedEdges, weights)
    }

    fun reachableVertices(from: T): Set<T> {
        val shortestPathTree = this.dijkstra(from)
        return this.vertices.filter {
            if (from == it) return@filter true
            val path = shortestPath(shortestPathTree, from, it)
            path.size > 1
        }.toSet()
    }

    fun dijkstra(start: T): Map<T, T?> {

        val visited: MutableSet<T> = mutableSetOf()

        // map vertex to steps from start
        val delta = this.vertices.associateWith { Int.MAX_VALUE }.toMutableMap()
        // start node as minimal distance 0
        delta[start] = 0

        // map vertex
        val previous: MutableMap<T, T?> = this.vertices.associateWith { null }.toMutableMap()

        while (visited != this.vertices) {

            val current: T = delta
                .filterNot { it.key in visited }
                .minBy { it.value }
                .key

            // node is not connected to start node
            // -> return as all following nodes are also not connected!
            if (Int.MAX_VALUE == delta.getValue(current)) return previous.toMap()

            // update previous node map
            this.edges
                .getValue(current)
                .minus(visited)
                .forEach { neighbor ->

                    val newPath = delta.getValue(current) +
                            this.weights.getValue(Pair(current, neighbor))

                    if (newPath < delta.getValue(neighbor)) {
                        delta[neighbor] = newPath
                        previous[neighbor] = current
                    }

                }

            visited.add(current) // mark vertex as visited
        }

        return previous.toMap()
    }

}

fun <T> shortestPath(shortestPathTree: Map<T, T?>, start: T, end: T): List<T> {
    fun pathTo(start: T, end: T): List<T> {
        if (shortestPathTree[end] == null) return listOf(end)
        return listOf(pathTo(start, shortestPathTree[end]!!), listOf(end)).flatten()
    }
    return pathTo(start, end)
}
