package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DayEightTest {

    private val input1 =
        """
            RL

            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent()

    private val input2 =
        """LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)
        """.trimIndent()

    private val input3 =
        """
            LR

            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
        """.trimIndent()

    @Test
    fun partOneExample1() {
        val result = DayEight(input1).partOne()
        Assertions.assertEquals(2, result)
    }

    @Test
    fun partOneExample2() {
        val result = DayEight(input2).partOne()
        Assertions.assertEquals(6, result)
    }

    @Test
    fun partTwo() {
        val result = DayEight(input3).partTwo()
        Assertions.assertEquals(6, result)
    }
}
