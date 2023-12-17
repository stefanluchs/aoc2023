package me.luchs.aoc2023.shared

enum class Direction {
    UP, DOWN, LEFT, RIGHT;

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