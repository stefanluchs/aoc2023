package me.luchs.aoc2023.shared

import me.luchs.aoc2023.DayEleven
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PairTest {

    private val input =
        """
        ...#......
        .......#..
        #.........
        ..........
        ......#...
        .#........
        .........#
        ..........
        .......#..
        #...#.....
        """.trimIndent()

    @Test
    fun pairs() {
        val pairs = DayEleven.Space(input).expand().galaxies.distinctPairs()
        Assertions.assertEquals(36, pairs.size)
    }

}