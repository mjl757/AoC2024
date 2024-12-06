import Day06.Part1
import Day06.Part2
import java.time.LocalTime

fun main() {
    val input = readInput("Day06")
    println("Part 1: ${Part1.run(input)}")
    val startTime = LocalTime.now()
    println("Part 2: ${Part2.run(input)}")
    val endTime = LocalTime.now()
    val difference = endTime.second - startTime.second
    println("Time to run part 2: $difference")
}

private object Day06 {
    object Part1 {
        fun run(input: List<String>): Int {
            val board = input.createGrid()
            return countSpaces(predictGuardsMovement(board))
        }

        fun countSpaces(board: Board): Int {
            var sum = 0
            board.area.forEach { row ->
                row.forEach {
                    if (it is GridItem.FreeSpace && it.hasBeenVisited || it is GridItem.Guard) {
                        sum++
                    }
                }
            }
            return sum
        }

        fun predictGuardsMovement(board: Board): Board {
            var updatedBoard = board
            var nextPosition: GridItem? = board.guard
            while (nextPosition != null) {
                nextPosition = checkGuardsNextPosition(updatedBoard)
                when (nextPosition) {
                    is GridItem.FreeSpace -> {
                        updatedBoard = moveGuard(updatedBoard)
                    }

                    GridItem.Obstacle -> {
                        updatedBoard = rotateGuard(updatedBoard)
                    }

                    else -> { /*no op*/
                    }
                }

            }
            return updatedBoard
        }

        fun checkGuardsNextPosition(board: Board): GridItem? {
            val nextPoint = getNextPoint(board.guard)
            if (!nextPoint.isInBounds(board.area.first().lastIndex, board.area.lastIndex)) {
                return null
            }
            return board.area[nextPoint.y][nextPoint.x]
        }

        fun moveGuard(board: Board): Board {
            val currentPoint = board.guard.point
            val nextPoint = getNextPoint(board.guard)
            val newGuard = board.guard.copy(point = nextPoint)
            val mutableArea = board.area.map { it.toMutableList() }.toMutableList()
            mutableArea[currentPoint.y][currentPoint.x] = GridItem.FreeSpace(true)
            mutableArea[nextPoint.y][nextPoint.x] = newGuard
            return Board(mutableArea, newGuard)
        }

        fun rotateGuard(board: Board): Board {
            return board.copy(guard = board.guard.copy(direction = board.guard.direction.turn()))
        }
    }

    /**
     * 646 is to low
     */
    object Part2 {
        fun run(input: List<String>): Int {
            val board = input.createGrid()
            val guardPath = Part1.predictGuardsMovement(board)
            val boards = getPotentialBoards(guardPath, board)
            var sum = 0
            boards.forEach {
                if (doesBoardLoop(it)) {
                    sum++
                }
            }
            return sum
        }

        fun getPotentialBoards(pathedBoard: Board, board: Board): List<Board> {
            return buildList {
                pathedBoard.area.forEachIndexed { y, row ->
                    row.forEachIndexed { x, item ->
                        if (item is GridItem.FreeSpace && item.hasBeenVisited || item is GridItem.Guard) {
                            val mutableArea = board.area.map { it.toMutableList() }.toMutableList()
                            mutableArea[y][x] = GridItem.Obstacle
                            add(board.copy(area = mutableArea))
                        }
                    }
                }
            }
        }

        fun doesBoardLoop(board: Board): Boolean {
            var updatedBoard = board
            var nextPosition: GridItem? = board.guard
            val obstacleMap: MutableList<Pair<Point, Direction>> = mutableListOf()
            while (nextPosition != null) {
                nextPosition = Part1.checkGuardsNextPosition(updatedBoard)
                when (nextPosition) {
                    is GridItem.FreeSpace -> {
                        updatedBoard = Part1.moveGuard(updatedBoard)
                    }

                    GridItem.Obstacle -> {
                        val nextPoint = getNextPoint(updatedBoard.guard)
                        val encounteredObstacle = nextPoint to updatedBoard.guard.direction
                        if (obstacleMap.contains(encounteredObstacle)) {
                            return true
                        }
                        obstacleMap.add(encounteredObstacle)
                        updatedBoard = Part1.rotateGuard(updatedBoard)
                    }

                    else -> { /*no op*/
                    }
                }
                // updatedBoard.println()
            }
            return false
        }
    }

    fun getNextPoint(guard: GridItem.Guard): Point {
        val point = guard.point
        return when (guard.direction) {
            Direction.UP -> Point(point.x, point.y - 1)
            Direction.DOWN -> Point(point.x, point.y + 1)
            Direction.LEFT -> Point(point.x - 1, point.y)
            Direction.RIGHT -> Point(point.x + 1, point.y)
        }
    }

    fun List<String>.createGrid(): Board {
        val stringList = this
        lateinit var guard: GridItem.Guard
        val area = buildList {
            stringList.forEachIndexed { y, row ->
                add(buildList {
                    row.forEachIndexed { x, column ->
                        val gridItem = column.toGridItem(x, y)
                        add(gridItem)
                        if (gridItem is GridItem.Guard) {
                            guard = gridItem
                        }
                    }
                }
                )
            }
        }
        return Board(area, guard)
    }

    fun Char.toGridItem(x: Int, y: Int): GridItem {
        val direction = Direction.getDirectionBySymbol(this)
        return when {
            direction != null -> GridItem.Guard(direction, Point(x, y))
            this == '#' -> GridItem.Obstacle
            else -> GridItem.FreeSpace()
        }
    }

    sealed class GridItem {
        data class Guard(val direction: Direction, val point: Point) : GridItem() {
            override fun toString(): String {
                return direction.symbol.toString()
            }
        }

        data class FreeSpace(val hasBeenVisited: Boolean = false) : GridItem() {
            override fun toString(): String {
                return if (hasBeenVisited) "x" else "."
            }
        }

        data object Obstacle : GridItem() {
            override fun toString(): String {
                return "#"
            }
        }
    }

    data class Board(val area: List<List<GridItem>>, val guard: GridItem.Guard) {
        override fun toString(): String {
            return buildString {
                area.forEach {
                    append("\n")
                    append(it)
                }
            }
        }
    }

    data class Point(val x: Int, val y: Int) {
        fun isInBounds(maxX: Int, maxY: Int): Boolean {
            return x in 0..maxX && y in 0..maxY
        }
    }

    enum class Direction(val symbol: Char) {
        UP('^'),
        DOWN('v'),
        LEFT('<'),
        RIGHT('>');

        fun turn(): Direction {
            return when (this) {
                UP -> RIGHT
                DOWN -> LEFT
                LEFT -> UP
                RIGHT -> DOWN
            }
        }

        companion object {
            fun getDirectionBySymbol(symbol: Char): Direction? {
                return entries.firstOrNull { it.symbol == symbol }
            }
        }
    }
}