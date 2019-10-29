package core.input

import core.Line
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

object BinFile {
    fun read(filename: String): Line =
            FileChannel.open(Paths.get(filename), StandardOpenOption.READ).use {
                val resArr = FloatArray(it.size().toInt())
                it.map(FileChannel.MapMode.READ_ONLY, 0, it.size())
                        .order(ByteOrder.nativeOrder())
                        .asFloatBuffer()
                        .get(resArr)
                return Line(resArr)
            }

    fun save(line: Line, filename: String) {
        TODO()
    }
}
