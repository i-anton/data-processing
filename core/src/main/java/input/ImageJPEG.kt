package core.input

import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB

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
        val elem = if (element >= 170.0) {
            element.toInt()
        } else {
            0
        }
        array[index] = elem + elem.shl(8) + elem.shl(16)
    }

    fun toBufferedImage(data: Array<DoubleArray>): BufferedImage {
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
}
