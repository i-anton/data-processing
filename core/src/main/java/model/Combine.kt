package core.model

import core.Line
import core.input.LineGenerator
import java.util.function.BiFunction
import java.util.function.DoubleFunction

object Combine {

    fun additive(one: Line, other: Line) = lineCombine(one, other, BiFunction { x, y -> x + y })

    fun additive(one: Line, other: Double) = lineApply(one, DoubleFunction { y -> other + y })

    fun multiplicative(one: Line, other: Line) = lineCombine(one, other, BiFunction { x, y -> x * y })

    fun multiplicative(one: Line, other: Double) = lineApply(one, DoubleFunction { y -> y * other })

    private fun lineApply(one: Line, f: DoubleFunction<Double>): Line {
        val resultLine = Line(one)
        for (i in 0 until one.size)
            resultLine.ys[i] = f.apply(resultLine.ys[i])
        return resultLine
    }

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
        for (i in 0 until copyFrom.size)
            resultLine.ys[i] = f.apply(resultLine.ys[i], copyFrom.ys[i])
        return resultLine
    }

    fun convolution(one: Line, other: Line) = Line(one.xs, other.ys)

    fun convolution(one: Line, other: DoubleArray) = Line(one.xs, convolution(one.ys, other))

    fun convolution(one: DoubleArray, other: DoubleArray): DoubleArray {
        val n = one.size
        val m = other.size
        return DoubleArray(n) {
            when (it) {
                in 0 until n + m - 1 -> {
                    var result = 0.0
                    for (j in 0 until m) if (it - j in 1 until n) {
                        result += one[it - j] * other[j]
                    }
                    result
                }
                else -> 0.0
            }
        }
    }
}

infix fun Line.add(other: Line) = Combine.additive(this, other)
infix fun Line.mul(other: Line) = Combine.multiplicative(this, other)

infix fun Line.mul(number: Double) = Combine.multiplicative(this, number)
infix fun Line.add(number: Double) = Combine.additive(this, number)
