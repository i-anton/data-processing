package core.analysis

import core.Line
import kotlin.math.*

object CompositeStatistics {

    fun dataPerInterval(line: Line, intervalsCount: Int, error: Double): List<Line> {
        val dispResult = Line(intervalsCount)
        val avgResult = Line(intervalsCount)
        val errorResult = Line(intervalsCount)
        val size = line.size
        val pointsInInterval = size / intervalsCount
        val ampl = line.amplitude()
        for (i in 0 until intervalsCount) {
            val startIdx = i * pointsInInterval
            val endIdx = min(startIdx + pointsInInterval, size)
            dispResult.xs[i] = startIdx.toDouble()
            dispResult.ys[i] = line.variance(startIdx, endIdx)
            avgResult.xs[i] = startIdx.toDouble()
            avgResult.ys[i] = line.mean(startIdx, endIdx)
            errorResult.xs[i] = startIdx.toDouble()
            errorResult.ys[i] = error * ampl
        }
        return listOf(dispResult, avgResult, errorResult)
    }

    fun isStationary(line: Line, intervalCount: Int, deltaPercent: Double): Boolean {
        val delta = line.amplitude() * deltaPercent
        val intervalSize = line.size / intervalCount

        val disps = DoubleArray(intervalCount)
        val means = DoubleArray(intervalCount)

        for (intervalIdx in 0 until intervalCount) {
            val start = intervalIdx * intervalSize
            val end = min(start + intervalSize, line.size)

            disps[intervalIdx] = line.variance(start, end)
            means[intervalIdx] = line.mean(start, end)
        }

        return (0 until intervalCount - 1).none {
            (abs(disps[it] - disps[it + 1]) > delta) or
                    (abs(means[it] - means[it + 1]) > delta)
        }
    }

    fun valuesDistribution(line: Line, intervalsCount: Int): Line {
        val min = line.min()
        val max = line.max()
        val step = (max - min) / intervalsCount
        val ys = DoubleArray(intervalsCount) {
            val start = min + step * it
            val end = start + step
            var count = 0.0
            for (j in line.ys)
                if (j in start..end)
                    count++
            count
        }
        return Line(intervalsCount,  {
            min + step * (it + 1)
        }, ys)
    }

    fun autoCorrelation(line: Line, start: Int = 0, end: Int = line.size): Line {
        val avg = line.mean(start, end)
        val divider = (start until end)
                .sumByDouble { (line.ys[it] - avg).pow(2.0) }

        val array = DoubleArray(end - start + 1)
        for (funShift in start until end) {
            array[funShift - start] =
                    (0 until end - funShift).fold(0.0) { sum, k ->
                        sum + (line.ys[k] - avg) * (line.ys[k + funShift] - avg)
                    } / divider
        }
        return Line(array)
    }

    fun crossCorrelation(first: Line, second: Line): Line {
        require(first.size == second.size)
        val avgFirst = first.mean()
        val avgSecond = second.mean()
        val divider =
                sqrt(first.ys.sumByDouble { (it - avgFirst).pow(2.0) }) *
                        sqrt(second.ys.sumByDouble { (it - avgSecond).pow(2.0) })

        return Line(DoubleArray(first.size) {
            (0 until first.size - it).fold(0.0) { sum, k ->
                sum + (first.ys[k] - avgFirst) * (second.ys[k + it] - avgSecond)
            } / divider
        })
    }

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

    fun Line.hammingWindowed(alpha: Double = 0.46) =
            Line(xs) { ys[it] * (alpha - (1.0 - alpha) * cos(2.0 * Math.PI * it / size)) }

    fun Line.zero(from: Int, to: Int = size) = Line(xs) {
        when {
            (it >= from) and (it <= to) -> 0.0
            else -> ys[it]
        }
    }
}