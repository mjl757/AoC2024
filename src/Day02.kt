import kotlin.math.absoluteValue

fun main() {

    fun part1(input: List<List<Int>>): Int {
        var total = 0
        input.forEach { report ->
            if (report.getFirstUnsafeLevelIndex() == null) {
                total++
            }
        }
        return total
    }

    fun part2(input: List<List<Int>>): Int {
        var total = 0
        input.forEach { report ->
            val unsafeIndex = report.getFirstUnsafeLevelIndex()
            if (unsafeIndex == null) {
                total++
            } else {
                val shortenedReport1 = report.toMutableList().apply { removeAt(unsafeIndex) }
                val shortenedReport2 = report.toMutableList().apply { removeAt(unsafeIndex-1) }
                if (shortenedReport1.getFirstUnsafeLevelIndex() == null) {
                    total++
                } else if (shortenedReport2.getFirstUnsafeLevelIndex() == null) {
                    total ++
                } else {
                    // If the First index is the issue it won't be apparent until we get to the 3rd
                    // item in the list (index 2) and fail due to the order appearing to be wrong.
                    // Because the isDecreasing flag is determined by the first to levels in the report
                    if (unsafeIndex == 2) {
                        val shortenedReport3 = report.toMutableList().apply { removeAt(0) }
                        if (shortenedReport3.getFirstUnsafeLevelIndex() == null) {
                            total++
                        }
                    }
                }
            }
        }

        return total
    }

    fun String.mapToList(): List<Int> {
        val numbers = split(" ")
        return numbers.map { it.toInt() }
    }

    val input = readInput("Day02").map { it.mapToList() }

    part1(input).println()
    part2(input).println()
}


private fun List<Int>.getFirstUnsafeLevelIndex(): Int? {
    var lastLevel: Int? = null
    var isDecreasing = true
    this.forEachIndexed { index, level ->
        if (lastLevel != null) {
            val difference = level - lastLevel!!
            if (difference.absoluteValue > 3 || difference == 0 ||
                (isDecreasing && difference > 0) ||
                (!isDecreasing && difference < 0)
            ) {
                return index
            }
        } else {
            val nextDiff = this[1] - level
            isDecreasing = nextDiff < 0
        }
        lastLevel = level
    }
    return null
}