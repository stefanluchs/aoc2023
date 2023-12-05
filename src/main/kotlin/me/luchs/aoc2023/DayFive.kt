package me.luchs.aoc2023

data class DayFive(val input: String) : Day<Long> {


    override fun partOne(): Long {
        val almanac = Almanac(input = input)
        return almanac.seeds.minOf { seed -> almanac.process(seed) }
    }

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

                    val groupMappings = mutableListOf<Mapping>()

                    for (i in 1 until lines.size) {
                        val line = lines[i]
                        if (line.isEmpty()) continue
                        val (dest, source, size) = line.split(" ").map { it.toLong() }
                        groupMappings += Mapping(
                            source = if (inverse) dest else source,
                            destination = if (inverse) source else dest,
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

        fun seedRanges(): List<LongRange> {
            return seeds
                .chunked(2)
                .map { (start, len) -> LongRange(start, start + len - 1) }
        }

        fun process(value: Long): Long {
            var result = value
            for (mapping in mappings) {
                result = mapping.mappedValue(result)
            }
            return result
        }

    }

    data class MappingGroup(
        val sourceCategory: String,
        val destinationCategory: String,
        val mappings: List<Mapping>
    ) {
        fun mappedValue(value: Long): Long {
            return mappings.firstNotNullOfOrNull { it.mappedValue(value) } ?: value
        }
    }

    data class Mapping(val source: Long, val destination: Long, val range: Long) {
        fun mappedValue(value: Long): Long? {
            return if (value in source until source + range) destination + (value - source) else null
        }
    }

}
