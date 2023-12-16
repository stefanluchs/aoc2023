package me.luchs.aoc2023

import me.luchs.aoc2023.shared.Direction4
import me.luchs.aoc2023.shared.Matrix
import me.luchs.aoc2023.shared.Point

data class DaySixteen(val input: String) : Day<Int> {

    private val matrix = Matrix(input)

    private val maxRow = matrix.maxRow()
    private val maxColumn = matrix.maxColumn()

    override fun partOne(): Int =
        energizedTiles(Beam(position = Point(0, -1), direction = Direction4.RIGHT))

    override fun partTwo(): Int =
        (matrix.columns().flatMap { column ->
            listOf(
                Beam(position = Point(-1, column.toLong()), direction = Direction4.DOWN),
                Beam(position = Point(maxRow + 1L, column.toLong()), direction = Direction4.UP),
            )
        } + matrix.rows().flatMap { row ->
            listOf(
                Beam(position = Point(row.toLong(), -1), direction = Direction4.RIGHT),
                Beam(position = Point(row.toLong(), maxColumn + 1L), direction = Direction4.LEFT),
            )
        }).maxOf { energizedTiles(start = it) }


    private fun energizedTiles(start: Beam): Int {

        var state = State(beams = listOf(start))

        while (state.beams.isNotEmpty()) {
            state = state.next(matrix)
        }

        return state.path.map { it.position }.distinct().size
    }

    data class State(val path: MutableSet<Beam> = mutableSetOf(), val beams: List<Beam>) {

        fun next(matrix: Matrix): State = beams
            .flatMap { it.next(matrix) }
            .filterNot { it in path }
            .also { path.addAll(it) }
            .let { State(path, it) }

    }

    data class Beam(val position: Point, val direction: Direction4) {

        fun next(matrix: Matrix): List<Beam> {

            val next = position.move(direction)

            // if the next position is outside of matrix beam terminated
            val node = matrix.position(next) ?: return this.terminate()

            return when (node.value!!) {
                '|' -> {
                    if (direction in listOf(Direction4.LEFT, Direction4.RIGHT)) {
                        // split beam into up and down
                        listOf(Beam(next, Direction4.DOWN), Beam(next, Direction4.UP))
                    } else {
                        // proceed to the next point without changes
                        proceedTo(point = next)
                    }
                }

                '-' -> {
                    if (direction in listOf(Direction4.UP, Direction4.DOWN)) {
                        // split beam into left and right
                        listOf(Beam(next, Direction4.LEFT), Beam(next, Direction4.RIGHT))
                    } else {
                        // proceed to the next point without changes
                        proceedTo(point = next)
                    }
                }

                '\\' -> {
                    // change the beam direction
                    when (direction) {
                        Direction4.UP -> Direction4.LEFT
                        Direction4.DOWN -> Direction4.RIGHT
                        Direction4.LEFT -> Direction4.UP
                        Direction4.RIGHT -> Direction4.DOWN
                    }.let { Beam(next, it) }.let { listOf(it) }
                }

                '/' -> {
                    // change the beam direction
                    when (direction) {
                        Direction4.UP -> Direction4.RIGHT
                        Direction4.DOWN -> Direction4.LEFT
                        Direction4.LEFT -> Direction4.DOWN
                        Direction4.RIGHT -> Direction4.UP
                    }.let { Beam(next, it) }.let { listOf(it) }
                }

                else -> proceedTo(point = next)
            }
        }

        private fun proceedTo(point: Point): List<Beam> = listOf(Beam(point, direction))

        private fun terminate(): List<Beam> = emptyList()

    }

}
