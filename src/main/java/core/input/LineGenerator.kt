package core.input

import core.Line
import java.util.*
import kotlin.math.exp
import kotlin.math.sin

object LineGenerator {
    fun linear(dots: Int, angle: Double, offset: Double): Line {
        val line = Line(dots)
        for (i in 0 until dots) {
            line.xs[i] = i.toDouble()
            line.ys[i] = angle * i + offset
        }
        return line
    }

    fun exponent(dots: Int, koef: Double, degree: Double): Line {
        val line = Line(dots)
        for (i in 0 until dots) {
            line.xs[i] = i.toDouble()
            line.ys[i] = koef * exp(degree * i)
        }
        return line
    }

    fun piecewise(dots: Int, stepSize: Double): Line {
        val line = Line(dots)
        for (i in 0 until dots) {
            val si = stepSize * i
            line.xs[i] = si
            val value: Double
            value = when {
                si < 20 -> (si - 10) * (si - 10)
                si < 40 -> -5 * ((si - 30) * (si - 30) - 120)
                else -> 2.5 * si
            }
            line.ys[i] = value
        }
        return line
    }

    fun random(dots: Int, minVal: Double = 0.0,
               maxVal: Double = 1.0, seed: Int = anySeed()): Line {
        val line = Line(dots)
        val rnd = Random(seed.toLong())
        for (i in 0 until dots) {
            line.xs[i] = i.toDouble()
            line.ys[i] = rnd.nextDouble() * (maxVal - minVal) + minVal
        }
        return line
    }

    fun myRandom(dots: Int, seed: Int = anySeed()): Line {
        val line = Line(dots)
        val rnd = MyRandom(seed)
        for (i in 0 until dots) {
            line.xs[i] = i.toDouble()
            line.ys[i] = rnd.nextDouble()
        }
        return line
    }

    fun harmonic(dots: Int, amplitude: Double, frequency: Double): Line {
        val line = Line(dots)
        for (i in 0 until dots) {
            line.xs[i] = i.toDouble()
            line.ys[i] = sin(2.0 * Math.PI * frequency * i * 0.001) * amplitude
        }
        return line
    }
}
