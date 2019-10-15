package data.analysis

import data.Line
import kotlin.math.abs
import kotlin.math.min

object CompositeStatistics {
    fun dataPerInterval(line: Line, intervalsCount: Int, error: Double): List<Line> {
        val dispResult = Line(intervalsCount)
        val avgResult = Line(intervalsCount)
        val errorResult = Line(intervalsCount)
        val size = line.size
        val pointsInInterval = size / intervalsCount
        var startIdx = 0
        var endIdx: Int
        val ampl = LineStatistics.amplitude(line, 0, size)
        for (i in 0 until intervalsCount) {
            endIdx = min(startIdx + pointsInInterval, size)
            dispResult.xs[i] = startIdx.toDouble()
            dispResult.ys[i] = LineStatistics.disp(line, startIdx, endIdx)
            avgResult.xs[i] = startIdx.toDouble()
            avgResult.ys[i] = LineStatistics.avg(line, startIdx, endIdx)
            errorResult.xs[i] = startIdx.toDouble()
            errorResult.ys[i] = error * ampl
            startIdx += pointsInInterval
        }
        return listOf(dispResult, avgResult,errorResult)
    }

    fun isStationary(line: Line, intervalCount: Int, deltaPercent: Double): Boolean {
        val delta = LineStatistics.amplitude(line) * deltaPercent
        val intervalSize = line.size / intervalCount

        val disps = DoubleArray(intervalCount)
        val avgs = DoubleArray(intervalCount)

        for (intervalIdx in 0 until intervalCount){
            val start = intervalIdx * intervalSize
            val end = min(start + intervalSize,line.size)

            disps[intervalIdx] = LineStatistics.disp(line, start, end)
            avgs[intervalIdx] = LineStatistics.avg(line, start, end)
        }

        for (i in 0 until intervalCount-1) {
            if ((abs(disps[i] - disps[i+1]) > delta) or
                    (abs(avgs[i] - avgs[i+1]) > delta))
                return true
        }
        return false
    }
}