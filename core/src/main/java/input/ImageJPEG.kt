package core.input

import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
import javax.imageio.ImageIO

object ImageJPEG {
    private fun FloatArray.copyInto(array: IntArray) = this.forEachIndexed { index, element ->
        val elem = element.toInt()
        array[index] = elem + elem.shl(8) + elem.shl(16)
    }

    fun toBufferedImage(data: Array<FloatArray>): BufferedImage {
        val width = data[0].size
        val height = data.size
        val image = BufferedImage(width, height, TYPE_INT_RGB)
        val intBuffer = IntArray(width)
        for (y in 0 until height) {
            data[y].copyInto(intBuffer)
            image.setRGB(0, y, width, 1, intBuffer, 0, width)
        }
        return image
    }

    private fun DoubleArray.copyInto(array: IntArray) = this.forEachIndexed { index, element ->
        val elem = element.toInt()
        array[index] = elem + elem.shl(8) + elem.shl(16)
    }
    private fun DoubleArray.copyIntoNorm(array: IntArray) = this.forEachIndexed { index, element ->
        val elem = kotlin.math.min(element.toInt(), 256)/2 + 128
        array[index] = elem + elem.shl(8) + elem.shl(16)
    }

    fun toBufferedImage(data: Array<DoubleArray>, isNormal: Boolean = false): BufferedImage {
        val width = data[0].size
        val height = data.size
        val image = BufferedImage(width, height, TYPE_INT_RGB)
        val intBuffer = IntArray(width)
        for (y in 0 until height) {
            if (isNormal) data[y].copyIntoNorm(intBuffer)
            else data[y].copyInto(intBuffer)
            image.setRGB(0, y, width, 1, intBuffer, 0, width)
        }
        return image
    }

    private fun IntArray.copyInto(data: DoubleArray) = this.forEachIndexed { index, element ->
        data[index] = (element and 0xFF).toDouble()
    }

    fun readFromFile(file: File): Array<DoubleArray> {
        val image = ImageIO.read(file)
        val width = image.width
        val height = image.height
        val bufferArray = IntArray(width)
        return Array(height) { y ->
            val row = image.getRGB(0, y, width, 1, bufferArray, 0, 0)
            val doubleRow = DoubleArray(width)
            row.copyInto(doubleRow)
            doubleRow
        }
    }
}
