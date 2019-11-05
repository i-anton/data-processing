package core.analysis

import core.Line
import java.util.*
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
        val map = TreeMap<Double, Int>()
        repeat(intervalsCount) {
            val start = min + step * it
            val end = start + step
            for (j in line.ys)
                if ((j >= start) and (j <= end))
                    map[end] = map.getOrDefault(end, 0) + 1
        }
        val ys = map.keys.toDoubleArray()
        val xs = DoubleArray(intervalsCount) {
            map.getOrDefault(ys[it], 0).toDouble()
        }
        return Line(xs, ys)
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
        var sumReal = 0.0
        var sumImag = 0.0
        for (t in 0 until size) {
            val angle = (2.0 * Math.PI * k * t) / size
            sumReal += ys[t] * cos(angle)
            sumImag += ys[t] * sin(angle)
        }
        sumReal /= size
        sumImag /= size

        sqrt(sumReal * sumReal + sumImag * sumImag)
    }

    fun Line.dftRemap(rate: Double) =
            Line(DoubleArray(size / 2) { it * rate / size }, ys.copyOf(size / 2))
}