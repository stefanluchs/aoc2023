package me.luchs.aoc2023

data class DayFive(val input: String) : Day<Long> {

    override fun partOne(): Long {
        val almanac = Almanac.ofInput(input)
        val seeds = seedsFromInput()
        return seeds.map { almanac.seedToLocation(it) }.minBy { it }
    }

    override fun partTwo(): Long {
        TODO("Not yet implemented")
    }

    fun seedsFromInput(): LongArray {
        return input
            .lines()[0]
            .split(':')[1]
            .split(Regex(" +"))
            .filter { it.isNotBlank() }
            .map { it.toLong() }
            .toTypedArray()
            .toLongArray()
    }

    data class Almanac(val foodMaps: List<FoodMap>) {
        companion object {
            fun ofInput(input: String): Almanac {
                val inputLines = input.lines()
                val mappingInput = inputLines.subList(2, inputLines.size)

                val mappings = listOf(
                    FoodMap.ofMapsInput(Food.SEED, Food.SOIL, mappingInput),
                    FoodMap.ofMapsInput(Food.SOIL, Food.FERTILIZER, mappingInput),
                    FoodMap.ofMapsInput(Food.FERTILIZER, Food.WATER, mappingInput),
                    FoodMap.ofMapsInput(Food.WATER, Food.LIGHT, mappingInput),
                    FoodMap.ofMapsInput(Food.LIGHT, Food.TEMPERATURE, mappingInput),
                    FoodMap.ofMapsInput(Food.TEMPERATURE, Food.HUMIDITY, mappingInput),
                    FoodMap.ofMapsInput(Food.HUMIDITY, Food.LOCATION, mappingInput)
                ).flatten()

                return Almanac(mappings)
            }
        }

        fun seedToLocation(seed: Long): Long {
            val soil = mapSourceToDestination(Food.SEED, Food.SOIL, seed)
            val fertilizer = mapSourceToDestination(Food.SOIL, Food.FERTILIZER, soil)
            val water = mapSourceToDestination(Food.FERTILIZER, Food.WATER, fertilizer)
            val light = mapSourceToDestination(Food.WATER, Food.LIGHT, water)
            val temperature = mapSourceToDestination(Food.LIGHT, Food.TEMPERATURE, light)
            val humidity = mapSourceToDestination(Food.TEMPERATURE, Food.HUMIDITY, temperature)
            return mapSourceToDestination(Food.HUMIDITY, Food.LOCATION, humidity)
        }

        private fun mapSourceToDestination(source: Food, destination: Food, number: Long): Long {
            val map = foodMaps
                .filter { it.source == source }
                .find { it.isInSourceRange(number) }
            return map?.destinationValue(number) ?: number
        }

    }

    data class FoodMap(
        val source: Food, val sourceRangeStart: Long,
        val destination: Food, val destinationRangeStart: Long,
        val range: Long
    ) {
        companion object {
            fun ofMapsInput(source: Food, target: Food, mapsInput: List<String>): List<FoodMap> {
                val foodMapStartIndex = mapsInput
                    .indexOfFirst { it.startsWith(source.name, ignoreCase = true) }

                var foodMapEndIndex = mapsInput
                    .subList(foodMapStartIndex, mapsInput.size)
                    .indexOfFirst { it.isBlank() }

                if (foodMapEndIndex < 0) {
                    foodMapEndIndex = mapsInput.size
                } else {
                    foodMapEndIndex += foodMapStartIndex
                }

                val entries =
                    mapsInput.subList(
                        foodMapStartIndex + 1,
                        foodMapEndIndex
                    )

                return entries.map {
                    val values = it.split(' ')
                    val destinationRangeStart = values[0].toLong()
                    val sourceRangeStart = values[1].toLong()
                    val range = values[2].toLong()
                    FoodMap(source, sourceRangeStart, target, destinationRangeStart, range)
                }
            }
        }

        fun destinationValue(number: Long): Long {
            return number - sourceRangeStart + destinationRangeStart
        }

        fun isInSourceRange(number: Long): Boolean {
            return LongRange(sourceRangeStart, sourceRangeStart + range - 1).contains(number)
        }
    }

    enum class Food {
        SEED,
        SOIL,
        FERTILIZER,
        WATER,
        LIGHT,
        TEMPERATURE,
        HUMIDITY,
        LOCATION
    }

}
