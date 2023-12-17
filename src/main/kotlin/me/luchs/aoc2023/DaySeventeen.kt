package me.luchs.aoc2023

import me.luchs.aoc2023.shared.Direction
import me.luchs.aoc2023.shared.Matrix
import me.luchs.aoc2023.shared.Point
import java.util.*
import kotlin.collections.HashSet

data class DaySeventeen(val input: String) : Day<Int> {

    private val matrix = Matrix(input)

    override fun partOne(): Int {
        val target = Point(matrix.maxRow(), matrix.maxColumn())
        return computeMinimalPath(State.initial(), target)
    }

    override fun partTwo(): Int {
        TODO("Not yet implemented")
    }

    fun computeMinimalPath(start: State, target: Point): Int {
        val queue = PriorityQueue<State>(compareBy { it.cost })
        val visited = HashSet<Triple<Point, Direction, Int>>()

        queue.add(start)

        while (queue.isNotEmpty()) {
            val current = queue.poll()

            if (Triple(current.position, current.direction, current.stepsInDirection) in visited) {
                continue
            }

            if (current.position == target) {
                return current.cost
            }

            visited.add(Triple(current.position, current.direction, current.stepsInDirection))

            queue.addAll(current.neighbours(matrix))

        }

        // If the open set is empty and the goal is not reached
        return 0
    }


    data class State(
        val cost: Int,
        val position: Point,
        val previous: State?,
        val direction: Direction,
        val stepsInDirection: Int
    ) {
        companion object {

            fun initial(): State =
                State(
                    cost = 0,
                    position = Point(0, 0),
                    previous = null,
                    direction = Direction.RIGHT,
                    stepsInDirection = 0
                )
        }

        fun neighbours(matrix: Matrix): List<State> =
            position.adjacent4WithDirection()
                .filterNot { it.value.row == previous?.position?.row && it.value.column == previous.position.column }
                .filterNot { matrix.valueAt(it.value) == null }
                .map {
                    State(
                        cost = cost + matrix.valueAt(it.value)!!.digitToInt(),
                        position = it.value,
                        previous = this,
                        direction = it.key,
                        stepsInDirection = if (it.key == direction) stepsInDirection + 1 else 1
                    )
                }
                .filterNot { it.stepsInDirection > 3 }

    }

}


