
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val input = readInput("Day11").joinToString(" ").split(" ")
    val blinks = Day11.Blinks(input)
    println("Part 1: ${blinks.getCount(25)}")
    println("Part 2: ${blinks.getCount(75)}")
}

private object Day11 {

    class Blinks(val initialStones: List<String>, ) {
        var foundValues = mutableMapOf<Pair<String, Int>, Int>()

        fun getCount(numberOfBlinks: Int) : Int {
            foundValues = mutableMapOf()
            return initialStones.sumOf {
                getStoneCount(numberOfBlinks, it)
            }
        }

        fun getStoneCount(currentDepth: Int, stone: String): Int {
            val value = foundValues.getOrPut(stone to currentDepth) {
                val nextDepth = currentDepth - 1
                val length = stone.length
                if (currentDepth <= 0) {
                    1
                } else if (stone == "0") {
                    getStoneCount(nextDepth, "1")
                } else if (length % 2 == 0) {
                    val half = length / 2
                    listOf(stone.substring(0, half), stone.substring(half).toLong().toString()).sumOf {
                        getStoneCount(nextDepth, it)
                    }
                } else {
                    getStoneCount(nextDepth, (stone.toLong() * 2024).toString())
                }
            }

            return value
        }
    }
}