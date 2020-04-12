package core.analysis

import core.Line
import core.analysis.Convolution.add
import core.analysis.Convolution.mul
import core.analysis.Fourier.dftSeparate
import core.analysis.Fourier.idft

object Convolution {
    fun Line.deconvolution(other: Line): Line {
        val dft = this.dftSeparate() //a + ib
        val dft2 = other.dftSeparate() // c + id

        val newLineRe = DoubleArray(this.size)
        val newLineIm = DoubleArray(this.size)
        for (i in newLineRe.indices) {
            val a = dft.first[i]
            val b = dft.second[i]
            val c = dft2.first[i]
            val d = dft2.second[i]
            newLineRe[i] = (a * c + b * d) /
                    (c * c + d * d)

            newLineIm[i] = (b * c - a * d) /
                    (c * c + d * d)
        }

        return idft(Pair(newLineRe, newLineIm))
    }

    fun div(first: Pair<DoubleArray, DoubleArray>, second: Pair<DoubleArray, DoubleArray>)
            : Pair<DoubleArray, DoubleArray> {
        val size = first.first.size
        val newLineRe = DoubleArray(size)
        val newLineIm = DoubleArray(size)
        for (i in newLineRe.indices) {
            val a = first.first[i]
            val b = first.second[i]
            val c = second.first[i]
            val d = second.second[i]
            newLineRe[i] = (a * c + b * d) /
                    (c * c + d * d)

            newLineIm[i] = (b * c - a * d) /
                    (c * c + d * d)
        }
        return Pair(newLineRe, newLineIm)
    }

    fun mul(first: Pair<DoubleArray, DoubleArray>, second: Pair<DoubleArray, DoubleArray>)
            : Pair<DoubleArray, DoubleArray> {
        val size = first.first.size
        val newLineRe = DoubleArray(size)
        val newLineIm = DoubleArray(size)
        for (i in newLineRe.indices) {
            val a = first.first[i]
            val b = first.second[i]
            val c = second.first[i]
            val d = second.second[i]
            newLineRe[i] = a * c - b * d
            newLineIm[i] = a * d + b * c
        }
        return Pair(newLineRe, newLineIm)
    }

    fun DoubleArray.div(number: Double): DoubleArray {
        val new = this.copyOf()
        new.forEachIndexed { index, value ->
            new[index] = value / number
        }
        return new
    }
    fun DoubleArray.div(array: DoubleArray): DoubleArray {
        val new = this.copyOf()
        new.forEachIndexed { index, value ->
            new[index] = value / array[index]
        }
        return new
    }
    fun DoubleArray.mul(number: Double): DoubleArray {
        val new = this.copyOf()
        new.forEachIndexed { index, value ->
            new[index] = value * number
        }
        return new
    }
    fun DoubleArray.mul(other: DoubleArray): DoubleArray {
        val new = this.copyOf()
        new.forEachIndexed { index, value ->
            new[index] = value * other[index]
        }
        return new
    }
    fun DoubleArray.add(number: Double): DoubleArray {
        val new = this.copyOf()
        new.forEachIndexed { index, value ->
            new[index] = value + number
        }
        return new
    }
    fun DoubleArray.add(other: DoubleArray): DoubleArray {
        val new = this.copyOf()
        new.forEachIndexed { index, value ->
            new[index] = value + other[index]
        }
        return new
    }

    fun convolution(one: Line, other: Line) = Line(one.xs, other.ys)

    fun convolution(one: Line, other: DoubleArray) = Line(one.xs, convolution(one.ys, other))

    fun convolution(one: DoubleArray, other: DoubleArray): DoubleArray {
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
}