package core.analysis

import core.Line
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

object LineStatistics {
    fun mean(line: Line, startIdx: Int = 0, endIdx: Int = line.size) =
            (startIdx until endIdx).sumByDouble { line.ys[it] } / (endIdx - startIdx)

    private fun statisticsMoment(order: Int, line: Line, startIdx: Int = 0, endIdx: Int = line.size): Double {
        val avg = mean(line, startIdx, endIdx)
        return (startIdx until endIdx)
                .sumByDouble { (line.ys[it] - avg).pow(order) } /
                (endIdx - startIdx)
    }

    fun stdAbsDev(line: Line, startIdx: Int = 0, endIdx: Int = line.size): Double {
        val avg = mean(line, startIdx, endIdx)
        return (startIdx until endIdx)
                .sumByDouble { abs(line.ys[it] - avg) } /
                (endIdx - startIdx)
    }

    fun variance(line: Line, startIdx: Int = 0, endIdx: Int = line.size)
            = statisticsMoment(2, line, startIdx, endIdx)

    fun skewness(line: Line, startIdx: Int = 0, endIdx: Int = line.size)
            = statisticsMoment(3, line, startIdx, endIdx)

    fun excess(line: Line, startIdx: Int = 0, endIdx: Int = line.size)
            = statisticsMoment(4, line, startIdx, endIdx)

    fun stdDev(line: Line, startIdx: Int = 0, endIdx: Int = line.size)
            = sqrt(variance(line, startIdx, endIdx))

    fun amplitude(line: Line, startIdx: Int = 0, endIdx: Int = line.size) =
            max(line, startIdx, endIdx) - min(line, startIdx, endIdx)

    fun min(line: Line, startIdx: Int = 0, endIdx: Int = line.size): Double {
        var minVal = line.ys[startIdx]
        for (i in startIdx + 1 until endIdx) {
            val curr = line.ys[i]
            if (curr < minVal) minVal = curr
        }
        return minVal
    }

    fun max(line: Line, startIdx: Int = 0, endIdx: Int = line.size): Double {
        var maxVal = line.ys[startIdx]
        for (i in startIdx + 1 until endIdx) {
            val curr = line.ys[i]
            if (curr > maxVal) maxVal = curr
        }
        return maxVal
    }

    fun midSquare(line: Line, startIdx: Int = 0, endIdx: Int = line.size) =
            (startIdx until endIdx)
                    .sumByDouble { line.ys[it].pow(2) } /
                    (endIdx - startIdx)

    fun midSquareError(line: Line, startIdx: Int = 0, endIdx: Int = line.size) =
            sqrt(midSquare(line, startIdx, endIdx))

    fun kurtosis(line: Line, startIdx: Int = 0, endIdx: Int = line.size): Double {
        val variance = variance(line, startIdx, endIdx)
        val excess = excess(line, startIdx, endIdx)
        return (endIdx - startIdx) * (excess / (variance * variance)) - 3
    }
}
