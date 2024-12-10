import Day10.Part1
import Day10.Part1.findPath
import Day10.Part2
import com.soberg.aoc.utlities.datastructures.Grid2D
import com.soberg.aoc.utlities.datastructures.toGrid2D

fun main() {
    val input = readInput("Day10")
    val grid = input.toGrid2D { row ->
        row.toList().map { it.digitToInt() }
    }
    println("Part 1: ${Part1.run(grid)}")
    println("Part 2: ${Part2.run(grid)}")

}

private object Day10 {
    object Part1 {
        fun run(input: Grid2D<Int>) : Int {
            var total = 0
            input.traverse {
                if (input[it] == 0) {
                    val score = findScore(it, input)
                    total += score
                }
            }
            return total
        }

        fun findScore(location: Grid2D.Location, grid: Grid2D<Int>): Int {
            val totalList = buildSet {
                addAll(findPath(grid, location.move(Grid2D.Direction.North), 0))
                addAll(findPath(grid, location.move(Grid2D.Direction.South), 0))
                addAll(findPath(grid, location.move(Grid2D.Direction.East), 0))
                addAll(findPath(grid, location.move(Grid2D.Direction.West), 0))
            }
            return totalList.size
        }

        fun findPath(grid: Grid2D<Int>, location: Grid2D.Location, previousValue: Int): List<Grid2D.Location> {
            if (!grid.isInBounds(location)) {
                return emptyList()
            }
            val currentValue = grid[location]
            if (currentValue - previousValue != 1) {
                return emptyList()
            }
            if (currentValue == 9) {
                return listOf(location)
            }
            return buildList {
                addAll(findPath(grid, location.move(Grid2D.Direction.North), currentValue))
                addAll(findPath(grid, location.move(Grid2D.Direction.South), currentValue))
                addAll(findPath(grid, location.move(Grid2D.Direction.East), currentValue))
                addAll(findPath(grid, location.move(Grid2D.Direction.West), currentValue))
            }
        }
    }

    object Part2 {
        fun run(input: Grid2D<Int>) : Int {
            var total = 0
            input.traverse {
                if (input[it] == 0) {
                    val score = findScore(it, input)
                    total += score
                }
            }
            return total
        }

        fun findScore(location: Grid2D.Location, grid: Grid2D<Int>): Int {
            val totalList = buildList {
                addAll(findPath(grid, location.move(Grid2D.Direction.North), 0))
                addAll(findPath(grid, location.move(Grid2D.Direction.South), 0))
                addAll(findPath(grid, location.move(Grid2D.Direction.East), 0))
                addAll(findPath(grid, location.move(Grid2D.Direction.West), 0))
            }
            return totalList.size
        }

    }
}