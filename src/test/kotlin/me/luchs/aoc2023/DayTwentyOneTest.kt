package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


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
        val result =  DayTwentyOne(input).positionsAfter(steps = 6).size.toLong()
        Assertions.assertEquals(16L, result)
    }

    @Test
    fun partTwo() {
        val result = DayTwentyOne(input).partTwo()
        Assertions.assertEquals(32000000L, result)
    }

}