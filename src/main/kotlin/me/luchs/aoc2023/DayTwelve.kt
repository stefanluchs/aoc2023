package me.luchs.aoc2023

data class DayTwelve(val input: String) : Day<Long> {

    override fun partOne(): Long {
        return Springs(input).sumOf { it.numberOfArrangements() }
    }

    override fun partTwo(): Long {
        return Springs(input, times = 5).sumOf { it.numberOfArrangements() }
    }

    data class Springs(val record: String, val groups: List<Int>) {
        companion object {
            operator fun invoke(input: String, times: Int = 1): List<Springs> {
                return input.lines().map { line ->
                    val (record, groupInput) = line.split(' ')
                    val groups = groupInput.split(',').map { it.toInt() } * times
                    Springs(record * times, groups)
                }
            }

            private operator fun List<Int>.times(times: Int): List<Int> {
                val result = ArrayList<Int>(this.size * times)
                repeat(times) { result += this }
                return result
            }

            private operator fun String.times(times: Int): String {
                val stringBuilder = StringBuilder()
                repeat(times) {
                    if (stringBuilder.isNotEmpty()) {
                        stringBuilder.append('?')
                    }
                    stringBuilder.append(this)
                }
                return stringBuilder.toString()
            }
        }

        fun recordToGroups(): List<Int> {
            return record.toGroups()
        }

        private fun String.toGroups(): List<Int> {
            return this.split(Regex("[.]+")).map { it.length }.filter { it > 0 }
        }

        data class State(
            val index: Int = 0,
            val lastGroupIndex: Int? = null,
            val lastGroupSize: Int = 0,
            val isGroup: Boolean = false,
            val remainingUnknown: Int
        ) {
            companion object {
                fun initial(record: String): State {
                    return State(remainingUnknown = record.count { it == '?' })
                }
            }

            fun next(inGroup: Boolean, isUnknown: Boolean): State {
                return copy(
                    index = index + 1,
                    lastGroupIndex = if (!isGroup && inGroup) (lastGroupIndex ?: -1) + 1 else lastGroupIndex,
                    lastGroupSize =
                    if (!isGroup && inGroup) 1 else if (inGroup) lastGroupSize + 1 else lastGroupSize,
                    isGroup = inGroup,
                    remainingUnknown = if (isUnknown) remainingUnknown - 1 else remainingUnknown
                )
            }

            fun isComplete(groups: List<Int>): Boolean {
                return (
                    remainingUnknown == 0 &&
                        lastGroupIndex != null &&
                        lastGroupSize == groups[lastGroupIndex] &&
                        lastGroupIndex == groups.size - 1
                    )
            }

            fun isInvalid(groups: List<Int>): Boolean {
                return isInvalidAsMaxNumberOfGroupsExceeded(groups) ||
                    isInvalidAsLastGroupSizeIsTooSmall(groups) ||
                    isInvalidAsLastGroupSizeIsTooLarge(groups) ||
                    isNoUnknownLeft()
            }

            private fun isInvalidAsMaxNumberOfGroupsExceeded(groups: List<Int>): Boolean {
                return lastGroupIndex != null && lastGroupIndex >= groups.size
            }

            private fun isInvalidAsLastGroupSizeIsTooSmall(groups: List<Int>): Boolean {
                return !isGroup && lastGroupIndex != null && lastGroupSize < groups[lastGroupIndex]
            }

            private fun isInvalidAsLastGroupSizeIsTooLarge(groups: List<Int>): Boolean {
                return lastGroupIndex != null && lastGroupSize > groups[lastGroupIndex]
            }

            private fun isNoUnknownLeft(): Boolean {
                return remainingUnknown < 0
            }
        }

        fun numberOfArrangements(): Long {
            return evaluateState(State.initial(record))
        }

        private fun evaluateState(state: State, cache: MutableMap<State, Long> = hashMapOf()): Long {
            if (state.isInvalid(groups)) {
                return 0L
            }

            if (state.index == record.length) {
                return if (state.isComplete(groups)) 1L else 0L
            }

            return cache[state]
                ?: run {
                    var result = 0L
                    when (record[state.index]) {
                        '?' -> {
                            result += evaluateState(state.next(inGroup = false, isUnknown = true), cache)
                            result += evaluateState(state.next(inGroup = true, isUnknown = true), cache)
                        }
                        '.' -> {
                            result += evaluateState(state.next(inGroup = false, isUnknown = false), cache)
                        }
                        '#' -> {
                            result += evaluateState(state.next(inGroup = true, isUnknown = false), cache)
                        }
                    }
                    result.also { cache[state] = it }
                }
        }
    }
}
