package me.luchs.aoc2023.shared

import me.luchs.aoc2023.DayEighteen

data class StringMatrix(val nodes: MutableMap<Coordinate, String> = mutableMapOf()) {

    var minColumn = 0
    var maxColumn = 0
    var minRow = 0
    var maxRow = 0

    fun add(value: Pair<Coordinate, String>): StringMatrix {
        this.nodes[value.first] = value.second

        minRow = minRow()
        maxRow = maxRow()
        minColumn = minColumn()
        maxColumn = maxColumn()

        return this
    }

    fun valueAt(row: Int, column: Int): String? {
        return valueAt(Coordinate(row, column))
    }

    fun valueAt(point: Coordinate): String? {
        return nodes[point]
    }

    fun isOutside(point: Coordinate): Boolean {
        return point.row !in rows() || point.column !in columns()
    }

    fun rows(): IntRange = minRow..maxRow

    fun columns(): IntRange = minColumn..maxColumn

    private fun minRow(): Int {
        return nodes.keys.minOf { it.row }.toInt()
    }

    private fun maxRow(): Int {
        return nodes.keys.maxOf { it.row }.toInt()
    }

    private fun minColumn(): Int {
        return nodes.keys.minOf { it.column }.toInt()
    }

    private fun maxColumn(): Int {
        return nodes.keys.maxOf { it.column }.toInt()
    }

    override fun toString(): String {
        fillBlanks()
        return nodes.entries.groupBy { it.key.row }.entries.sortedBy { it.key }
            .joinToString(separator = "\n") { row ->
                row.value.sortedBy { it.key.column }.map { it.value.first() }
                    .joinToString(separator = "")
            }
    }

    fun fillBlanks(value: String = "."): StringMatrix {
        columns().forEach { column ->
            rows().forEach { row ->
                if (valueAt(row, column) == null) nodes[Coordinate(row, column)] = value
            }
        }
        return this
    }

}


fun StringMatrix.floodFill(start: Coordinate, value: String, nullValue: String = ".") {

    val queue = mutableListOf(start)
    val visited = mutableListOf<Coordinate>()

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()

        if (current in visited) {
            continue
        }

        val valueAt = this.valueAt(current)
        if (valueAt != null && valueAt != nullValue) {
            continue // Point is already present -> skip
        }

        visited.add(current)

        this.add(current to value)

        current.adjacent4()
            .filterNot { it in visited }
            .filterNot { this.isOutside(it) }
            .forEach { queue.add(it) }
    }

}