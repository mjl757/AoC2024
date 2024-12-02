import kotlin.math.abs

fun main() {
    fun part1(input1: List<Int>, input2: List<Int>): Int {
        val sort1 = input1.sorted()
        val sort2 = input2.sorted()
        var total = 0
        sort1.forEachIndexed { index, item ->
            total += abs(item - sort2[index])
        }
        return total
    }

    fun part2(input1: List<Int>, input2: List<Int>): Int {
        var total = 0
        input1.forEachIndexed { index, item ->
            total += input2.filter { it == item }.count() * item
        }

        return total
    }

    fun String.mapToPair() : Pair<Int, Int> {
        val numbers = split("   ")
        return Pair(numbers[0].toInt(), numbers[1].toInt())
    }

    val input1 = mutableListOf<Int>()
    val input2 = mutableListOf<Int>()
    // Read the input from the `src/Day01.txt` file.
    readInput("Day01").forEach {
        val pair = it.mapToPair()
        input1.add(pair.first)
        input2.add(pair.second)

    }
    part1(input1, input2).println()
    part2(input1, input2).println()
}
