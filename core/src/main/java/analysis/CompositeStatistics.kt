package core.analysis

import core.Line
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

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

    fun histogram(line: DoubleArray,
                  intervalsCount: Int,
                  min: Double = line.min()!!,
                  max: Double = line.max()!!,
                  resultBuffer: IntArray? = null): IntArray {
        require(line.isNotEmpty())
        require(intervalsCount > 0)
        val divider = if (min == max) 1.0 else max - min
        val intervalToRange = intervalsCount / divider
        val ys = resultBuffer ?: IntArray(intervalsCount + 1)
        val bufferSize = ys.size - 1
        line.forEach { value ->
            val index = ((value - min) * intervalToRange).roundToInt()
            val safeIndex = min(max(index, 0), bufferSize)
            ys[safeIndex]++
        }
        return ys
    }

    fun histogram(line: Array<DoubleArray>, intervalsCount: Int, min: Double, max: Double): IntArray {
        require(intervalsCount > 0)
        val accumulator = IntArray(intervalsCount + 1)
        val resultBuffer = IntArray(intervalsCount + 1)
        line.forEach {
            histogram(it, intervalsCount, min, max, resultBuffer)
            resultBuffer.forEachIndexed { index, value ->
                accumulator[index] = value + resultBuffer[index]
            }
        }
        return accumulator
    }

    fun histogram(line: Array<DoubleArray>, intervalsCount: Int): IntArray {
        require(intervalsCount > 0)
        var min = line[0][0]
        var max = line[0][0]
        line.forEach {
            val localMin = it.min()!!
            val localMax = it.max()!!
            if (min > localMin) min = localMin
            if (max < localMax) max = localMax
        }
        return histogram(line, intervalsCount, min, max)
    }
}