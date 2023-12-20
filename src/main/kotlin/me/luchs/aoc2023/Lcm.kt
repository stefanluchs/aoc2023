package me.luchs.aoc2023

/**
 * Calculates the least common multiple (LCM) of two integers.
 *
 * @param left The first integer.
 * @param right The second integer.
 * @return The LCM of the two integers.
 */
fun lcm(left: Long, right: Long): Long {
    val larger = if (left > right) left else right
    val maxLcm = left * right
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % left == 0L && lcm % right == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}

fun Iterable<Long>.lcm(): Long = this.reduce { acc, l -> lcm(acc, l) }

