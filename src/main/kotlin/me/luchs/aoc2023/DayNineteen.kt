package me.luchs.aoc2023

data class DayNineteen(val input: String) : Day<Int> {

    override fun partOne(): Int {
        val (workflows, parts) = parseInput(input)

        val results = parts.associateWith { part ->
            var target = "in"
            var workflow = workflows[target]
            while (workflow != null) {
                target = workflow.rules.firstNotNullOf { it.matchToTarget(part) }
                workflow = workflows[target]
            }
            target
        }

        return results.entries
            .filter { it.value == "A" }
            .map { it.key }
            .sumOf { part -> part.values.map { it.value }.sumOf { it } }
    }

    override fun partTwo(): Int {
        TODO("Not yet implemented")
    }

    private fun parseInput(input: String): Pair<Map<String, Workflow>, List<Part>> {
        val (workflowInput, partsInput) = input.split("\n\n")
        val workflows = workflowInput.lines().map { Workflow(it) }.associateBy { it.name }
        val parts = partsInput.lines().map { Part(it) }
        return workflows to parts
    }

    data class Part(val values: Map<String, Int>) {
        companion object {
            operator fun invoke(row: String): Part =
                Part(row.drop(1).dropLast(1)
                    .split(',')
                    .associate {
                        val (key, value) = it.split('=')
                        key to value.toInt()
                    })
        }
    }

    interface Rule {
        fun matchToTarget(part: Part): String?
    }

    data class PartRule(
        val category: String,
        val value: Int,
        val target: String,
        val comparison: (Int, Int) -> Boolean
    ) : Rule {

        companion object {
            operator fun invoke(input: String): Rule {
                if (!input.contains(':')) {
                    return fallback(input)
                }
                val (operation, target) = input.split(':')
                val (category, value) = operation.split('<', '>')
                val comparison =
                    if (input.contains('<')) { i: Int, i2: Int -> i < i2 } else { i: Int, i2: Int -> i > i2 }
                return PartRule(category, value.toInt(), target, comparison)
            }

            private fun fallback(input: String): Rule = object : Rule {
                override fun matchToTarget(part: Part): String {
                    return input
                }
            }
        }

        override fun matchToTarget(part: Part): String? {
            val partValue = part.values[category]!!
            return if (comparison(partValue, value)) target else null
        }

    }

    data class Workflow(val name: String, val rules: List<Rule>) {
        companion object {
            operator fun invoke(row: String): Workflow {
                val (name, p2) = row.split('{')
                val rules = p2.dropLast(1).split(',').map { PartRule(it) }
                return Workflow(name, rules)
            }
        }
    }

}
