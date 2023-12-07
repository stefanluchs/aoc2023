package me.luchs.aoc2023

/**
 * Performs a binary search within the specified range to find a value that satisfies the given
 * evaluation function.
 *
 * @param lowerBound The lower bound of the search range.
 * @param upperBound The upper bound of the search range.
 * @param inverted Indicates whether the search should be performed in an inverted manner, i.e., to
 * find the first value that * does not satisfy the evaluation.
 * @param evaluation The evaluation function that determines whether a value satisfies the search
 * condition.
 * ```
 *                   It should take a single parameter of type Long and return a Boolean indicating whether the value satisfies the condition.
 * @return
 * ```
 * The value that satisfies the evaluation function, or null if no such value is found.
 */
fun binarySearch(
    lowerBound: Long,
    upperBound: Long,
    inverted: Boolean = false,
    evaluation: (Long) -> Boolean
): Long? {
    var begin = lowerBound
    var end = upperBound
    var result: Long? = null
    while (begin <= end) {
        val mid = (begin + end) / 2L
        if (evaluation(mid)) {
            result = mid
            if (inverted) {
                end = mid - 1
            } else {
                begin = mid + 1
            }
        } else {
            if (inverted) {
                begin = mid + 1
            } else {
                end = mid - 1
            }
        }
    }
    return result
}
