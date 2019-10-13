package data.analysis

import data.Line
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
            dispResult.ys[i] = LineStatistics.variance(line, startIdx, endIdx)
            avgResult.xs[i] = startIdx.toDouble()
            avgResult.ys[i] = LineStatistics.avg(line, startIdx, endIdx)
            errorResult.xs[i] = startIdx.toDouble()
            errorResult.ys[i] = error * ampl
            startIdx += pointsInInterval
        }
        return listOf(dispResult, avgResult,errorResult)
    }


}