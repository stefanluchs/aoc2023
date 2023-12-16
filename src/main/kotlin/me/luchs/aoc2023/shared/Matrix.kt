package me.luchs.aoc2023.shared

data class Matrix(val entries: List<Point>) {

    companion object {
        /**
         * Input must be a String matrix with dimensions n x m
         * @return Map with all nodes of the matrix as Point
         */
        operator fun invoke(input: String): Matrix {
            val nodes = input.lines().flatMapIndexed { row, content ->
                content.mapIndexed { column, value -> Point(row, column, value) }
            }
            return Matrix(nodes)
        }
    }

    fun transpose(): Matrix {
        return Matrix(this.entries.map { Point(it.column, it.row, it.value) })
    }

    fun fillBlanks(value: Char): Matrix {
        (0..this.maxColumn()).forEach { column ->
            (0..this.maxRow()).forEach { row ->
                if (position(row, column) == null) entries.addLast(Point(row, column, value))
            }
        }
        return this
    }

    fun position(point: Point): Point? {
        return entries.find { it.row == point.row && it.column == point.column }
    }

    fun position(row: Int, column: Int): Point? {
        return entries.find { it.row.toInt() == row && it.column.toInt() == column }
    }

    fun rows(): IntRange = 0..maxRow()

    fun columns(): IntRange = 0..maxColumn()

    fun maxRow(): Int {
        return entries.maxOf { it.row }.toInt()
    }

    fun maxColumn(): Int {
        return entries.maxOf { it.column }.toInt()
    }

    fun rotateLeft(): Matrix {
        val maxColumn = this.maxColumn()
        return Matrix(this.entries.map { Point(maxColumn - it.column, it.row, it.value) })
    }

    fun rotateRight(): Matrix {
        val maxRow = this.maxRow()
        return Matrix(this.entries.map { Point(it.column, maxRow - it.row, it.value) })
    }

    override fun toString(): String {
        return entries.groupBy { it.row }.entries.sortedBy { it.key }
            .joinToString(separator = "\n") { row ->
                row.value.sortedBy { it.column }.map { it.value }.joinToString(separator = "")
            }
    }

}
