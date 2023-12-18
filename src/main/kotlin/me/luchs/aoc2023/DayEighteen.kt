package me.luchs.aoc2023

import me.luchs.aoc2023.shared.Direction

data class DayEighteen(val input: String) : Day<Int> {

    override fun partOne(): Int {
        val terrain = StringMatrix()

        var position = Coordinate(0, 0)
        val plans = Plan.fromInput(input)

        plans.forEach { plan ->
            position.range(plan.direction, plan.range).forEach { terrain.add(it to plan.color) }
            position = position.move(plan.direction, plan.range)
        }

        val intersection = (terrain.minRow..terrain.maxRow).first {
            val value = terrain.valueAt(
                it,
                terrain.minColumn + 1
            )
            value != null && value != "."
        }.let { Coordinate(it, terrain.minColumn + 1) }

        val col = terrain.minColumn + 1
        terrain.nodes.filter { it.key.column == col }

        terrain.floodFill(start = intersection.down(), value = "#")

        return terrain.nodes.filterNot { it.value == "." }.size //+ count - 1
    }

    override fun partTwo(): Int {
        TODO("Not yet implemented")
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

    data class Plan(val direction: Direction, val range: Int, val color: String) {
        companion object {

            fun fromInput(input: String): List<Plan> = input.lines().map { Plan(it) }

            operator fun invoke(row: String): Plan {
                val (direction, range, color) = row.split(' ')
                return Plan(
                    direction = Direction.fromAbbreviation(direction.first())!!,
                    range = range.toInt(),
                    color = color.drop(1).dropLast(1)
                )
            }
        }
    }

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

    data class Coordinate(val row: Int, val column: Int) {

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

    }

}
