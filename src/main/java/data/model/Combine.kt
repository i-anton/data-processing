package data.model

import data.Line
import java.util.function.BiFunction

object Combine {
    fun additive(one: Line, other: Line): Line {
        return lineCombine(one, other, BiFunction { x, y -> x + y; })
    }

    fun multiplicative(one: Line, other: Line): Line {
        return lineCombine(one, other, BiFunction { x, y -> x * y; })
    }

    fun lineCombine(one: Line, other: Line, f: BiFunction<Double, Double, Double>): Line {
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
