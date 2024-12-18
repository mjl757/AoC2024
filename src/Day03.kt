fun main() {
    fun part1(input: List<String>): Int {
        var total = 0
        input.forEach {
            total += getLineTotal(it)
        }
        return total
    }

    fun part2(input: List<String>): Int {
        return getEnabledTotal(input.joinToString())
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day03")

    part1(input).println()
    part2(input).println()
}

fun getLineTotal(input: String) : Int {
    val expressionRegex = Regex("(mul\\()+[0-9]{1,3}+,+[0-9]{1,3}+\\)")

    val numberRegex = Regex("([0-9]{1,3})")
    val expressions = expressionRegex.findAll(input).map { it.value }

    var total = 0

    expressions.forEach { expression ->
        val numbers = numberRegex.findAll(expression).map { it.value.toInt() }.toList()
        total += numbers[0] * numbers[1]
    }

    return total
}

fun getEnabledTotal(input: String) : Int {
    val doRegex = Regex("(do\\(\\))")
    val dontRegex = Regex("(don't\\(\\))")

    val doNotResults = dontRegex.findAll(input).map { it.range.last }
    val doResults = doRegex.findAll(input).map { it.range.last }

    val enabledRanges = buildEnableRanges(doResults, doNotResults, input.lastIndex)
    var total = 0
    enabledRanges.forEach { range ->
        total += getLineTotal(input.substring(range))
    }

    return total
}

fun buildEnableRanges(doIndices: Sequence<Int>, dontIndices: Sequence<Int>, lastIndex: Int): List<IntRange> {
    return buildList {
        var lastDo = 0
        var lastDont = 0
        var enabled = true
        while (true) {
            if (enabled) {
                val nextDont = dontIndices.firstOrNull { it > lastDo }
                if (nextDont != null) {
                    add(IntRange(lastDo, nextDont))
                    lastDont = nextDont
                } else {
                    add(IntRange(lastDo, lastIndex))
                    break
                }
            } else {
                val nextDo = doIndices.firstOrNull { it > lastDont } ?: break
                lastDo = nextDo
            }
            enabled = !enabled
        }

    }
}
