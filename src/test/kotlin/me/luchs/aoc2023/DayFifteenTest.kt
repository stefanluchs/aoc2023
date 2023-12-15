package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class DayFifteenTest {

    private val input = """
        rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
    """.trimIndent()

    @Test
    fun partOne() {
        val result = DayFifteen(input).partOne()
        Assertions.assertEquals(1320, result)
    }

    @Test
    fun partTwo() {
        val result = DayFifteen(input).partTwo()
        Assertions.assertEquals(145, result)
    }

}