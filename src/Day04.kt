typealias GridD4 = List<List<Char>>

fun main() {
    val input = readInput("Day04").createGrid()
    println("Part 1: ${Day04.Part1.run(input)}")
    println("Part 2: ${Day04.Part2.run(input)}")
}

fun List<String>.createGrid(): GridD4 {
    val stringList = this
    return buildList {
        stringList.forEach { row ->
            add(row.toCharArray().toList())
        }
    }
}

private object Day04 {
    object Part1 {
        fun run(input: GridD4): Int {
            return getXMasCount(input)
        }

        private fun getXMasCount(grid: GridD4): Int {
            var total = 0
            grid.forEachIndexed { xIndex, row ->
                row.forEachIndexed { yIndex, value ->
                    if (value == 'X') {
                        total += checkPoint(xIndex, yIndex, grid)
                    }
                }
            }
            return total
        }

        private fun checkPoint(xIndex: Int, yIndex: Int, input: GridD4): Int {
            var total = 0
            for (direction in Direction.entries) {
                if (direction.isXMas(xIndex, yIndex, input)) {
                    total++
                }
            }
            return total
        }

        fun Direction.isXMas(xIndex: Int, yIndex: Int, input: GridD4): Boolean {
            val mPair: Pair<Int, Int>
            val aPair: Pair<Int, Int>
            val sPair: Pair<Int, Int>
            when (this) {
                Direction.UP -> {
                    mPair = xIndex to yIndex - 1
                    aPair = xIndex to yIndex - 2
                    sPair = xIndex to yIndex - 3
                }
                Direction.DOWN -> {
                    mPair = xIndex to yIndex + 1
                    aPair = xIndex to yIndex + 2
                    sPair = xIndex to yIndex + 3
                }
                Direction.LEFT -> {
                    mPair = xIndex - 1 to yIndex
                    aPair = xIndex - 2 to yIndex
                    sPair = xIndex - 3 to yIndex
                }
                Direction.RIGHT -> {
                    mPair = xIndex + 1 to yIndex
                    aPair = xIndex + 2 to yIndex
                    sPair = xIndex + 3 to yIndex
                }
                Direction.UL -> {
                    mPair = xIndex - 1 to yIndex - 1
                    aPair = xIndex - 2 to yIndex - 2
                    sPair = xIndex - 3 to yIndex - 3
                }
                Direction.UR -> {
                    mPair = xIndex + 1 to yIndex - 1
                    aPair = xIndex + 2 to yIndex - 2
                    sPair = xIndex + 3 to yIndex - 3
                }
                Direction.DL -> {
                    mPair = xIndex - 1 to yIndex + 1
                    aPair = xIndex - 2 to yIndex + 2
                    sPair = xIndex - 3 to yIndex + 3
                }
                Direction.DR -> {
                    mPair = xIndex + 1 to yIndex + 1
                    aPair = xIndex + 2 to yIndex + 2
                    sPair = xIndex + 3 to yIndex + 3
                }
            }
            return try {
                input[mPair.first][mPair.second] == 'M'
                        && input[aPair.first][aPair.second] == 'A'
                        && input[sPair.first][sPair.second] == 'S'
            } catch(e: Exception) {
                false
            }
        }
    }

    object Part2 {
        fun run(input: GridD4): Int {
            return getXMasCount(input)
        }

        private fun getXMasCount(grid: GridD4): Int {
            var total = 0
            grid.forEachIndexed { xIndex, row ->
                row.forEachIndexed { yIndex, value ->
                    try {
                        if (value == 'A' && checkPoint(xIndex, yIndex, grid)) {
                            total++
                        }
                    } catch (_: Exception) {

                    }
                }
            }
            return total
        }

        private fun checkPoint(xIndex: Int, yIndex: Int, input: GridD4): Boolean {
            val ul = input[xIndex-1][yIndex-1]
            val ur = input[xIndex+1][yIndex-1]
            val dl = input[xIndex-1][yIndex+1]
            val dr = input[xIndex+1][yIndex+1]
            val corners = buildList {
                add(ul)
                add(ur)
                add(dl)
                add(dr)
            }

            return !(corners.contains('X') || corners.contains('A') || ul == dr || dl == ur)
        }

    }

    enum class Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        UL,
        UR,
        DL,
        DR
    }


}