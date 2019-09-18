package data.analysis

import data.Line
import kotlin.math.min

object Stationary {
    fun avg(line: Line, startIdx: Int, endIdx: Int): Double {
        var result = 0.0
        for (i in startIdx until endIdx) {
            result += line.getY(i)
        }
        return result / (endIdx - startIdx)
    }

    fun dispersion(line: Line, startIdx: Int, endIdx: Int): Double {
        var result = 0.0
        val avg = avg(line, startIdx, endIdx)
        for (i in startIdx until endIdx) {
            val value = line.getY(i)
            result += (value - avg) * (value - avg)
        }
        return result / (endIdx - startIdx)
    }

    fun amplitude(line: Line, startIdx: Int, endIdx: Int): Double {
        var minVal = line.getY(startIdx)
        var maxVal = line.getY(startIdx)
        for (i in startIdx + 1 until endIdx) {
            val curr = line.getY(i)
            if (curr > maxVal) maxVal = curr
            if (curr < minVal) minVal = curr
        }
        return maxVal - minVal
    }

    fun dataPerInterval(line: Line, intervalsCount: Int, error: Double): List<Line> {
        val dispResult = Line(intervalsCount)
        val avgResult = Line(intervalsCount)
        val errorResult = Line(intervalsCount)
        val size = line.size
        val pointsInInterval = size / intervalsCount

        var startIdx = 0
        var endIdx: Int
        val ampl = amplitude(line, 0, size)
        for (i in 0 until intervalsCount) {
            endIdx = min(startIdx + pointsInInterval, size)
            dispResult.setX(i, dispersion(line, startIdx, endIdx))
            avgResult.setX(i, avg(line, startIdx, endIdx))
            errorResult.setX(i, error * ampl)
            startIdx += pointsInInterval
        }
        return listOf(dispResult, avgResult,errorResult)
    }
}
