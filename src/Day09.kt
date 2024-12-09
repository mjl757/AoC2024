import Day09.Part1
import Day09.Part2
import Day09.convertDiskToFileList

fun main() {
    val input = readInput("Day09").joinToString()
    val disk = input.convertDiskToFileList()
    println("Part 1: ${Part1.run(disk)}")
    println("Part 2: ${Part2.run(disk)}")
}

private object Day09 {
    object Part1 {
        fun run(input: List<Int>): Long {
            val sortedBlocks = sortFileBlocks(input)
            var total = 0L
            for ((index, value) in sortedBlocks.withIndex()) {
                if (value.isFreeSpace()) {
                    break
                }
                total += index * value
            }
            return total
        }

        fun sortFileBlocks(disk: List<Int>): List<Int> {
            val mutableDisk = disk.toMutableList()
            var isSorted = false
            while (!isSorted) {
                val firstFreeSpace = mutableDisk.indexOfFirst { it.isFreeSpace() }
                val lastUsedSpace = mutableDisk.indexOfLast { !it.isFreeSpace() }
                if (firstFreeSpace > lastUsedSpace) {
                    isSorted = true
                    continue
                }
                val firstValue = mutableDisk[firstFreeSpace]
                val lastValue = mutableDisk[lastUsedSpace]
                mutableDisk[firstFreeSpace] = lastValue
                mutableDisk[lastUsedSpace] = firstValue
            }
            return mutableDisk
        }
    }

    object Part2 {
        fun run(input: List<Int>): Long {
            val sortedBlocks = sortFileBlocks(input)
            var total = 0L
            for ((index, value) in sortedBlocks.withIndex()) {
                if (value.isFreeSpace()) {
                    continue
                }
                total += index * value
            }
            return total
        }

        fun sortFileBlocks(disk: List<Int>): List<Int> {
            val mutableDisk = disk.toMutableList()
            val fileBlocks = getFileBlocks(disk).reversed()
            fileBlocks.forEach { block ->
                val startIndex = mutableDisk.indexOfFirst { it == block.id }
                val freeSpaceIndex = findFreeSpaceForBlockSize(mutableDisk, block.blockSize, startIndex)
                if (freeSpaceIndex != null) {
                    for (i in 0..<block.blockSize) {
                        mutableDisk[startIndex+i] = -1
                        mutableDisk[freeSpaceIndex+i] = block.id
                    }
                }
            }
            return mutableDisk
        }

        /**
         * Returns the start index of free space large enough to support the block size. Null if it doesn't exist
         */
        fun findFreeSpaceForBlockSize(disk: List<Int>, neededBlockSize: Int, blockStartIndex: Int): Int? {
            var startIndex : Int? = null
            var blockSize = 0
            for ((index, value) in disk.withIndex()) {
                if (index >= blockStartIndex) {
                    return null
                }
                if (value.isFreeSpace()) {
                    if (blockSize == 0) {
                        startIndex = index
                    }
                    blockSize++
                } else {
                    blockSize = 0
                    startIndex = null
                }
                if (blockSize >= neededBlockSize) {
                    return startIndex
                }
            }
            return null

        }


        fun getFileBlocks(disk: List<Int>): List<FileBlock> = buildList {
            var currentId = disk.first()
            var blockSize = 0
            for (value in disk) {
                if (value == currentId) {
                    blockSize++
                } else {
                    if (!currentId.isFreeSpace()) {
                        add(FileBlock(currentId, blockSize))
                    }
                    currentId = value
                    blockSize = 1
                }
            }
            if (!currentId.isFreeSpace()) {
                add(FileBlock(currentId, blockSize))
            }

        }

        data class FileBlock(
            val id: Int,
            val blockSize: Int
        )
    }

    fun String.convertDiskToFileList(): List<Int> = buildList {
        var currentId = 0
        var isFile = true
        this@convertDiskToFileList.forEach { value ->
            val id = if (isFile) {
                currentId
            } else {
                -1
            }
            for (i in 1..value.digitToInt()) {
                add(id)
            }
            if (isFile) {
                currentId++
            }
            isFile = !isFile
        }
    }

    fun Int.isFreeSpace(): Boolean = this < 0
}

