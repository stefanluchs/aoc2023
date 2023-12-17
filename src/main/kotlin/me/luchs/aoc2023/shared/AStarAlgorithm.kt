package me.luchs.aoc2023.shared

import java.util.PriorityQueue

data class AStarAlgorithm(
    val start: Node,
    val goal: Node,
    val g: (Node) -> Long = { it.g + 1 },
    val h: (Node) -> Long = { node -> node.point.manhattanDistanceTo(goal.point) },
    val neighbours: (Node) -> List<Point> = { it.point.adjacent4() }
) {

    companion object {
        operator fun invoke(start: Point, goal: Point): AStarAlgorithm {
            return AStarAlgorithm(Node(start), Node(goal))
        }
    }

    fun computeMinimalPath(): List<Node> {
        val queue = PriorityQueue<Node>(compareBy { it.f() })
        val closedSet = HashSet<Node>()

        queue.add(start)

        while (queue.isNotEmpty()) {
            val current = queue.poll()

            if (current.point == goal.point) {
                return reconstructPath(current)
            }

            closedSet.add(current)

            for (neighbor in neighbours(current).map { Node(it) }) {
                if (neighbor in closedSet) {
                    continue
                }

                val tentativeG = g(current)

                if (neighbor !in queue || tentativeG < neighbor.g) {
                    neighbor.parent = current
                    neighbor.g = tentativeG
                    neighbor.h = h(neighbor)

                    if (neighbor !in queue) {
                        queue.add(neighbor)
                    }
                }
            }
        }

        // If the open set is empty and the goal is not reached
        return emptyList()
    }

    private fun reconstructPath(current: Node): List<Node> {
        val path = mutableListOf<Node>()
        var currentPath = current

        while (currentPath.parent != null) {
            path.add(currentPath)
            currentPath = currentPath.parent!!
        }

        path.add(start)
        return path.reversed()
    }

    /**
     * Represents a node in a graph.
     *
     * @param point The point associated with this node.
     * @param g The movement cost from start node to this node.
     * @param h The heuristic cost from this node to the goal node.
     * @param parent The parent node of this node.
     */
    data class Node(val point: Point, var g: Long = 0, var h: Long = 0, var parent: Node? = null) {
        fun f(): Long = g + h
    }
}
