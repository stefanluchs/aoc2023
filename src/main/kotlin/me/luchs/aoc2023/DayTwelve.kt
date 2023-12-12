package me.luchs.aoc2023

data class DayTwelve(val input: String) : Day<Long> {
    override fun partOne(): Long {
        return Springs(input).sumOf { it.numberOfArrangements() }
    }

    override fun partTwo(): Long {
        return Springs(input, times = 5).sumOf { it.numberOfArrangementsPartTwo() }
    }

    data class State(
        val index: Int = 0,
        val lastGroupIndex: Int? = null,
        val lastGroupSize: Int = 0,
        val isGroup: Boolean = false,
        val remainingUnknown: Int
    ) {
        fun next(inGroup: Boolean, isUnknown: Boolean): State {
            return copy(
                index = index + 1,
                lastGroupIndex = if (!isGroup && inGroup) (lastGroupIndex
                    ?: -1) + 1 else lastGroupIndex,
                lastGroupSize = if (!isGroup && inGroup) 1 else if (inGroup) lastGroupSize + 1 else lastGroupSize,
                isGroup = inGroup,
                remainingUnknown = if (isUnknown) remainingUnknown - 1 else remainingUnknown
            )
        }
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
                repeat(times) {
                    result += this
                }
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

        fun numberOfArrangements(): Long {
            val unknown = record
                .mapIndexedNotNull { index, char -> if (char == '?') index else null }
                .toMutableList()

            val input = record.toCharArray()

            var arrangements: List<CharArray> = mutableListOf(input)

            while (unknown.isNotEmpty()) {
                val index = unknown.removeFirst()
                arrangements = arrangements.flatMap { it.fillAt(index) }
            }

            val possible = arrangements.map { it.toGroups() }.filter { it == groups }

            return possible.size.toLong()
        }

        private fun CharArray.toGroups(): List<Int> {
            return this.joinToString("").toGroups()
        }

        private fun String.toGroups(): List<Int> {
            return this
                .split(Regex("[.]+"))
                .map { it.length }
                .filter { it > 0 }
        }

        private fun CharArray.fillAt(index: Int): List<CharArray> {
            val operational = this.clone()
            val damaged = this.clone()
            operational[index] = '.'
            damaged[index] = '#'
            return listOf(operational, damaged)
        }

        fun numberOfArrangementsPartTwo(): Long {
            return process(
                state = State(remainingUnknown = record.count { it == '?' }),
                cache = hashMapOf()
            )
        }

        private fun process(state: State, cache: MutableMap<State, Long>): Long {
            // state has reached max number of groups
            if (state.lastGroupIndex != null && state.lastGroupIndex >= groups.size) {
                return 0L
            }
            // state is not in group and last group size does not match expected size
            if (!state.isGroup && state.lastGroupIndex != null
                && state.lastGroupSize < groups[state.lastGroupIndex]) {
                return 0L
            }
            //
            if (state.lastGroupIndex != null
                && state.lastGroupSize > groups[state.lastGroupIndex]) {
                return 0L
            }
            if (state.remainingUnknown < 0) {
                return 0L
            }
            if (state.index == record.length) {
                // Check for completeness
                return if (state.remainingUnknown == 0
                    && state.lastGroupIndex != null
                    && state.lastGroupSize == groups[state.lastGroupIndex]
                    && state.lastGroupIndex == groups.size - 1) {
                    1L
                } else {
                    0L
                }
            }
            return cache[state] ?: run {
                var total = 0L
                when (record[state.index]) {
                    '?' -> {
                        // Wildcard, process both options
                        total += process(
                            state = state.next(
                                inGroup = false,
                                isUnknown = true
                            ), cache = cache
                        )
                        total += process(
                            state = state.next(inGroup = true, isUnknown = true),
                            cache = cache
                        )
                    }

                    '.' -> {
                        // Finish group if possible
                        total += process(
                            state = state.next(
                                inGroup = false,
                                isUnknown = false
                            ), cache = cache
                        )
                    }

                    '#' -> {
                        // Increase group count
                        total += process(
                            state = state.next(
                                inGroup = true,
                                isUnknown = false
                            ), cache = cache
                        )
                    }
                }
                // Store result in cache
                total.also {
                    cache[state] = it
                }
            }
        }

    }
}
