package me.luchs.aoc2023.shared

enum class Direction {
    UP, DOWN, LEFT, RIGHT;

    companion object {
        fun fromAbbreviation(char: Char ): Direction? {
            return when(char) {
                'R' -> RIGHT
                'L' -> LEFT
                'U' -> UP
                'D' -> DOWN
                else -> null
            }
        }
    }

    fun turnLeft(): Direction {
        return when(this) {
            UP -> LEFT
            LEFT -> DOWN
            DOWN -> RIGHT
            RIGHT -> UP
        }
    }

    fun turnRight(): Direction {
        return when(this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
        }
    }

}