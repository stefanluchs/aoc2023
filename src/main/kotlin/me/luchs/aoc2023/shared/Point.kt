package me.luchs.aoc2023.shared

import kotlin.math.abs

data class Point(val row: Long, val column: Long, val value: Char? = null) {

    companion object {
        operator fun invoke(pair: Pair<Long, Long>): Point {
            return Point(pair.first, pair.second)
        }
    }

    fun manhattanDistanceTo(other: Point): Long {
        return abs(this.row - other.row) + abs(this.column - other.column)
    }

    fun adjacent4(): List<Point> {
        return listOf(
            Point(row, column + 1),
            Point(row, column - 1),
            Point(row + 1, column),
            Point(row - 1, column)
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (row != other.row) return false
        if (column != other.column) return false

        return true
    }

    override fun hashCode(): Int {
        var result = row
        result = 31 * result + column
        return result.toInt()
    }
}
