package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class DayEighteenTest {

    private val input = """
        R 6 (#70c710)
        D 5 (#0dc571)
        L 2 (#5713f0)
        D 2 (#d2c081)
        R 2 (#59c680)
        D 2 (#411b91)
        L 5 (#8ceee2)
        U 2 (#caa173)
        L 1 (#1b58a2)
        U 2 (#caa171)
        R 2 (#7807d2)
        U 3 (#a77fa3)
        L 2 (#015232)
        U 2 (#7a21e3)
    """.trimIndent()

    @Test
    fun partOne() {
        val result = DayEighteen(input).partOne()
        Assertions.assertEquals(62, result)
    }

    @Test
    fun partTwo() {
        val result = DayEighteen(input).partTwo()
        //Assertions.assertEquals(94, result)
    }

}