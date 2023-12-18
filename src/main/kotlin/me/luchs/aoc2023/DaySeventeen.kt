package me.luchs.aoc2023

import me.luchs.aoc2023.shared.Direction
import me.luchs.aoc2023.shared.Matrix
import me.luchs.aoc2023.shared.Point
import java.util.*
import kotlin.collections.HashSet

data class DaySeventeen(val input: String) : Day<Int> {

    private val matrix = Matrix(input)

    private val target = Point(matrix.maxRow(), matrix.maxColumn())

    override fun partOne(): Int {
        return computeMinimalPath(
            start = State.initial(),
            target = { it.position == target },
            neighbours = { state, matrix -> state.neighboursPart1(matrix) }
        )
    }

    override fun partTwo(): Int {
        return computeMinimalPath(
            start = State.initial(stepsInDirection = 1),
            target = { it.position == target && it.stepsInDirection >= 4 },
            neighbours = { state, matrix -> state.neighboursPart2(matrix) }
        )
    }

    private fun computeMinimalPath(
        start: State,
        target: (State) -> Boolean,
        neighbours: (State, Matrix) -> List<State>
    ): Int {

        val queue = PriorityQueue<State>(compareBy { it.cost })
        val visited = HashSet<Triple<Point, Direction, Int>>()

        queue.add(start)

        while (queue.isNotEmpty()) {
            val current = queue.poll()

            if (Triple(current.position, current.direction, current.stepsInDirection) in visited) {
                continue
            }

            if (target(current)) {
                return current.cost
            }

            visited.add(Triple(current.position, current.direction, current.stepsInDirection))

            queue.addAll(neighbours(current, matrix))

        }

        // no result found
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

            fun initial(stepsInDirection: Int = 0): State =
                State(
                    cost = 0,
                    position = Point(0, 0),
                    previous = null,
                    direction = Direction.RIGHT,
                    stepsInDirection = stepsInDirection
                )
        }

        fun neighboursPart1(matrix: Matrix): List<State> =
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

        fun neighboursPart2(matrix: Matrix): List<State> {

            val next = mutableListOf<Triple<Point, Direction, Int>>()

            if (stepsInDirection < 4) {
                next.add(moveForward())
            } else if (stepsInDirection >= 10) {
                next.add(moveLeft())
                next.add(moveRight())
            } else {
                next.add(moveForward())
                next.add(moveLeft())
                next.add(moveRight())
            }

            return next
                .filterNot { matrix.valueAt(it.first) == null }
                .map { (position, direction, steps) ->
                    State(
                        cost = cost + matrix.valueAt(position)!!.digitToInt(),
                        position = position,
                        previous = this,
                        direction = direction,
                        stepsInDirection = steps
                    )
                }
        }

        private fun moveForward() = Triple(
            this.position.move(this.direction),
            this.direction,
            this.stepsInDirection + 1
        )

        private fun moveLeft() = Triple(
            this.position.move(this.direction.turnLeft()),
            this.direction.turnLeft(),
            1
        )

        private fun moveRight() = Triple(
            this.position.move(this.direction.turnRight()),
            this.direction.turnRight(),
            1
        )

    }

}


