package me.luchs.aoc2023

import me.luchs.aoc2023.shared.lcm

data class DayEight(val input: String) : Day<Long> {

    override fun partOne(): Long {
        return Network(input).pathLength()
    }

    override fun partTwo(): Long {
        return Network(input).lcmPathLength()
    }

    data class Network(val nodes: Map<String, Pair<String, String>>, val instructions: CharArray) {
        companion object {
            operator fun invoke(input: String): Network {
                val lines = input.lines()
                val instructions = lines.first().toCharArray()
                val map =
                    lines.drop(2).associate { line ->
                        val (key, values) = line.split(" = ")
                        val map =
                            values
                                .split(", ")
                                .map { it.removeSuffix(")") }
                                .map { it.removePrefix("(") }
                                .zipWithNext()
                                .first()
                        key to map
                    }
                return Network(map, instructions)
            }
        }

        /**
         * Calculates the length of a path from a start node to an exit node.
         *
         * @param start The starting node. Defaults to "AAA".
         * @param exitCriteria A lambda function that evaluates whether a given node is the exit node.
         * Defaults to checking if the node is "ZZZ".
         * @return The length of the path in the form of a long value.
         */
        fun pathLength(
            start: String = "AAA",
            exitCriteria: (String) -> Boolean = { next -> "ZZZ" == next }
        ): Long {
            var next = start
            var node: Pair<String, String> = nodes[next]!!
            var steps = 1L
            while (true) {
                instructions.forEach { instruction ->
                    next =
                        when (instruction) {
                            'L' -> node.first
                            'R' -> node.second
                            else -> throw IllegalStateException("Can not use instruction $instruction")
                        }
                    if (exitCriteria(next)) return@forEach
                    node = nodes[next]!!
                    steps++
                }
                if (exitCriteria(next)) break
            }
            return steps
        }

        /**
         * Calculates the least common multiple (LCM) of the path lengths from all nodes ending with 'A'
         * to the nodes ending with 'Z'.
         *
         * @return The LCM of the path lengths as a Long value.
         */
        fun lcmPathLength(): Long {
            return nodes.keys
                .filter { it.endsWith('A') }
                .map { pathLength(start = it, exitCriteria = { s -> s.endsWith('Z') }) }
                .reduce { left, right -> lcm(left, right) }
        }
    }
}
