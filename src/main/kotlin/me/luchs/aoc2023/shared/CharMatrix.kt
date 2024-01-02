package me.luchs.aoc2023.shared

data class CharMatrix(val nodes: MutableMap<Coordinate, Char> = mutableMapOf()) {

    var minColumn = 0
    var maxColumn = 0
    var minRow = 0
    var maxRow = 0

    val dimensions: Dimensions
        get() = maxColumn + 1 to maxRow + 1

    operator fun get(coordinate: Coordinate): Char? = nodes[coordinate]

    operator fun set(coordinate: Coordinate, value: Char) {
        nodes[coordinate] = value
    }

    operator fun get(value: Char): List<Coordinate> =
        nodes.entries.filter { it.value == value }.map { it.key }

    companion object {
        /**
         * Input must be a String matrix with dimensions n x m
         * @return Map with all nodes of the matrix as Point
         */
        operator fun invoke(input: String): CharMatrix {
            val nodes = input.lines().mapIndexed { row, content ->
                content.mapIndexed { column, value -> Coordinate(row, column) to value }
                    .associate { it.first to it.second }.toMutableMap()
            }.reduce { acc, map -> (acc + map).toMutableMap() }
            return CharMatrix(nodes)
        }
    }

    init {
        this.updateDimensions()
    }

    fun add(value: Pair<Coordinate, Char>): CharMatrix {
        this.nodes[value.first] = value.second
        this.updateDimensions()
        return this
    }

    private fun updateDimensions() {
        minRow = minRow()
        maxRow = maxRow()
        minColumn = minColumn()
        maxColumn = maxColumn()
    }

    fun valueAt(row: Int, column: Int): Char? {
        return valueAt(Coordinate(row, column))
    }

    fun valueAt(point: Coordinate): Char? {
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
                row.value.sortedBy { it.key.column }.map { it.value }
                    .joinToString(separator = "")
            }
    }

    fun fillBlanks(value: Char = '.'): CharMatrix {
        columns().forEach { column ->
            rows().forEach { row ->
                if (valueAt(row, column) == null) nodes[Coordinate(row, column)] = value
            }
        }
        return this
    }

    fun adjacent4(coordinate: Coordinate): Map<Coordinate, Char> =
        coordinate.adjacent4().filterNot { this[it] == null }.associateWith { this[it]!! }


    fun adjacent4WithValues(coordinate: Coordinate, vararg value: Char): Map<Coordinate, Char> =
        coordinate.adjacent4().filter { this[it] in value.toList() }.associateWith { this[it]!! }

}

fun CharMatrix.floodFill(start: Coordinate, value: Char, nullValue: Char = '.') {

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

typealias Dimensions = Pair<Int, Int>
