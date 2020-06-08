package core.analysis

import core.Line
import kotlin.math.pow
import kotlin.math.sqrt

object Correlation {
    fun autoCorrelation(line: DoubleArray, start: Int = 0, end: Int = line.size): DoubleArray {
        val avg = line.mean(start, end)
        val divider = (start until end)
                .sumByDouble { (line[it] - avg).pow(2.0) }

        val array = DoubleArray(end - start + 1)
        for (funShift in start until end) {
            array[funShift - start] =
                    (0 until end - funShift).fold(0.0) { sum, k ->
                        sum + (line[k] - avg) * (line[k + funShift] - avg)
                    } / divider
        }
        return array
    }

    fun autoCorrelation(line: Line, start: Int = 0, end: Int = line.size) =
            Line(autoCorrelation(line.ys,start, end))

    fun crossCorrelation(first: DoubleArray, second: DoubleArray): DoubleArray {
        require(first.size == second.size)
        val avgFirst = first.mean()
        val avgSecond = second.mean()
        val divider =
                sqrt(first.sumByDouble { (it - avgFirst).pow(2.0) }) *
                        sqrt(second.sumByDouble { (it - avgSecond).pow(2.0) })

        return DoubleArray(first.size) {
            (0 until first.size - it).fold(0.0) { sum, k ->
                sum + (first[k] - avgFirst) * (second[k + it] - avgSecond)
            } / divider
        }
    }

    fun crossCorrelation(first: Line, second: Line)
            = Line(crossCorrelation(first.ys, second.ys))
}