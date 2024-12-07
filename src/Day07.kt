import Day07.Part1
import Day07.Part2

fun main() {
    val input = readInput("Day07")
    val equations = input.map { it.toEquation() }
    println("Part 1: ${Part1.run(equations)}")
    println("Part 2: ${Part2.run(equations)}")
}

private object Day07 {
    object Part1 {
        fun run(input: List<Equation>): Long = input.mapNotNull {
            testEquation(
                it,
                listOf(Operator.SUM, Operator.MUL)
            )
        }.sum()

        fun testEquation(equation: Equation, listOfPossibleOperators: List<Operator>): Long? {
            val operatorsList = generateOperators(
                equation.components.size - 1,
                listOfPossibleOperators
            )
            operatorsList.forEach { operators ->
                var total = 0L
                equation.components.forEachIndexed { index, component ->
                    if (index == 0) {
                        total += component
                    } else {
                        total = operators[index - 1].compute(total, component)
                    }
                    if (total > equation.answer) return@forEachIndexed
                }
                if (total == equation.answer) {
                    return equation.answer
                }
            }
            return null
        }

        fun generateOperators(
            numberOfOperators: Int,
            listOfPossibleOperators: List<Operator>
        ): List<List<Operator>> = buildList {
            listOfPossibleOperators.forEach {
                addAll(createOperators(numberOfOperators, listOf(), it, listOfPossibleOperators))
            }
        }

        fun createOperators(
            numberOfOperators: Int,
            currentList: List<Operator>,
            value: Operator,
            listOfPossibleOperators: List<Operator>
        ): List<List<Operator>> {
            val newList = currentList.toMutableList().apply {
                add(value)
            }
            if (newList.size == numberOfOperators) {
                return listOf(newList)
            }

            val sumList = buildList {
                listOfPossibleOperators.forEach {
                    addAll(
                        createOperators(
                            numberOfOperators,
                            newList,
                            it,
                            listOfPossibleOperators
                        )
                    )
                }
            }

            return sumList
        }
    }

    object Part2 {
        fun run(input: List<Equation>): Long = input.mapNotNull {
            Part1.testEquation(
                it,
                Operator.entries
            )
        }.sum()
    }

    data class Equation(
        val answer: Long,
        val components: List<Long>
    )

    enum class Operator {
        SUM,
        MUL,
        CAT;

        fun compute(first: Long, second: Long): Long {
            return when (this) {
                SUM -> first + second
                MUL -> first.times(second)
                CAT -> (first.toString() + second.toString()).toLong()
            }
        }
    }
}

private fun String.toEquation(): Day07.Equation {
    val input = this.split(":", limit = 2)
    val components: List<Long> = input.last().trim().split(" ").map { it.toLong() }
    return Day07.Equation(input.first().toLong(), components)
}