
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val input = readInput("Day11").joinToString(" ").split(" ")
    val blinks = Day11.Blinks(input.map { it.toLong() })
    println("Part 1: ${blinks.getCount(25)}")
    println("Part 2: ${blinks.getCount(75)}")
}

private object Day11 {

    class Blinks(val initialStones: List<Long>, ) {
        var foundValues = mutableMapOf<Pair<Long, Int>, Long>()

        fun getCount(numberOfBlinks: Int) : Long {
            foundValues = mutableMapOf()
            return initialStones.sumOf {
                getStoneCount(numberOfBlinks, it)
            }
        }

        fun getStoneCount(currentDepth: Int, stone: Long): Long {
            val value = foundValues.getOrPut(stone to currentDepth) {
                val nextDepth = currentDepth - 1
                if (currentDepth <= 0) {
                    1
                } else if (stone == 0L) {
                    getStoneCount(nextDepth, 1L)
                } else if (stone.toString().length % 2 == 0) {
                    val stoneString = stone.toString()
                    val half = stoneString.length / 2
                    listOf(stoneString.substring(0, half).toLong(), stoneString.substring(half).toLong()).sumOf {
                        getStoneCount(nextDepth, it)
                    }
                } else {
                    getStoneCount(nextDepth, (stone* 2024))
                }
            }

            return value
        }
    }
}