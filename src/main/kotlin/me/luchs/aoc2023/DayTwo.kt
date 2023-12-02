package me.luchs.aoc2023


data class DayTwo(val input: String) : Day<Int> {

    override fun partOne(): Int {
        return input.lines()
            .map { it.toGame() }
            .filter { it.isPossible() }
            .sumOf { it.id }
    }

    override fun partTwo(): Int {
        return input.lines()
            .map { it.toGame() }
            .sumOf { it.getPowerOfMinimumSetOfCubes() }
    }

    private fun String.toGame(): Game {
        val id = this
            .split(':')[0]
            .split(' ')[1]
            .toInt()
        val subsets = this
            .split(':')[1]
            .split(';')
            .map { it.trim() }
            .map { it.toSubset() }
        return Game(id, subsets)
    }

    private fun String.toSubset(): Subset {
        val cubes = this
            .split(',')
            .map { it.trim() }
            .map { it.split(' ') }
        val redCubes = cubes.getCubesWithColor(CubeColor.RED)
        val greenCubes = cubes.getCubesWithColor(CubeColor.GREEN)
        val blueCubes = cubes.getCubesWithColor(CubeColor.BLUE)
        return Subset(redCubes, greenCubes, blueCubes)
    }

    private fun List<List<String>>.getCubesWithColor(cubeColor: CubeColor): Int {
        return this
            .firstOrNull { it[1].equals(cubeColor.name, ignoreCase = true) }
            ?.first()
            ?.toInt()
            ?: 0
    }

    private data class Game(val id: Int, val subsets: List<Subset>) {
        fun isPossible(): Boolean {
            return this.subsets.none { it.isImpossible() }
        }

        fun getPowerOfMinimumSetOfCubes(): Int {
            val maxRed  = subsets.maxOf { it.red }
            val maxGreen  = subsets.maxOf { it.green }
            val maxBlue = subsets.maxOf { it.blue }
            return maxRed * maxGreen * maxBlue
        }
    }

    private data class Subset(val red: Int, val green: Int, val blue: Int) {
        fun isImpossible(): Boolean {
            return red > CubeColor.RED.maxNumber
                    || green > CubeColor.GREEN.maxNumber
                    || blue > CubeColor.BLUE.maxNumber
        }
    }

    private enum class CubeColor(val maxNumber: Int) {
        RED(12),
        GREEN(13),
        BLUE(14)
    }
}