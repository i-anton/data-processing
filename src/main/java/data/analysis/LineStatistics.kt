package data.analysis

import data.Line
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

object LineStatistics {
    fun avg(line: Line, startIdx: Int = 0, endIdx: Int = line.size) =
            (startIdx until endIdx).sumByDouble { line.ys[it] } / (endIdx - startIdx + 1)

    fun stdAbsDev(line: Line, startIdx: Int = 0, endIdx: Int = line.size): Double {
        val avg = avg(line, startIdx, endIdx)
        return (startIdx until endIdx)
                .sumByDouble { abs(line.ys[it] - avg) } /
                (endIdx - startIdx + 1)
    }

    fun disp(line: Line, startIdx: Int = 0, endIdx: Int = line.size): Double {
        val avg = avg(line, startIdx, endIdx)
        return (startIdx until endIdx)
                .sumByDouble { (line.ys[it] - avg).pow(2) } /
                (endIdx - startIdx + 1)
    }

    fun assimetry(line: Line, startIdx: Int = 0, endIdx: Int = line.size): Double {
        val avg = avg(line, startIdx, endIdx)
        return (startIdx until endIdx)
                .sumByDouble { (line.ys[it] - avg).pow(3) } /
                (endIdx - startIdx + 1)
    }

    fun excess(line: Line, startIdx: Int = 0, endIdx: Int = line.size): Double {
        val avg = avg(line, startIdx, endIdx)
        return (startIdx until endIdx)
                .sumByDouble { (line.ys[it] - avg).pow(4) } /
                (endIdx - startIdx + 1)
    }

    fun stdDev(line: Line, startIdx: Int = 0, endIdx: Int = line.size) = sqrt(disp(line, startIdx, endIdx))

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
                    (endIdx - startIdx + 1)

    fun midSquareError(line: Line, startIdx: Int = 0, endIdx: Int = line.size) = sqrt(midSquare(line, startIdx, endIdx))

    fun kurtosis(line: Line, startIdx: Int = 0, endIdx: Int = line.size): Double {
        val variance = disp(line, startIdx, endIdx)
        val excess = excess(line, startIdx, endIdx)
        return (endIdx - startIdx + 1) * (excess / (variance * variance)) - 3
    }
}
