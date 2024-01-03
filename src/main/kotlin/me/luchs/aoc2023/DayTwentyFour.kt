package me.luchs.aoc2023

import Jama.Matrix
import me.luchs.aoc2023.shared.distinctPairs
import kotlin.math.roundToLong

data class DayTwentyFour(val input: String) : Day<Long> {

    private val hailstones = input.lines().map { Hailstone(it) }

    override fun partOne(): Long {
        return numberOfIntersectionsWithin(min = 200000000000000, max = 400000000000000).toLong()
    }

    fun numberOfIntersectionsWithin(min: Long, max: Long): Int =
        hailstones.distinctPairs()
            .mapNotNull { it.first.intersectionPoint2D(it.second) }
            .filter {
                val range = min.toDouble()..max.toDouble()
                range.contains(it.first) && range.contains(it.second)
            }
            .size

    /**
     * Assume the stone we shoot starts at a, b, c @ d, e, f
     * If we look at the world from the point of view of the stone, then we are not moving.
     * All of the hailstones will be aiming to hit us right at the origin.
     * So transform each hailstone x, y, z @ dx, dy, dz to x-a, y-b, z-c @ dx-d, dy-e, dz-f
     * If it is going to hit the origin, then the vector from the origin to the starting position has to be
     * a multiple of its velocity. We only need two dimensions to solve this for a, b, d and e:
     * (x-a) : (y-b) = (dx-d) : (dy-e)
     * (x-a) * (dy-e) = (y-b) * (dx-d)
     * Fill in for two different hailstones x1, y1 @ dx1, dy1 and x2, y2 @ dx2, dy2 and subtract
     * to get a linear equation for a, b, d and e:
     * (dy2 - dy1) * a + (dx1 - dx2) * b + (y1 - y2) * d + (x2 - x1) * e = dx1 * y1 - dx2 * y2 + x2 * dy2 - x1 * dy1
     * Each combination j of hailstorms yields a different equation of the type
     * cj1 * a + cj2 * b + cj3 * c + cj4 * d = cj5
     * Take four such equations to form a matrix
     * ((c11, c12, c13, c14),   (a,  (c51,
     *  (c21, c22, c23, c24),    b,   c52,
     *  (c31, c32, c33, c34),    d,   c53,
     *  (c41, c42, c43, c44)) *  e) = c54)
     * Or, A * x = b
     * We can find x by inverting A and computing x = A^-1 * b
     *
     * Credit to on https://www.reddit.com/r/adventofcode/comments/18pnycy/comment/kesqnis/?utm_source=share&utm_medium=web3x&utm_name=web3xcss&utm_term=1&utm_content=share_button
     */
    override fun partTwo(): Long {

        val coefficients = hailstones
            .distinctPairs()
            .take(4)
            .map {
                listOf(
                    it.second.velocity.y - it.first.velocity.y,
                    it.first.velocity.x - it.second.velocity.x,
                    it.first.position.y - it.second.position.y,
                    it.second.position.x - it.first.position.x
                ).map { it.toDouble() }.toDoubleArray() to
                        1.0 * it.first.velocity.x * it.first.position.y - it.second.velocity.x * it.second.position.y +
                        it.second.position.x * it.second.velocity.y - it.first.position.x * it.first.velocity.y
            }

        val A = Matrix(coefficients.map { it.first }.toTypedArray())
        val x = Matrix(coefficients.map { listOf(it.second).toDoubleArray() }.toTypedArray())

        val (a, b, d, _) = A.inverse().times(x).array.map { it[0] }.map { it.roundToLong() }

        val h1 = hailstones.first()
        val t1 = (a - h1.position.x) / (h1.velocity.x - d)

        val h2 = hailstones[1]
        val t2 = (a - h2.position.x) / (h2.velocity.x - d)

        val f =
            ((h1.position.z - h2.position.z) + t1 * h1.velocity.z - t2 * h2.velocity.z) / (t1 - t2)
        val c = h1.position.z + t1 * (h1.velocity.z - f)

        return a + b + c

    }

    data class Hailstone(val position: Array<Long>, val velocity: Array<Long>) {

        companion object {
            operator fun invoke(row: String): Hailstone {
                val (position, velocity) = row.split(" @ ")
                    .map { it.split(",").map { it.trim().toLong() }.toTypedArray() }
                return Hailstone(position, velocity)
            }
        }

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
                1.0 * (p1.y * v2.x + v2.y * p2.x - p2.y * v2.x - v2.y * p1.x) / (v1.x * v2.y - v1.y * v2.x)

            val t2: Double = (p1.x + v1.x * t1 - p2.x) / v2.x

            // check if intersection is in the future for both vectors
            if (t1 < 0 || t2 < 0) return null

            // Use t1 to determine the intersection point
            return p1.x + t1 * v1.x to p1.y + t1 * v1.y
        }

        private fun crossProduct2D(other: Hailstone): Long =
            this.velocity[0] * other.velocity[1] - this.velocity[1] * other.velocity[0]

    }

}

val Array<Long>.x: Long
    get() = this[0]

val Array<Long>.y: Long
    get() = this[1]

val Array<Long>.z: Long
    get() = this[2]
