import Day11.Part1
import Day11.Part2
import Day11.Part2.getStonesAfterBlink
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main() = runBlocking {
    val input = readInput("Day11").joinToString(" ").split(" ")
    println("Part 1: ${Part1.run(input)}")
    println("Part 2: ${Part2.run(input)}")
}

private object Day11 {
    object Part1 {
        suspend fun run(input: List<String>): Int {
            return getStonesAfterBlink(input, 25)
        }
    }

    object Part2 {
        suspend fun run(input: List<String>): Int {
             return getStonesAfterBlink(input, 75)
        }

        suspend fun getStonesAfterBlink(stones: List<String>, numberOfBlinks: Int) : Int = withContext(Dispatchers.IO) {
            val deferred: MutableList<Deferred<Int>> = mutableListOf()
            stones.forEach {
                deferred.add(
                    async {
                        getStoneCount(numberOfBlinks, 0, it)
                    }
                )
            }
            deferred.awaitAll().sum()
        }

        fun getStoneCount(maxDepth: Int, currentDepth: Int, stone: String): Int {
            if (currentDepth >= maxDepth) {
                return 1
            }
            val nextDepth = currentDepth + 1
            val length = stone.length
            val value = if (stone == "0") {
                getStoneCount(maxDepth, nextDepth, "1")
            } else if (length % 2 == 0) {
                val half = length / 2
                val leftStone = stone.substring(0, half)
                val rightStone = stone.substring(half, stone.length).toLong().toString()
                getStoneCount(maxDepth, nextDepth, leftStone) +
                        getStoneCount(maxDepth, nextDepth, rightStone)
            } else {
                getStoneCount(maxDepth, nextDepth, (stone.toLong() * 2024).toString())
            }
            return value
        }
    }
}