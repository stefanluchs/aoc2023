package me.luchs.aoc2023

import me.luchs.aoc2023.shared.distinctPairs


data class DayTwentyFour(val input: String) : Day<Int> {

    private val hailstones = input.lines().map { Hailstone(it) }

    override fun partOne(): Int {
        return numberOfIntersectionsWithin(min = 200000000000000, max = 400000000000000)
    }

    fun numberOfIntersectionsWithin(min: Long, max: Long): Int =
        hailstones.distinctPairs()
            .mapNotNull { it.first.intersectionPoint2D(it.second) }
            .filter {
                val range = min.toDouble()..max.toDouble()
                range.contains(it.first) && range.contains(it.second)
            }
            .size

    override fun partTwo(): Int {
        TODO("Not yet implemented")
    }

    data class Hailstone(val position: Array<Long>, val velocity: Array<Long>) {
        companion object {
            operator fun invoke(row: String): Hailstone {
                val (position, velocity) = row.split(" @ ")
                    .map { it.split(",").map { it.trim().toLong() }.toTypedArray() }
                return Hailstone(position, velocity)
            }
        }

        fun Array<Long>.x() = this[0]

        fun Array<Long>.y() = this[1]

        fun intersectionPoint2D(other: Hailstone): Pair<Double, Double>? {

            // determine if intersection exists
            if (this.crossProduct2D(other) == 0L) return null

            // Assuming vectors are lines in 2D space, you can find the intersection point
            // by solving the system of linear equations formed by the vectors.

            // Represent the vectors as parametric equations: v1 = p1 + t1 * d1, v2 = p2 + t2 * d2
            val p1 = this.position // Starting point of vector 1
            val p2 = other.position // Starting point of vector 2

            val v1 = this.velocity // Direction of vector 1
            val v2 = other.velocity // Direction of vector 2

            val t1: Double =
                1.0 * (p1.y() * v2.x() + v2.y() * p2.x() - p2.y() * v2.x() - v2.y() * p1.x()) / (v1.x() * v2.y() - v1.y() * v2.x())

            val t2: Double = (p1.x() + v1.x() * t1 - p2.x()) / v2.x()

            // check if intersection is in the future
            if (t1 < 0 || t2 < 0) return null

            // Use t1 to find the intersection point
            return p1.x() + t1 * v1.x() to p1.y() + t1 * v1.y()
        }

        private fun crossProduct2D(other: Hailstone): Long =
            this.velocity[0] * other.velocity[1] - this.velocity[1] * other.velocity[0]

    }

}
