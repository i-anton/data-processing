package core.model

import core.Line
import core.input.LineGenerator
import java.util.function.BiFunction

object Combine {
    fun additive(one: Line, other: Line) = lineCombine(one, other, BiFunction { x, y -> x + y })

    fun multiplicative(one: Line, other: Line) = lineCombine(one, other, BiFunction { x, y -> x * y })

    private fun average(lines: List<Line>): Line {
        require(lines.all { line -> line.size == lines.first().size })
        return Line(DoubleArray(lines.first().size) {
            lines.sumByDouble { line -> line.ys[it] } / lines.size
        })
    }

    fun multiNoiseAvg(amplitude: Double, dots: Int, noisyIterations: Int) = average(
            (0..noisyIterations).map {
                LineGenerator.random(dots, -amplitude, amplitude)
            }
    )

    private fun lineCombine(one: Line, other: Line, f: BiFunction<Double, Double, Double>): Line {
        val isOne = one.size > other.size
        val resultLine: Line = when {
            isOne -> Line(one)
            else -> Line(other)
        }
        val copyFrom: Line = when {
            isOne -> other
            else -> one
        }
        for (i in 0 until copyFrom.size) {
            resultLine.ys[i] = f.apply(resultLine.ys[i], copyFrom.ys[i])
        }
        return resultLine
    }
}
