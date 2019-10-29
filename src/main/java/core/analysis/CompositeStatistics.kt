package core.analysis

import core.Line
import core.analysis.LineStatistics.avg
import core.analysis.LineStatistics.max
import core.analysis.LineStatistics.min
import java.util.*
import kotlin.math.*

object CompositeStatistics {
    fun dataPerInterval(line: Line, intervalsCount: Int, error: Double): List<Line> {
        val dispResult = Line(intervalsCount)
        val avgResult = Line(intervalsCount)
        val errorResult = Line(intervalsCount)
        val size = line.size
        val pointsInInterval = size / intervalsCount
        var startIdx = 0
        val ampl = LineStatistics.amplitude(line, 0, size)
        for (i in 0 until intervalsCount) {
            val endIdx = min(startIdx + pointsInInterval, size)
            dispResult.xs[i] = startIdx.toDouble()
            dispResult.ys[i] = LineStatistics.disp(line, startIdx, endIdx)
            avgResult.xs[i] = startIdx.toDouble()
            avgResult.ys[i] = avg(line, startIdx, endIdx)
            errorResult.xs[i] = startIdx.toDouble()
            errorResult.ys[i] = error * ampl
            startIdx += pointsInInterval
        }
        return listOf(dispResult, avgResult, errorResult)
    }

    fun isStationary(line: Line, intervalCount: Int, deltaPercent: Double): Boolean {
        val delta = LineStatistics.amplitude(line) * deltaPercent
        val intervalSize = line.size / intervalCount

        val disps = DoubleArray(intervalCount)
        val avgs = DoubleArray(intervalCount)

        for (intervalIdx in 0 until intervalCount) {
            val start = intervalIdx * intervalSize
            val end = min(start + intervalSize, line.size)

            disps[intervalIdx] = LineStatistics.disp(line, start, end)
            avgs[intervalIdx] = avg(line, start, end)
        }

        return (0 until intervalCount - 1).none {
            (abs(disps[it] - disps[it + 1]) > delta) or
                    (abs(avgs[it] - avgs[it + 1]) > delta)
        }
    }

    fun valuesDistribution(line: Line, intervalsCount: Int): Line {
        val min = min(line)
        val max = max(line)
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
        val xs = DoubleArray(intervalsCount)
        for (index in xs.indices)
            xs[index] = map.getOrDefault(ys[index], 0).toDouble()
        return Line(xs, ys)
    }

    fun autoCorrelation(line: Line, start: Int = 0, end: Int = line.size): Line {
        val avg = avg(line, start, end)
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
        val avgFirst = avg(first)
        val avgSecond = avg(second)
        val divider =
                sqrt(first.ys.sumByDouble { (it - avgFirst).pow(2.0) }) *
                        sqrt(second.ys.sumByDouble { (it - avgSecond).pow(2.0) })

        return Line(DoubleArray(first.size) {
            (0 until first.size - it).fold(0.0) { sum, k ->
                sum + (first.ys[k] - avgFirst) * (second.ys[k + it] - avgSecond)
            } / divider
        })
    }

    fun dft(line: Line): Line {
        val n = line.size
        val arr = DoubleArray(n) { k ->
            var sumReal = 0.0
            var sumImag = 0.0
            for (t in 0 until n) {
                val angle = (2.0 * Math.PI * k * t) / n
                sumReal += line.ys[t] * cos(angle)
                sumImag += line.ys[t] * sin(angle)
            }
            sumReal /= n
            sumImag /= n

            sqrt(sumReal * sumReal + sumImag * sumImag)
        }
        return Line(arr)
    }

    fun dftRemap(line: Line, rate: Double): Line {
        val n = line.size
        return Line(DoubleArray(n / 2) { it * rate / n }, line.ys.copyOf(n / 2))
    }
}