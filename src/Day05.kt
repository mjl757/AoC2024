import Day05.Part1
import Day05.Part2

fun main() {
    val input = readInput("Day05")
    val rules = getRules(input)
    val updates = getUpdates(input)
    println("Part 1: ${Part1.run(rules, updates)}")
    println("Part 2: ${Part2.run(rules, updates)}")
}

fun getUpdates(input: List<String>): List<List<Int>> = buildList {
    val startIndex = input.indexOf("") + 1
    input.subList(startIndex, input.size).forEach { update ->
        add(update.split(",").map { it.toInt() })
    }
}

fun getRules(input: List<String>): Map<Int, List<Int>> {
    return buildMap {
        for (rule in input) {
            if (rule.isEmpty()) break
            val beforeAfter = rule.split("|").map { it.toInt() }
            val before = beforeAfter[0]
            val after = beforeAfter[1]
            val currentList = get(after)?.toMutableList() ?: mutableListOf()
            currentList.add(before)
            put(after, currentList)
        }
    }
}

private object Day05 {
    object Part1 {
        fun run(rules: Map<Int, List<Int>>, updates: List<List<Int>>): Int {
            var total = 0
            var number = 0
            updates.forEach {
                if (checkOrder(rules, it)) {
                    val middle = it.size / 2
                    total += it[middle]
                    number++
                }
            }
            return total
        }

        fun checkOrder(rules: Map<Int, List<Int>>, update: List<Int>): Boolean {
            update.forEachIndexed { index, item ->
                if (index != update.lastIndex) {
                    val beforeValues = rules[item] ?: emptyList()
                    val nextValues = update.subList(index + 1, update.size)
                    nextValues.forEach {
                        if (beforeValues.contains(it)) {
                            return false
                        }
                    }
                }
            }
            return true
        }
    }

    object Part2 {
        fun run(rules: Map<Int, List<Int>>, updates: List<List<Int>>): Int {
            var total = 0
            updates.forEach {
                if (!Part1.checkOrder(rules, it)) {
                    val sortedLiat = sortUpdate(rules, it)
                    val middle = sortedLiat.size / 2
                    total += sortedLiat[middle]
                }
            }
            return total
        }

        fun sortUpdate(rules: Map<Int, List<Int>>, update: List<Int>): List<Int> {
            val mutableList = update.toMutableList()
            while (!Part1.checkOrder(rules, mutableList)) {
                var index = 0
                outer@ for (item in mutableList) {
                    if (index != update.lastIndex) {
                        val beforeValues = rules[item] ?: emptyList()
                        val nextValues = mutableList.subList(index + 1, update.size)
                        var checkedIndex = index + 1
                        for (checkedItem in nextValues) {
                            if (beforeValues.contains(checkedItem)) {
                                mutableList.removeAt(checkedIndex)
                                mutableList.add(index, checkedItem)
                                break@outer
                            }
                            checkedIndex++
                        }
                    }
                    index++
                }
            }
            return mutableList
        }
    }
}