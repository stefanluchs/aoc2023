package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource


class DayTwentyOneTest {

    private val input = """
        ...........
        .....###.#.
        .###.##..#.
        ..#.#...#..
        ....#.#....
        .##..S####.
        .##..#...#.
        .......##..
        .##.#.####.
        .##..##.##.
        ...........
    """.trimIndent()

    @Test
    fun partOne() {
        val result = DayTwentyOne(input).positionsAfter(steps = 6)
        Assertions.assertEquals(16L, result)
    }

    @ParameterizedTest
    @CsvSource(
        "6,16",
        "10, 50",
        "50, 1594",
        "100, 6536",
        "500, 167004",
        "1000, 668697",
    )
    fun partTwo(steps: Int, expected: Long) {
        val result = DayTwentyOne(input).positionsAfter(steps)
        Assertions.assertEquals(expected, result)
    }

}