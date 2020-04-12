package core.analysis

import core.Line
import kotlin.math.pow
import kotlin.math.sqrt

object Correlation {
    fun autoCorrelation(line: Line, start: Int = 0, end: Int = line.size): Line {
        val avg = line.mean(start, end)
        val divider = (start until end)
                .sumByDouble { (line.ys[it] - avg).pow(2.0) }

        val array = DoubleArray(end - start + 1)
        for (funShift in start until end) {
            array[funShift - start] =
                    (0 until end - funShift).fold(0.0) { sum, k ->
                        sum + (line.ys[k] - avg) * (line.ys[k + funShift] - avg)
                    } / divider
        }
        return Line(array)
    }

    fun crossCorrelation(first: Line, second: Line): Line {
        require(first.size == second.size)
        val avgFirst = first.mean()
        val avgSecond = second.mean()
        val divider =
                sqrt(first.ys.sumByDouble { (it - avgFirst).pow(2.0) }) *
                        sqrt(second.ys.sumByDouble { (it - avgSecond).pow(2.0) })

        return Line(DoubleArray(first.size) {
            (0 until first.size - it).fold(0.0) { sum, k ->
                sum + (first.ys[k] - avgFirst) * (second.ys[k + it] - avgSecond)
            } / divider
        })
    }
}