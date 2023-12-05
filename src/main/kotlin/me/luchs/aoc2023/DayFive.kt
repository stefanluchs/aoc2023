package me.luchs.aoc2023

data class DayFive(val input: String) : Day<Long> {

    /**
     * Returns the minimum value calculated by processing the seeds in the Almanac.
     *
     * @return the minimum calculated value
     */
    override fun partOne(): Long {
        val almanac = Almanac(input = input)
        return almanac.seeds.minOf { seed -> almanac.process(seed) }
    }

    /**
     * Finds the location using the given input and Almanac. This method performs a binary search
     * to find the location with a local minimum value.
     *
     * @return The found location as a Long value.
     */
    override fun partTwo(): Long {
        val almanac = Almanac(input = input, inverse = true)
        val seedRanges = almanac.seedRanges()

        // initialize with minimum seed as upper bound for the location result
        var location = seedRanges.map { it.first }.minOf { it }

        // search for local minima with decreased upper bound every iteration
        // break if no valid result is found anymore
        while (true) {
            location =
                binarySearch(lowerBound = 0, upperBound = location - 1, inverted = true) { value ->
                    // Valid if any of the seed ranges contains the value
                    seedRanges.any { it.contains(almanac.process(value)) }
                } ?: break
        }
        return location
    }

    data class Almanac(val seeds: List<Long>, val mappings: List<MappingGroup>) {
        companion object {

            operator fun invoke(input: String, inverse: Boolean = false): Almanac {

                val sections = input.split("\n\n")
                val seeds = mutableListOf<Long>()
                val mappings = mutableListOf<MappingGroup>()

                sections.forEach { processSection(it, seeds, inverse, mappings) }

                return Almanac(
                    seeds,
                    if (inverse) mappings.reversed() else mappings
                )
            }


            private fun processSection(
                section: String,
                seeds: MutableList<Long>,
                inverse: Boolean,
                mappings: MutableList<MappingGroup>
            ) {
                val lines = section.split("\n")
                val header = lines[0]
                if (header.startsWith("seeds:")) {
                    seeds += header.split(": ")[1].split(" ").map { it.toLong() }
                } else {

                    val (sourceCategory, targetCategory) = header
                        .split(" ")[0]
                        .split("-to-")


                    val groupMappings = lines.drop(1)
                        .filter { it.isNotBlank() }
                        .map {
                            val (destination, source, size) = it.split(" ").map { it.toLong() }
                            Mapping(
                                source = if (inverse) destination else source,
                                destination = if (inverse) source else destination,
                                range = size,
                            )
                        }

                    mappings += MappingGroup(
                        sourceCategory = if (inverse) targetCategory else sourceCategory,
                        destinationCategory = if (inverse) sourceCategory else targetCategory,
                        mappings = groupMappings
                    )
                }
            }
        }

        /**
         * Generates a list of LongRange objects from the seeds.
         *
         * @return A list of LongRange objects based on the seeds.
         */
        fun seedRanges(): List<LongRange> {
            return seeds
                .chunked(2)
                .map { (start, len) -> LongRange(start, start + len - 1) }
        }

        /**
         * Processes the given value by applying a series of mappings.
         *
         * @param value the value to process
         * @return the processed value
         */
        fun process(value: Long): Long {
            var result = value
            for (mapping in mappings) {
                result = mapping.mappedValue(result)
            }
            return result
        }

    }

    /**
     * Represents a group of mappings between source and destination categories.
     *
     * @property sourceCategory The source category for this mapping group.
     * @property destinationCategory The destination category for this mapping group.
     * @property mappings The list of mappings within this group.
     */
    data class MappingGroup(
        val sourceCategory: String,
        val destinationCategory: String,
        val mappings: List<Mapping>
    ) {

        /**
         * Returns the mapped value for the given value based on the mappings.
         *
         * @param value The value to be mapped.
         * @return The mapped value. If there is no mapping found, returns the original value itself.
         */
        fun mappedValue(value: Long): Long {
            return mappings.firstNotNullOfOrNull { it.mappedValue(value) } ?: value
        }
    }

    /**
     * Represents a mapping between a source range of values to a destination range of values.
     *
     * @property source The starting value of the source range.
     * @property destination The starting value of the destination range.
     * @property range The length of the range.
     */
    data class Mapping(val source: Long, val destination: Long, val range: Long) {

        /**
         * Maps the given value from the source range to the destination range.
         *
         * @param value the value to be mapped
         * @return the mapped value or null if the given value is outside the source range
         */
        fun mappedValue(value: Long): Long? {
            return if (value in source until source + range) destination + (value - source) else null
        }
    }

}
