import Day08.Part1
import Day08.Part2
import com.soberg.aoc.utlities.datastructures.Grid2D
import com.soberg.aoc.utlities.datastructures.toGrid2D

fun main() {
    val input = readInput("Day08")
    val grid = input.toGrid2D {
        it.toList()
    }
    println("Part 1: ${Part1.run(grid)}")
    println("Part 2: ${Part2.run(grid)}")
}

private object Day08 {
    object Part1 {
        fun run(grid: Grid2D<Char>): Int {
            val antennas = getAntennaSet(grid)
            val antinodeSet: MutableSet<Grid2D.Location> = mutableSetOf()
            antennas.entries.forEach { entry ->
                antinodeSet.addAll(getAntinodeCount(grid, entry.value))
            }
            return antinodeSet.size
        }

        fun getAntinodeCount(grid: Grid2D<Char>, antennaLocations: Set<Grid2D.Location>): List<Grid2D.Location> {
            val antinodeLocations: MutableSet<Grid2D.Location> = mutableSetOf()
            antennaLocations.forEachIndexed { i, location1 ->
                val subSet = antennaLocations.filterIndexed { index, _ ->
                    index > i
                }
                subSet.forEachIndexed { j, location2 ->
                    val differenceRow = location1.row - location2.row
                    val differenceCol = location1.col - location2.col
                    antinodeLocations.add(
                        Grid2D.Location(
                            row = location1.row + differenceRow,
                            col = location1.col + differenceCol
                        )
                    )
                    antinodeLocations.add(
                        Grid2D.Location(
                            row = location2.row - differenceRow,
                            col = location2.col - differenceCol
                        )
                    )
                }
            }
            val filteredLocations = antinodeLocations.filter { grid.isInBounds(it) }
            return filteredLocations
        }

        fun getAntennaSet(grid: Grid2D<Char>): Map<Char, Set<Grid2D.Location>> {
            return buildMap {
                grid.traverse { location ->
                    val value = grid get location
                    if (value != '.') {
                        val locations = get(value) ?: setOf()
                        put(value, locations.plus(location))
                    }
                }
            }

        }
    }

    object Part2 {
        fun run(grid: Grid2D<Char>) {

        }
    }

}


