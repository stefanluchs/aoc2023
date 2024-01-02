package me.luchs.aoc2023.shared

import kotlin.math.abs

data class Coordinate(val row: Int, val column: Int) {

    val x: Int get() = row
    val y: Int get() = column

    fun range(direction: Direction, range: Int): List<Coordinate> {
        return when (direction) {
            Direction.UP -> upRange(range)
            Direction.DOWN -> downRange(range)
            Direction.LEFT -> leftRange(range)
            Direction.RIGHT -> rightRange(range)
        }
    }

    fun adjacent4(): List<Coordinate> {
        return listOf(
            right(),
            left(),
            down(),
            up()
        )
    }

    fun manhattanDistanceTo(other: Coordinate): Int {
        return abs(this.row - other.row) + abs(this.column - other.column)
    }

    fun rightRange(range: Int = 1): List<Coordinate> =
        (column..column + range).map { column -> Coordinate(row, column) }

    fun leftRange(range: Int = 1): List<Coordinate> =
        (column - range..column).map { column -> Coordinate(row, column) }

    fun upRange(range: Int = 1): List<Coordinate> =
        (row - range..row).map { row -> Coordinate(row, column) }

    fun downRange(range: Int = 1): List<Coordinate> =
        (row..row + range).map { row -> Coordinate(row, column) }

    fun move(direction: Direction, range: Int): Coordinate {
        return when (direction) {
            Direction.UP -> up(range)
            Direction.DOWN -> down(range)
            Direction.LEFT -> left(range)
            Direction.RIGHT -> right(range)
        }
    }

    fun right(range: Int = 1): Coordinate = Coordinate(row, column + range)

    fun left(range: Int = 1): Coordinate = Coordinate(row, column - range)

    fun up(range: Int = 1): Coordinate = Coordinate(row - range, column)

    fun down(range: Int = 1): Coordinate = Coordinate(row + range, column)

    override fun toString(): String {
        return "($row, $column)"
    }

}

fun Coordinate.mod(dimensions: Dimensions): Coordinate =
    Coordinate(this.row.mod(dimensions.first), this.column.mod(dimensions.second))