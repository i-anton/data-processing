package core.analysis

import core.Line
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object Fourier {
    fun Line.dft() = Line(size) { k ->
        val multiplier = (2.0 * Math.PI * k) / size
        var sumReal = 0.0
        var sumImag = 0.0
        ys.forEachIndexed { t, y ->
            val angle = multiplier * t
            val cos = cos(angle)
            val sin = sin(angle)
            sumReal += y * cos
            sumImag += y * sin
        }
        sumReal /= size
        sumImag /= size

        sqrt(sumReal * sumReal + sumImag * sumImag)
    }

    fun dft(data: FloatArray): Pair<DoubleArray, DoubleArray> {
        val size = data.size
        val reals = DoubleArray(size)
        val imags = DoubleArray(size)
        for (k in 0 until size) {
            val multiplier = (2.0 * Math.PI * k) / size
            var sumReal = 0.0
            var sumImag = 0.0
            data.forEachIndexed { t, y ->
                val angle = multiplier * t
                sumReal += y * cos(angle)
                sumImag += y * sin(angle)
            }
            reals[k] = sumReal / size
            imags[k] = sumImag / size
        }
        return Pair(reals, imags)
    }

    fun Line.dftRemap(rate: Double) =
            Line(size / 2, { it * rate / size }, ys)

    fun Line.dftSeparate(): Pair<DoubleArray, DoubleArray> {
        val reals = DoubleArray(size)
        val imags = DoubleArray(size)
        for (k in 0 until size) {
            val multiplier = (2.0 * Math.PI * k) / size
            var sumReal = 0.0
            var sumImag = 0.0
            ys.forEachIndexed { t, y ->
                val angle = multiplier * t
                sumReal += y * cos(angle)
                sumImag += y * sin(angle)
            }
            reals[k] = sumReal / size
            imags[k] = sumImag / size
        }
        return Pair(reals, imags)
    }

    fun toAmplitudes(data: Pair<DoubleArray, DoubleArray>): Line {
        val size = data.first.size
        val reals = data.first
        val imags = data.second
        return Line(size) { k ->
            val real = reals[k]
            val imag = imags[k]
            sqrt(real * real + imag * imag)
        }
    }

    fun idft(data: Pair<DoubleArray, DoubleArray>): Line {
        val size = data.first.size
        val reals = data.first
        val imags = data.second
        return Line(size) { k ->
            val multiplier = (2.0 * Math.PI * k) / size
            var sum = 0.0
            for (t in 0 until size) {
                val angle = multiplier * t
                sum += reals[t] * cos(angle) + imags[t] * sin(angle)
            }
            sum
        }
    }

    fun idftD(data: Pair<DoubleArray, DoubleArray>): DoubleArray {
        val size = data.first.size
        val reals = data.first
        val imags = data.second
        return DoubleArray(size) { k ->
            val multiplier = (2.0 * Math.PI * k) / size
            var sum = 0.0
            for (t in 0 until size) {
                val angle = multiplier * t
                sum += reals[t] * cos(angle) + imags[t] * sin(angle)
            }
            sum
        }
    }
}