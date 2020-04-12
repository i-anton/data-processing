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
}