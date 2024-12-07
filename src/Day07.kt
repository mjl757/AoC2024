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
        fun run(input: List<Equation>): Long = input.mapNotNull { testEquation(it) }.sum()

        fun testEquation(equation: Equation): Long? {
            val operatorsList = generateOperators(equation.components.size - 1)
            operatorsList.forEach { operators ->
                var total = 0L
                equation.components.forEachIndexed { index, component ->
                    if (index == 0) {
                        total += component
                    } else {
                        total = operators[index - 1].compute(total, component)
                    }
                    if (total > equation.answer) return@forEachIndexed
                    if (total == equation.answer) {
                        return equation.answer
                    }
                }
            }
            return null
        }

        fun generateOperators(numberOfOperators: Int): Set<List<Operator>> {
            val list = createOperators(numberOfOperators, listOf(), Operator.SUM, setOf())
            return createOperators(numberOfOperators, listOf(), Operator.MUL, list)
        }

        fun createOperators(
            numberOfOperators: Int,
            currentList: List<Operator>,
            value: Operator,
            listOfList: Set<List<Operator>>
        ): Set<List<Operator>> {
            if (currentList.size == numberOfOperators) {
                return listOfList.plus(element = currentList)
            }

            val sumList = createOperators(
                numberOfOperators,
                currentList.plus(value),
                Operator.SUM,
                listOfList
            )
            return createOperators(
                numberOfOperators,
                currentList.plus(value),
                Operator.MUL,
                sumList
            )
        }
    }

    object Part2 {
        fun run(input: List<Equation>) {

        }
    }

    data class Equation(
        val answer: Long,
        val components: List<Long>
    )

    enum class Operator {
        SUM,
        MUL;

        fun compute(first: Long, second: Long): Long {
            return when (this) {
                SUM -> first + second
                MUL -> first.times(second)
            }
        }
    }
}

private fun String.toEquation(): Day07.Equation {
    val input = this.split(":", limit = 2)
    val components: List<Long> = input.last().trim().split(" ").map { it.toLong() }
    return Day07.Equation(input.first().toLong(), components)
}