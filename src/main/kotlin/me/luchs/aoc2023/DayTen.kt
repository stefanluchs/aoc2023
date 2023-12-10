package me.luchs.aoc2023

import kotlin.math.abs

data class DayTen(val input: String) : Day<Int> {

    override fun partOne(): Int {
        return Maze(input).loop().size / 2
    }

    override fun partTwo(): Int {

        val maze = Maze(input)
        val loop = maze.loop()
        val areas = maze.areas()

        val enclosed = areas
            .filter { it.all { it.first > 0 && it.first < maze.maxRow() } }
            .filter { it.all { it.second > 0 && it.second < maze.maxColumn() } }
            .filterNot {
                val node = it.minBy { it.first }
                val rows = node.first..maze.maxRow()
                val nodes = rows.map { it to node.second }
                val tiles = loop.filter { it.asPair() in nodes }
                    .map { it.char }
                    .filterNot { it == '|' }

                val crossings = tiles.filter { it == '-' }.size + minOf(
                    tiles.filter { it == 'L' || it == 'F' }.size,
                    tiles.filter { it == '7' || it == 'J' }.size
                )

                crossings % 2 == 0
            }
        return enclosed.sumOf { it.size }
    }


    data class Maze(val tiles: Map<Pair<Int, Int>, Tile>) {

        companion object {
            operator fun invoke(input: String): Maze {
                val tiles = input.lines()
                    .map { it.toCharArray() }
                    .mapIndexed { row, chars ->
                        chars.mapIndexed { column, char -> Tile(row, column, char) }
                            .associateBy { (it.row to it.column) }
                    }
                    .reduce { left, right -> left + right }
                return Maze(tiles)
            }
        }

        fun startPosition(char: Char = 'S'): Tile {
            return tiles.entries.find { it.value.char == char }!!.value
        }

        fun connectionsOf(tile: Tile, start: Boolean = false): List<Tile> {
            val neighbors = neighborsOf(tile)
            val location = tile.row to tile.column
            val connectingTiles = neighbors.filter {
                val connections = it.connections()
                connections.contains(location)
            }.filter { if (!start) tile.connections().contains(it.row to it.column) else true }
            return connectingTiles
        }

        fun neighborsOf(location: Tile): List<Tile> {
            return tiles
                .filter {
                    it.key.first == location.row && abs(it.key.second - location.column) == 1
                            || it.key.second == location.column && abs(it.key.first - location.row) == 1
                }
                .map { it.value }
        }

        fun loop(): List<Tile> {
            var position = this.startPosition()
            val visited = mutableListOf(position)
            val routes = this.connectionsOf(position, start = true).toMutableList()

            while (routes.isNotEmpty()) {
                position = routes.removeFirst()

                if (visited.contains(position)) {
                    break
                }

                visited.addLast(position)

                val connections = this.connectionsOf(position).filter { !visited.contains(it) }
                routes.addAll(connections)
            }
            return visited
        }

        fun areas(): List<List<Pair<Int, Int>>> {
            var tiles = this.nonLoopTiles().toMutableList().map { it.asPair() }

            val areas = mutableListOf<List<Pair<Int, Int>>>()

            while (tiles.isNotEmpty()) {
                val tile = tiles.removeFirst()
                val adjacent = tile.adjacent().filter { it in tiles }.toMutableList()
                val visited = mutableListOf(tile)

                while (adjacent.isNotEmpty()) {
                    val connection = adjacent.removeFirst()

                    if (connection in visited) {
                        continue
                    }

                    visited.add(connection)
                    val connectionAdjacent = connection.adjacent()
                        .filter { it in tiles }
                        .filterNot { it in visited }
                        .toMutableList()
                    adjacent.addAll(connectionAdjacent)
                }

                areas.add(visited)
                tiles = tiles.filterNot { it in visited }
            }
            return areas
        }

        fun nonLoopTiles(): List<Tile> {
            val loop = loop()

            return this.tiles.entries.asSequence()
                .map { it.value }
                .filterNot { it in loop }
                .toList()
        }

        fun maxRow(): Int {
            return tiles.entries.maxOf { it.key.first }
        }

        fun maxColumn(): Int {
            return tiles.entries.maxOf { it.key.second }
        }

        fun Pair<Int, Int>.adjacent(): List<Pair<Int, Int>> {
            return listOf(
                first to second + 1,
                first to second - 1,
                first + 1 to second,
                first - 1 to second
            )
        }
    }

    data class Tile(val row: Int, val column: Int, val char: Char) {

        fun connections(): List<Pair<Int, Int>> {
            return when (char) {
                '-' -> listOf(row to column - 1, row to column + 1) // left + right
                '|' -> listOf(row - 1 to column, row + 1 to column) // top + bottom
                'L' -> listOf(row to column + 1, row - 1 to column) // top + right
                'J' -> listOf(row to column - 1, row - 1 to column) // top + left
                '7' -> listOf(row to column - 1, row + 1 to column) // bottom + left
                'F' -> listOf(row to column + 1, row + 1 to column) // bottom + right
                else -> emptyList()
            }
        }

        fun asPair(): Pair<Int, Int> {
            return row to column
        }
    }

}



