package me.luchs.aoc2023.shared

import kotlin.math.abs


data class Point(val row: Long, val column: Long, val value: Char? = null) {

    constructor(row: Int, column: Int, value: Char) : this(row.toLong(), column.toLong(), value)

    companion object {
        operator fun invoke(pair: Pair<Long, Long>): Point {
            return Point(pair.first, pair.second)
        }
    }

    fun manhattanDistanceTo(other: Point): Long {
        return abs(this.row - other.row) + abs(this.column - other.column)
    }

    fun right(limit: Int): Point? {
        return if (column + 1 < limit) null else right()
    }

    fun right(): Point {
        return Point(row, column + 1, value)
    }

    fun left(limit: Int = 0): Point? {
        return if (column - 1 < limit) null else left()
    }

    fun left(): Point {
        return Point(row, column - 1, value)
    }

    fun up(limit: Int = 0): Point? {
        return if (row - 1 < limit) null else up()
    }

    fun up(): Point {
        return Point(row - 1, column, value)
    }

    fun down(limit: Int): Point? {
        return if (row + 1 > limit) null else down()
    }

    fun down(): Point {
        return Point(row + 1, column, value)
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

fun List<Point>.asString(): String {
    return this.map { it.value }.joinToString(separator = "")
}

fun List<Point>.slice(index: Int, vertical: Boolean): List<Point> =
    if (vertical) this.filter { it.column == index.toLong() } else
        this.filter { it.row == index.toLong() }

fun List<Point>.maxColumn(): Int = this.maxOf { it.column }.toInt()

fun List<Point>.maxRow(): Int = this.maxOf { it.row }.toInt()

fun List<Point>.transpose(): List<Point> = this.map { Point(it.column, it.row, it.value) }

