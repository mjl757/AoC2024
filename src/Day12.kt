import Day12.Part1
import Day12.Part1.buildRegions
import Day12.Part2
import com.soberg.aoc.utlities.datastructures.Grid2D

fun main() {
    val input = readInput("Day12")
    val grid = Grid2D(input.map { it.toList() })
    println("Part 1: ${Part1.run(grid)}")
    println("Part 2: ${Part2.run(grid)}")
}

private object Day12 {
    object Part1 {
        fun run(grid: Grid2D<Char>): Int {
            val regions = buildRegions(grid)
            return regions.sumOf { it.getPrice() }
        }

        fun buildRegions(grid: Grid2D<Char>): List<Region> {
            val regions: MutableList<Region> = mutableListOf()
            grid.traverse { location, element ->
                if (!regions.containsLocation(location)) {
                    regions.add(
                        buildRegion(grid, location, Region(element), Grid2D.Direction.North)
                    )
                }
            }
            return regions
        }

        fun buildRegion(
            grid: Grid2D<Char>,
            currentLocation: Grid2D.Location,
            currentRegion: Region,
            direction: Grid2D.Direction
        ): Region {
            if (currentRegion.hasLocation(currentLocation)) {
                return currentRegion
            }
            var newRegion = currentRegion

            if (!grid.isInBounds(currentLocation)) {
                return newRegion.copy(perimeters = newRegion.perimeters.plus(currentLocation to direction))
            }
            val crop = grid[currentLocation]
            if (crop != currentRegion.crop) {
                return newRegion.copy(perimeters = newRegion.perimeters.plus(currentLocation to direction))
            }
            newRegion = newRegion.copy(plots = newRegion.plots.plus(currentLocation))
            newRegion = buildRegion(
                grid,
                currentLocation.move(Grid2D.Direction.North),
                newRegion,
                Grid2D.Direction.North
            )
            newRegion = buildRegion(
                grid,
                currentLocation.move(Grid2D.Direction.South),
                newRegion,
                Grid2D.Direction.South
            )
            newRegion = buildRegion(
                grid,
                currentLocation.move(Grid2D.Direction.East),
                newRegion,
                Grid2D.Direction.East
            )
            newRegion = buildRegion(
                grid,
                currentLocation.move(Grid2D.Direction.West),
                newRegion,
                Grid2D.Direction.West
            )

            return newRegion
        }
    }

    object Part2 {
        fun run(grid: Grid2D<Char>): Int {
            val regions = buildRegions(grid)
            return regions.sumOf {
                it.getSidePrice()
            }
        }
    }

    data class Region(
        val crop: Char,
        val plots: Set<Grid2D.Location> = setOf(),
        val perimeters: Set<Pair<Grid2D.Location, Grid2D.Direction>> = setOf()
    ) {
        fun hasLocation(location: Grid2D.Location): Boolean {
            return plots.contains(location)
        }

        fun getPrice(): Int {
            return plots.size * perimeters.size
        }

        fun getSidePrice(): Int {
            val sides = getNumOfSides()
            return plots.size * sides
        }

        private fun getNumOfSides(): Int {
            val sides: MutableList<Set<Pair<Grid2D.Location, Grid2D.Direction>>> = mutableListOf()
            perimeters.forEach { perimeter ->
                if (!sides.containsPerimeter(perimeter)) {
                    sides.add(buildSides(perimeter, setOf()))
                }
            }
            return sides.size
        }

        private fun buildSides(
            currentPerimeter: Pair<Grid2D.Location, Grid2D.Direction>,
            currentSet: Set<Pair<Grid2D.Location, Grid2D.Direction>>
        ) : Set<Pair<Grid2D.Location, Grid2D.Direction>> {
            val mutableSet = currentSet.toMutableSet()
            if (!perimeters.containsPerimeter(currentPerimeter) || currentSet.containsPerimeter(currentPerimeter)) {
                return currentSet
            }
            mutableSet.add(currentPerimeter)
            val sideChecks = getNextAndPrevious(currentPerimeter)
            sideChecks.forEach {
                mutableSet.addAll(buildSides(it, mutableSet))
            }
            return mutableSet
        }
    }

    fun getNextAndPrevious(
        startingPerimeter: Pair<Grid2D.Location, Grid2D.Direction>
    ) : List<Pair<Grid2D.Location, Grid2D.Direction>> = buildList {
        if (startingPerimeter.second.isVertical()) {
            add(startingPerimeter.first.move(Grid2D.Direction.East) to startingPerimeter.second)
            add(startingPerimeter.first.move(Grid2D.Direction.West) to startingPerimeter.second)
        } else {
            add(startingPerimeter.first.move(Grid2D.Direction.North) to startingPerimeter.second)
            add(startingPerimeter.first.move(Grid2D.Direction.South) to startingPerimeter.second)
        }
    }

    fun Grid2D.Direction.isVertical() : Boolean{
        return this == Grid2D.Direction.North || this == Grid2D.Direction.South
    }

    fun List<Region>.containsLocation(location: Grid2D.Location): Boolean {
        forEach {
            if (it.hasLocation(location)) {
               return true
            }
        }
        return false
    }

    fun Set<Pair<Grid2D.Location, Grid2D.Direction>>.containsPerimeter(
        perimeter: Pair<Grid2D.Location, Grid2D.Direction>
    ): Boolean {
        forEach {
            if (it == perimeter) {
                return true
            }
        }
        return false
    }

    fun List<Set<Pair<Grid2D.Location, Grid2D.Direction>>>.containsPerimeter(
        perimeter: Pair<Grid2D.Location, Grid2D.Direction>
    ): Boolean {
        forEach { set ->
            if (set.containsPerimeter(perimeter)) {
                return true
            }
        }
        return false
    }
}