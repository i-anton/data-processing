package data.analysis

import data.Line
import kotlin.math.abs
import kotlin.math.sqrt

object LineStatistics {
    fun avg(line: Line, startIdx: Int = 0, endIdx: Int = line.size): Double {
        var result = 0.0
        for (i in startIdx until endIdx) {
            result += line.ys[i]
        }
        return result / (endIdx - startIdx + 1)
    }

    fun variance(line: Line, startIdx: Int = 0, endIdx: Int = line.size): Double {
        var result = 0.0
        val avg = avg(line, startIdx, endIdx)
        for (i in startIdx until endIdx) {
            val value = line.ys[i] - avg
            result += value * value
        }
        return result / (endIdx - startIdx + 1)
    }

    fun stdDev(line: Line, startIdx: Int = 0, endIdx: Int = line.size) = sqrt(variance(line, startIdx, endIdx))

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

    fun assimetry(line: Line, startIdx: Int = 0, endIdx: Int = line.size): Double {
        var result = 0.0
        val avg = avg(line, startIdx, endIdx)
        for (i in startIdx until endIdx) {
            val value = line.ys[i] - avg
            result += value * value * value
        }
        return result / (endIdx - startIdx)
    }

    fun excess(line: Line, startIdx: Int = 0, endIdx: Int = line.size): Double {
        var result = 0.0
        val avg = avg(line, startIdx, endIdx)
        for (i in startIdx until endIdx) {
            val value = line.ys[i] - avg
            result += (value * value * value * value)
        }
        return result / (endIdx - startIdx + 1)
    }

    fun stdAbsDev(line: Line, startIdx: Int = 0, endIdx: Int = line.size): Double {
        var result = 0.0
        val avg = avg(line, startIdx, endIdx)
        for (i in startIdx until endIdx) {
            val value = line.ys[i] - avg
            result += abs(value)
        }
        return result / (endIdx - startIdx + 1)
    }

    fun midSquare(line: Line, startIdx: Int = 0, endIdx: Int = line.size): Double {
        var result = 0.0
        for (i in startIdx until endIdx) {
            val value = line.ys[i]
            result += value*value
        }
        return result / (endIdx - startIdx)
    }

    fun midSquareError(line: Line, startIdx: Int = 0, endIdx: Int = line.size) = sqrt(midSquare(line, startIdx,endIdx))

    fun kurtosis(line: Line, startIdx: Int = 0, endIdx: Int = line.size): Double {
        val variance = variance(line,startIdx,endIdx)
        val excess = excess(line,startIdx, endIdx)
        return (endIdx-startIdx+1)*(excess / (variance*variance))-3
    }
}
