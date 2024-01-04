package me.luchs.aoc2023

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class DayTwentyFiveTest {

    val example = """
        jqt: rhn xhk nvd
        rsh: frs pzl lsr
        xhk: hfx
        cmg: qnr nvd lhk bvb
        rhn: xhk bvb hfx
        bvb: xhk hfx
        pzl: lsr hfx nvd
        qnr: nvd
        ntq: jqt hfx bvb xhk
        nvd: lhk
        lsr: lhk
        rzs: qnr cmg lsr rsh
        frs: qnr lhk lsr
    """.trimIndent()

    @Test
    fun partOne() {
        val result = DayTwentyFive(example).partOne()
        Assertions.assertEquals(54, result)
    }

    @Test
    fun partTwo() {
        val result = DayTwentyFive(example).partTwo()
        Assertions.assertEquals(47L, result)
    }

}