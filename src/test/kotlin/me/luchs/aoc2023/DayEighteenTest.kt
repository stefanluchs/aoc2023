package me.luchs.aoc2023

import me.luchs.aoc2023.shared.Direction
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
        Assertions.assertEquals(952408144115L, result)
    }

    @Test
    fun plans() {
        val result = DayEighteen.Plan.fromInput(input, fromColor = false)
        Assertions.assertEquals(14, result.size)

        Assertions.assertEquals(Direction.RIGHT, result[0].direction)
        Assertions.assertEquals(6, result[0].range)

        Assertions.assertEquals(Direction.LEFT, result[2].direction)
        Assertions.assertEquals(2, result[2].range)
    }

    @Test
    fun plans_from_color() {
        val result = DayEighteen.Plan.fromInput(input, fromColor = true)
        Assertions.assertEquals(14, result.size)

        Assertions.assertEquals(Direction.RIGHT, result[0].direction)
        Assertions.assertEquals(461937, result[0].range)

        Assertions.assertEquals(Direction.RIGHT, result[2].direction)
        Assertions.assertEquals(356671, result[2].range)

        Assertions.assertEquals(Direction.DOWN, result[9].direction)
        Assertions.assertEquals(829975, result[9].range)

        Assertions.assertEquals(Direction.LEFT, result[12].direction)
        Assertions.assertEquals(5411, result[12].range)

        Assertions.assertEquals(Direction.UP, result[13].direction)
        Assertions.assertEquals(500254, result[13].range)
    }

}