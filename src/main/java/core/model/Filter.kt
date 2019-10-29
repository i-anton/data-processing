package core.model

import core.Line
import core.analysis.LineStatistics
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Filter {
    fun antiShift(input: Line): Line {
        val avg = LineStatistics.avg(input)
        return Line(DoubleArray(input.size) {
            input.ys[it] - avg
        })
    }

    fun antiSpike(input: Line, absRange: Double): Line {
        val fixed = Line(input)
        for (i in 1 until input.size - 1) {
            if (abs(fixed.ys[i]) > absRange) {
                val next = fixed.ys[i + 1]
                val clippedNext = min(max(-1.0, next), 1.0)
                val mid = (fixed.ys[i - 1] + clippedNext) / 2
                fixed.ys[i] = mid
            }
        }
        return fixed
    }


    fun antiSpikeWindowed(input: Line, absRange: Double, windowSize: Int = 2): Line {
        val fixed = Line(input)
        for (i in input.xs.indices) {
            if (abs(fixed.ys[i]) > absRange) {
                val winStart = max(0, i - windowSize)
                val winEnd = min(input.size, i + windowSize)
                val acc = (winStart until winEnd).sumByDouble {
                    min(max(-1.0, fixed.ys[it]), 1.0)
                }
                fixed.ys[i] = acc / (winEnd - winStart)
            }
        }
        return fixed
    }

    fun antiTrend(input: Line, windowSize: Int = 3): Line {
        val trendDetect = trendDetect(input, windowSize)
        return Line(DoubleArray(input.size) { input.ys[it] - trendDetect[it] })
    }

    fun trendDetect(input: Line, windowSize: Int = 3) =
            DoubleArray(input.size) {
                val endIdx = min(it + windowSize, input.size)
                val startIdx = max(0, it - windowSize)
                LineStatistics.avg(input, startIdx, endIdx)
            }
}