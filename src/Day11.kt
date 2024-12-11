import Day11.Part1
import Day11.Part1.blink
import Day11.Part2

fun main() {
    val input = readInput("Day11").joinToString(" ").split(" ")
    println("Part 1: ${Part1.run(input)}")
    println("Part 2: ${Part2.run(input)}")
}

private object Day11 {
    object Part1 {
        fun run(input: List<String>) : Int {
            var stones = input
            for (i in 1..25) {
                stones = stones.blink()
            }
            return stones.size
        }

        fun List<String>.blink() : List<String> {
            val currentStones = this
            return buildList {
                currentStones.forEach { stone ->
                    val length = stone.length
                    when {
                        stone == "0" -> add("1")
                        length % 2 == 0 -> {
                            val half = length / 2
                            add(stone.substring(0, half))
                            add(stone.substring(half, stone.length).toLong().toString())
                        }
                        else -> {
                            add((stone.toLong()*2024).toString())
                        }
                    }
                }
            }
        }
    }

    object Part2 {
        fun run(input: List<String>): Int {
            var stones = input
            for (i in 1..75) {
                stones = stones.blink()
            }
            return stones.size
        }
    }
}