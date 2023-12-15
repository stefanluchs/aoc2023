package me.luchs.aoc2023

data class DayFifteen(val input: String) : Day<Int> {
    override fun partOne(): Int {
        return input.split(',')
            .map { step ->
                step.toCharArray()
                    .map { it.code }
                    .fold(0) { acc, next -> ((acc + next) * 17) % 256 }
            }
            .sumOf { it }
    }

    override fun partTwo(): Int {
        TODO("Not yet implemented")
    }

}
