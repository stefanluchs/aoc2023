package me.luchs.aoc2023

import me.luchs.aoc2023.shared.CharMatrix
import me.luchs.aoc2023.shared.Coordinate
import kotlin.math.max

data class DayTwentyThree(val input: String) : Day<Int> {

    private val map = CharMatrix(input)
    private val start = Coordinate(0, 1)
    private val target = Coordinate(map.maxRow, map.maxColumn - 1)

    override fun partOne(): Int {

        val graph = buildGraph(
            addResult = { legStartNode, nodes -> add(Leg(legStartNode.first, nodes)) },
            process = { visited, nodes, queue ->
                forEach {
                    if (it.value.isSlope()) {

                        val next = when (it.value) {
                            '^' -> it.key.up()
                            '>' -> it.key.right()
                            'v' -> it.key.down()
                            '<' -> it.key.left()
                            else -> throw IllegalArgumentException("Can not process slope ${it.value}")
                        }
                        val value = map[next]

                        if (value != null && value != '#' && next !in visited) {
                            visited += it.key
                            nodes += it.key
                            queue.addFirst(next)
                        }
                    } else {
                        queue.addFirst(it.key)
                    }
                }
            }
        )

        return findMaxSteps(graph, start, target)
    }

    override fun partTwo(): Int {

        val graph = buildGraph(
            addResult = { legStartNode, nodes ->
                add(Leg(legStartNode.first, nodes, invert = false))
                add(Leg(legStartNode.first, nodes, invert = true))
            },
            process = { _, _, queue ->
                forEach { queue.addFirst(it.key) }
            }
        )

        return findMaxSteps(graph, start, target)
    }

    private fun buildGraph(
        addResult: MutableList<Leg>.(
            legStartNode: Pair<Coordinate, Coordinate>,
            nodes: MutableList<Coordinate>
        ) -> Unit,
        process: Map<Coordinate, Char>.(
            visited: MutableList<Coordinate>,
            nodes: MutableList<Coordinate>,
            queue: MutableList<Coordinate>
        ) -> Unit
    ): Map<Coordinate, Set<Pair<Coordinate, Int>>> {

        val intersections = mutableSetOf<Coordinate>()
        val legStartNodes = mutableListOf(start to start.down())

        val legs = buildList {
            while (legStartNodes.isNotEmpty()) {

                val legStartNode = legStartNodes.removeFirst()
                val visited = mutableListOf(legStartNode.first)

                val nodes = mutableListOf<Coordinate>()
                val queue = mutableListOf(legStartNode.second)

                while (queue.isNotEmpty()) {

                    val position = queue.removeFirst()

                    if (position in nodes) {
                        continue
                    }

                    visited += position
                    nodes += position

                    if (position == target) {
                        // dead end at target -> finish leg
                        break
                    }

                    if (position in intersections) {
                        break
                    }

                    val neighbours = map.adjacent4(position)
                        .filterNot { it.value == '#' }
                        .filterNot { it.key in visited }

                    if (neighbours.size > 1 && position !in intersections) {
                        // intersection -> finish leg and add new leg start nodes
                        legStartNodes.addAll(neighbours.keys.map { position to it })
                        intersections += position
                        break
                    }

                    if (neighbours.isEmpty()) {
                        // dead end at target -> finish leg
                        break
                    }

                    neighbours.process(visited, nodes, queue)

                }

                addResult(legStartNode, nodes)

            }
        }

        return legs
            .groupBy { it.from }
            .map { it.key to it.value.map { it.to to it.length }.toSet() }
            .associate { it.first to it.second }
    }


    private fun findMaxSteps(
        graph: Map<Coordinate, Set<Pair<Coordinate, Int>>>,
        current: Coordinate,
        target: Coordinate,
        seen: MutableMap<Coordinate, Boolean> = mutableMapOf()
    ): Int {

        if (current == target) {
            // Found destination
            return 0
        } else if (seen[current] == true) {
            // Cannot reach same coordinate twice
            return -1
        }

        seen[current] = true

        var maxSteps = -1

        for ((neighbour, length) in graph[current] ?: emptyList()) {
            val steps = findMaxSteps(graph, neighbour, target, seen)
            if (steps >= 0) {
                maxSteps = max(maxSteps, steps + length)
            }
        }

        seen[current] = false

        return maxSteps
    }

    private fun Char.isSlope(): Boolean = this in listOf('^', '>', 'v', '<')

    data class Leg(val from: Coordinate, val length: Int, val to: Coordinate) {

        companion object {
            operator fun invoke(
                from: Coordinate,
                nodes: List<Coordinate>,
                invert: Boolean = false
            ): Leg =
                if (invert) Leg(nodes.last(), nodes.size, from)
                else Leg(from, nodes.size, nodes.last())
        }

        override fun toString(): String {
            return "$from .. $length .. $to"
        }

    }

}
