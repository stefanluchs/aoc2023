package me.luchs.aoc2023

data class DayTwo(val input: String) : Day<Int> {

    override fun partOne(): Int {
        return input.lines().map { Game.of(it) }.filter { it.isPossible() }.sumOf { it.id }
    }

    override fun partTwo(): Int {
        return input.lines().map { Game.of(it) }.sumOf { it.getPowerOfMinimumSetOfCubes() }
    }

    /**
     * Represents a game that consists of subsets.
     *
     * @property id The id of the game.
     * @property subsets The list of subsets in the game.
     */
    private data class Game(val id: Int, val subsets: List<Subset>) {
        companion object {
            fun of(input: String): Game {
                val id = input.split(':')[0].split(' ')[1].toInt()
                val subsets = input.split(':')[1].split(';').map { it.trim() }.map { Subset.of(it) }
                return Game(id, subsets)
            }
        }

        /**
         * A Game is defined as 'possible' if none of the subsets is 'impossible'
         *
         * @return true if no subset is 'impossible'
         */
        fun isPossible(): Boolean {
            return this.subsets.none { it.isImpossible() }
        }

        /**
         * Calculates the power of the minimum set of cubes.
         *
         * @return The power of the minimum set of cubes.
         */
        fun getPowerOfMinimumSetOfCubes(): Int {
            val maxRed = subsets.maxOf { it.red }
            val maxGreen = subsets.maxOf { it.green }
            val maxBlue = subsets.maxOf { it.blue }
            return maxRed * maxGreen * maxBlue
        }
    }

    /**
     * Represents a subset of cubes with red, green, and blue colors.
     *
     * Each color is defined by an integer value representing the number of cubes of that color.
     *
     * @property red The number of red cubes.
     * @property green The number of green cubes.
     * @property blue The number of blue cubes.
     */
    private data class Subset(val red: Int, val green: Int, val blue: Int) {
        companion object {

            /**
             * Converts a string representation of cubes into a Subset object.
             *
             * @param input the string representation of cubes, separated by commas.
             * @return the Subset object representing the number of red, green, and blue cubes.
             */
            fun of(input: String): Subset {
                val cubes = input.split(',').map { it.trim() }.map { it.split(' ') }
                val redCubes = cubes.numberOf(CubeColor.RED)
                val greenCubes = cubes.numberOf(CubeColor.GREEN)
                val blueCubes = cubes.numberOf(CubeColor.BLUE)
                return Subset(redCubes, greenCubes, blueCubes)
            }

            private fun List<List<String>>.numberOf(cubeColor: CubeColor): Int {
                return this.firstOrNull { it[1].equals(cubeColor.name, ignoreCase = true) }
                    ?.first()
                    ?.toInt()
                    ?: 0
            }
        }

        /**
         * A Subset is defined as 'impossible' if any of the cube counts is above the limit
         *
         * @return true if any number of cubes is above the threshold.
         */
        fun isImpossible(): Boolean {
            return red > CubeColor.RED.maxNumber ||
                green > CubeColor.GREEN.maxNumber ||
                blue > CubeColor.BLUE.maxNumber
        }
    }

    /**
     * Represents the possible colors of a cube.
     *
     * @property maxNumber The maximum number associated with the color.
     *
     * @constructor Creates a new CubeColor enumeration with the given maximum number.
     *
     * @param maxNumber The maximum number associated with the color.
     */
    private enum class CubeColor(val maxNumber: Int) {
        RED(12),
        GREEN(13),
        BLUE(14)
    }
}
