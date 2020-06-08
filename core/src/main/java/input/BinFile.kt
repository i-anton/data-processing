package core.input

import core.Line
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

object BinFile {
    fun readFloatsLine(filename: String): Line = Line(readFloatsArray(filename))

    fun readFloatsArray(filename: String, count: Int = 0): FloatArray =
            FileChannel.open(Paths.get(filename), StandardOpenOption.READ).use { ch ->
                val channelElementsCount = ch.size().toInt() / 4
                val bufferSize = when (count) {
                    0 -> channelElementsCount
                    else -> count
                }
                val readBytes = when {
                    channelElementsCount < bufferSize -> channelElementsCount
                    else -> bufferSize
                } * 4L
                val resArr = FloatArray(bufferSize)
                ch.map(FileChannel.MapMode.READ_ONLY, 0, readBytes)
                        .order(ByteOrder.LITTLE_ENDIAN)
                        .asFloatBuffer()
                        .get(resArr, 0, readBytes.toInt() / 4)
                return resArr
            }

    fun readFloatsMatrix(filename: String, width: Int, height: Int, arraySize: Int = width): Array<FloatArray> {
        FileChannel.open(Paths.get(filename), StandardOpenOption.READ).use { ch ->
            val size = ch.size() / 4
            require(size >= width * height) {
                "File size is $size but expected $width x $height = ${width * height}"
            }
            return Array(height) { idx ->
                val resArr = FloatArray(arraySize)
                ch.map(FileChannel.MapMode.READ_ONLY, idx * width.toLong() * 4, width.toLong() * 4)
                        .order(ByteOrder.LITTLE_ENDIAN)
                        .asFloatBuffer()
                        .get(resArr)
                resArr
            }
        }
    }
}
