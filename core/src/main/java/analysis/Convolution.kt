package core.analysis

import core.Line
import core.analysis.Fourier.dftSeparate
import core.analysis.Fourier.idft
import core.model.div

object Convolution {
    fun Line.deconvolution(other: Line) =
            idft(div(this.dftSeparate(), other.dftSeparate()))

    fun convolution(one: Line, other: Line) = Line(one.xs, other.ys)

    fun convolution(one: Line, other: DoubleArray) = Line(one.xs, convolution(one.ys, other))

    fun convolution(one: DoubleArray, other: DoubleArray): DoubleArray {
        val pre = convolutionBoundary(one, other)
        return pre.copyOfRange(other.size/2, one.size + other.size/2)
    }

    fun convolutionBoundary(one: DoubleArray, other: DoubleArray): DoubleArray {
        val n = one.size
        val m = other.size
        return DoubleArray(n + m) {
            var result = 0.0
            for (j in other.indices) if (it - j in one.indices) {
                result += other[j] * one[it - j]
            }
            result
        }
    }
    fun convolution(image: Array<DoubleArray>, kernel: Array<DoubleArray>): Array<DoubleArray> {
        var kernelSum = kernel.sumByDouble { it.sum() }
        if (kernelSum == 0.0) kernelSum = 1.0
        val kHeightHalf = kernel.size / 2
        val kWidthHalf = kernel[0].size / 2
        val iHeight = image.size
        val iWidth = image[0].size
        val result = Array(image.size) { DoubleArray(image[0].size) }
        for (y in (0 until iHeight)) {
            for (x in (0 until iWidth)) {
                var weightedPixelSum = 0.0
                for (ky in (-kHeightHalf..kHeightHalf)) {
                    for (kx in (-kWidthHalf..kWidthHalf)) {
                        var pixel = 0.0
                        val pixelY = y + ky
                        val pixelX = x + kx
                        if ((pixelY >= 0) and (pixelY < iHeight) and (pixelX >= 0) and (pixelX < iWidth)) {
                            pixel = image[pixelY][pixelX]
                        }
                        val weight = kernel[ky + kHeightHalf][kx + kWidthHalf]
                        weightedPixelSum += pixel * weight

                    }
                }
                result[y][x] = weightedPixelSum / kernelSum
            }
        }
        return result
    }
}