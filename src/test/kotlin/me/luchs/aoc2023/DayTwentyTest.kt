package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DayTwentyTest {


    private val example1 = """
        broadcaster -> a, b, c
        %a -> b
        %b -> c
        %c -> inv
        &inv -> a
    """.trimIndent()

    private val example2 = """
        broadcaster -> a
        %a -> inv, con
        &inv -> b
        %b -> con
        &con -> output
    """.trimIndent()

    @Test
    fun partOne_example1() {
        val result = DayTwenty(example1).partOne()
        Assertions.assertEquals(32000000L, result)
    }

    @Test
    fun partOne_example2() {
        val result = DayTwenty(example2).partOne()
        Assertions.assertEquals(11687500L, result)
    }

}