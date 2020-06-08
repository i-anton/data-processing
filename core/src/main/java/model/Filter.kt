package core.model

import core.Line
import core.analysis.mean
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Filter {
    fun Line.antiShift(): Line {
        val avg = mean()
        return Line(DoubleArray(size) { ys[it] - avg })
    }

    fun Line.antiSpike(absRange: Double): Line {
        val fixed = Line(this)
        for (i in 1 until size - 1) {
            if (abs(fixed.ys[i]) > absRange) {
                val next = fixed.ys[i + 1]
                val clippedNext = min(max(-1.0, next), 1.0)
                val mid = (fixed.ys[i - 1] + clippedNext) / 2
                fixed.ys[i] = mid
            }
        }
        return fixed
    }

    fun Line.antiSpikeWindowed(absRange: Double, windowSize: Int = 2): Line {
        val fixed = Line(this)
        fixed.ys.forEachIndexed { index, i ->
            if (abs(i) > absRange) {
                val winStart = max(0, index - windowSize)
                val winEnd = min(size, index + windowSize)
                fixed.ys[index] = (winStart until winEnd).sumByDouble {
                    min(max(-1.0, fixed.ys[it]), 1.0)
                } / (winEnd - winStart)
            }
        }
        return fixed
    }

    fun Line.antiTrend(windowSize: Int = 3): Line {
        val trendDetect = trendDetect(windowSize)
        return Line(size) { ys[it] - trendDetect[it] }
    }

    fun Line.trendDetect(windowSize: Int = 3) =
            DoubleArray(size) {
                val endIdx = min(it + windowSize, size)
                val startIdx = max(0, it - windowSize)
                mean(startIdx, endIdx)
            }
}