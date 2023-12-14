package me.luchs.aoc2023

import me.luchs.aoc2023.shared.Matrix
import me.luchs.aoc2023.shared.findCycleInTransitions

data class DayFourteen(val input: String) : Day<Int> {

    override fun partOne(): Int = Matrix(input).tiltUp().totalLoad()

    override fun partTwo(): Int {
        var map = Matrix(input)

        // apply tilt procedure until cycle is detected
        val (cycle, cycleStartIndex) = findCycleInTransitions(
            initial = map.copy(),
            transition = { it.tiltCycle() }
        )

        // calculate remaining cycles as
        // expected number for iterations - iterations until cycle % cycle length
        val remainingCycles = (1000000000 - cycleStartIndex) % cycle.size

        // apply tilt cycle
        repeat(cycleStartIndex + cycle.size + remainingCycles) { map = map.tiltCycle() }

        return map.totalLoad()
    }
}

fun Matrix.totalLoad(): Int = this.entries
    .filter { it.value!! == 'O' }
    .sumOf { this.maxRow() - it.row.toInt() + 1 }

fun Matrix.tiltCycle(): Matrix = this
    .tiltUp().rotateRight()
    .tiltUp().rotateRight()
    .tiltUp().rotateRight()
    .tiltUp().rotateRight()

fun Matrix.tiltUp(moving: Char = 'O', fixed: Char = '#', blank: Char = '.'): Matrix {

    val cubesPositions = this.entries.filter { it.value!! == fixed }

    val roundsOriginalPositions =
        this.entries.filter { it.value!! == moving }.groupBy { it.row.toInt() }

    // first row always keeps current position
    val roundsNewPositions = roundsOriginalPositions[0]?.toMutableList() ?: mutableListOf()

    (1..this.maxRow()).forEach { row ->
        // update blockers each row
        val blockers = cubesPositions + roundsNewPositions
        roundsOriginalPositions[row]?.forEach {
            var unblocked = true
            var position = it
            while (unblocked) {
                val next = position.up(limit = 0)
                if (next != null && next !in blockers) position = next else unblocked = false
            }
            roundsNewPositions.addLast(position)
        }
    }

    return Matrix(cubesPositions + roundsNewPositions).fillBlanks(blank)
}
