package me.luchs.aoc2023

import me.luchs.aoc2023.shared.AStarAlgorithm
import me.luchs.aoc2023.shared.Point
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DayElevenTest {

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
    fun partOne() {
        val result = DayEleven(input).partOne()
        Assertions.assertEquals(374, result)
    }

    @Test
    fun space() {
        val space = DayEleven.Space(input)
        Assertions.assertEquals(9, space.galaxies.size)
        Assertions.assertEquals(Point(0, 3, '#'), space.galaxies[0])
        Assertions.assertEquals(Point(1, 7, '#'), space.galaxies[1])
        Assertions.assertEquals(Point(2, 0, '#'), space.galaxies[2])
        Assertions.assertEquals(Point(4, 6, '#'), space.galaxies[3])
    }

    @Test
    fun expandedSpace() {
        val space = DayEleven.Space(input).expand()
        Assertions.assertEquals(9, space.galaxies.size)
        Assertions.assertEquals(Point(0, 4, '#'), space.galaxies[0])
        Assertions.assertEquals(Point(1, 9, '#'), space.galaxies[1])
        Assertions.assertEquals(Point(2, 0, '#'), space.galaxies[2])
        Assertions.assertEquals(Point(5, 8, '#'), space.galaxies[3])
    }

    @Test
    fun pairs() {
        val pairs = DayEleven.Space(input).expand().pairs()
        Assertions.assertEquals(36, pairs.size)
    }

    @Test
    fun path_1_to7() {
        val space = DayEleven.Space(input).expand()
        val path = AStarAlgorithm(space.galaxies[0], space.galaxies[6]).computeMinimalPath()
        Assertions.assertEquals(15, path.size - 1)
    }

    @Test
    fun partTwo_faktor_10() {
        val result = DayEleven(input).partTwo(10)
        Assertions.assertEquals(1030, result)
    }

    @Test
    fun partTwo_faktor_100() {
        val result = DayEleven(input).partTwo(100)
        Assertions.assertEquals(8410, result)
    }
}
