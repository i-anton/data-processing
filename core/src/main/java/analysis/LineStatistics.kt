package core.analysis

import core.Line
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

private fun statisticsMoment(order: Int, line: Line, startIdx: Int = 0, endIdx: Int = line.size): Double {
    val avg = line.mean(startIdx, endIdx)
    return (startIdx until endIdx)
            .sumByDouble { (line.ys[it] - avg).pow(order) } /
            (endIdx - startIdx)
}

fun Line.mean(startIdx: Int = 0, endIdx: Int = this.size) =
        (startIdx until endIdx).sumByDouble { this.ys[it] } / (endIdx - startIdx)

fun Line.stdAbsDev(startIdx: Int = 0, endIdx: Int = this.size): Double {
    val avg = mean(startIdx, endIdx)
    return (startIdx until endIdx)
            .sumByDouble { abs(this.ys[it] - avg) } /
            (endIdx - startIdx)
}

fun Line.variance(startIdx: Int = 0, endIdx: Int = this.size) = statisticsMoment(2, this, startIdx, endIdx)

fun Line.skewness(startIdx: Int = 0, endIdx: Int = this.size) = statisticsMoment(3, this, startIdx, endIdx)

fun Line.excess(startIdx: Int = 0, endIdx: Int = this.size) = statisticsMoment(4, this, startIdx, endIdx)

fun Line.stdDev(startIdx: Int = 0, endIdx: Int = this.size) = sqrt(variance(startIdx, endIdx))

fun Line.amplitude(startIdx: Int = 0, endIdx: Int = this.size) =
        max(startIdx, endIdx) - min(startIdx, endIdx)

fun Line.min(startIdx: Int = 0, endIdx: Int = this.size): Double {
    var minVal = this.ys[startIdx]
    for (i in startIdx + 1 until endIdx) {
        val curr = this.ys[i]
        if (curr < minVal) minVal = curr
    }
    return minVal
}

fun Line.max(startIdx: Int = 0, endIdx: Int = this.size): Double {
    var maxVal = this.ys[startIdx]
    for (i in startIdx + 1 until endIdx) {
        val curr = this.ys[i]
        if (curr > maxVal) maxVal = curr
    }
    return maxVal
}

fun Line.midSquare(startIdx: Int = 0, endIdx: Int = this.size) =
        (startIdx until endIdx)
                .sumByDouble { this.ys[it].pow(2) } /
                (endIdx - startIdx)

fun Line.midSquareError(startIdx: Int = 0, endIdx: Int = this.size) =
        sqrt(midSquare(startIdx, endIdx))

fun Line.kurtosis(startIdx: Int = 0, endIdx: Int = this.size): Double {
    val variance = variance(startIdx, endIdx)
    val excess = excess(startIdx, endIdx)
    return (endIdx - startIdx) * (excess / (variance * variance)) - 3
}
