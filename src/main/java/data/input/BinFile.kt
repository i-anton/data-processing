package data.input

import data.Line
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

object BinFile {
    fun read(filename: String): Line =
            FileChannel.open(Paths.get(filename), StandardOpenOption.READ).use { ch ->
                val resArr = FloatArray(ch.size().toInt())
                ch.map(FileChannel.MapMode.READ_ONLY, 0, ch.size())
                        .order(ByteOrder.nativeOrder())
                        .asFloatBuffer()
                        .get(resArr)
                return Line(resArr)
            }

    fun save(line: Line, filename: String) {
        TODO()
    }
}
