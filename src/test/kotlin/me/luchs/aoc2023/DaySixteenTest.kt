package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class DaySixteenTest {

    private val input = """
            .|...\....
            |.-.\.....
            .....|-...
            ........|.
            ..........
            .........\
            ..../.\\..
            .-.-/..|..
            .|....-|.\
            ..//.|....
    """.trimIndent()

    @Test
    fun partOne() {
        val result = DaySixteen(input).partOne()
        Assertions.assertEquals(46, result)
    }

    @Test
    fun partTwo() {
        val result = DaySixteen(input).partTwo()
        Assertions.assertEquals(51, result)
    }

}