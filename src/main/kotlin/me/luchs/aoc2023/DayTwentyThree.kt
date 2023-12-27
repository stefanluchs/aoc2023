package me.luchs.aoc2023

import me.luchs.aoc2023.shared.CharMatrix
import me.luchs.aoc2023.shared.Coordinate

data class DayTwentyThree(val input: String) : Day<Int> {

    private val map = CharMatrix(input)
    private val start = Coordinate(0, 1)
    private val target = Coordinate(map.maxRow, map.maxColumn - 1)

    override fun partOne(): Int {
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

                    neighbours.forEach {
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

                add(Leg(legStartNode.first, nodes))

            }
        }

        val targetLeg = legs.find { target in it.nodes }!!

        val paths = mutableListOf(listOf(targetLeg))

        val finished = buildList<List<Leg>> {
            while (paths.isNotEmpty()) {
                val path = paths.removeFirst()
                val next = legs.filter { path.last().intersection in it.nodes }
                    .map { path.toMutableList() + it }

                if (next.isEmpty()) {
                    add(path)
                    continue
                }

                paths.addAll(next)
            }
        }

        val lenghts = finished.map { it.sumOf { it.nodes.size } }.sorted()

        return lenghts.maxOf { it }
    }


    private fun Char.isSlope(): Boolean = this in listOf('^', '>', 'v', '<')

    override fun partTwo(): Int {
        TODO("Not yet implemented")
    }

    data class Leg(val intersection: Coordinate, val nodes: List<Coordinate>) {
        override fun toString(): String {
            return "$intersection: ${nodes.first()} .. ${nodes.size} .. ${nodes.last()}"
        }
    }

}

