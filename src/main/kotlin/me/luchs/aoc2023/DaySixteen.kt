package me.luchs.aoc2023

import me.luchs.aoc2023.shared.Direction4
import me.luchs.aoc2023.shared.Matrix
import me.luchs.aoc2023.shared.Point

data class DaySixteen(val input: String) : Day<Int> {
    override fun partOne(): Int {

        val matrix = Matrix(input)

        var state = State(
            path = mutableSetOf(),
            beams = mutableListOf(Beam(position = Point(0, -1), direction = Direction4.RIGHT))
        )

        while (state.beams.isNotEmpty()) {
            state = state.next(matrix)
        }

        val energized = state.path.map { it.position }.distinct()

        return energized.size
    }

    override fun partTwo(): Int {
        TODO("Not yet implemented")
    }

    data class Beam(val position: Point, val direction: Direction4) {

        fun next(matrix: Matrix): List<Beam> {

            val next = position.move(direction)

            val node = matrix.position(next) ?: return emptyList()

            return when (node.value!!) {
                '|' -> {
                    if (direction in listOf(Direction4.LEFT, Direction4.RIGHT)) {
                        listOf(Beam(next, Direction4.DOWN), Beam(next, Direction4.UP))
                    } else {
                        listOf(Beam(next, direction))
                    }
                }

                '-' -> {
                    if (direction in listOf(Direction4.UP, Direction4.DOWN)) {
                        listOf(Beam(next, Direction4.LEFT), Beam(next, Direction4.RIGHT))
                    } else {
                        listOf(Beam(next, direction))
                    }
                }

                '\\' -> {
                    when (direction) {
                        Direction4.UP -> Beam(next, Direction4.LEFT)
                        Direction4.DOWN -> Beam(next, Direction4.RIGHT)
                        Direction4.LEFT -> Beam(next, Direction4.UP)
                        Direction4.RIGHT -> Beam(next, Direction4.DOWN)
                    }.let { listOf(it) }
                }

                '/' -> {
                    when (direction) {
                        Direction4.UP -> Beam(next, Direction4.RIGHT)
                        Direction4.DOWN -> Beam(next, Direction4.LEFT)
                        Direction4.LEFT -> Beam(next, Direction4.DOWN)
                        Direction4.RIGHT -> Beam(next, Direction4.UP)
                    }.let { listOf(it) }
                }

                else -> listOf(Beam(next, direction))
            }
        }
    }

    data class State(val path: MutableSet<Beam>, val beams: List<Beam>) {

        fun next(matrix: Matrix): State {
            val newBeams: List<Beam> = beams.flatMap { it.next(matrix) }.filterNot { it in path }
            newBeams.forEach { path.add(it) }
            return State(path, newBeams)
        }

    }

}
