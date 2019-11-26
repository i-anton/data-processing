package core.input

import core.Line
import java.util.*
import kotlin.math.exp
import kotlin.math.sin

object LineGenerator {
    fun linear(dots: Int, angle: Double, offset: Double)
            = Line(dots) { angle * it + offset }

    fun exponent(dots: Int, koef: Double, degree: Double)
            = Line(dots) { koef * exp(degree * it) }

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
        val rnd = Random(seed.toLong())
        return Line(dots) { rnd.nextDouble() * (maxVal - minVal) + minVal }
    }

    fun myRandom(dots: Int, minVal: Double = 0.0,
                 maxVal: Double = 1.0, seed: Int = anySeed()): Line {
        val rnd = MyRandom(seed)
        return Line(dots) { rnd.nextDouble() * (maxVal - minVal) + minVal }
    }

    fun harmonic(dots: Int, amplitude: Double, frequency: Double, deltaF: Double = 0.001): Line {
        val mul =  2.0 * Math.PI * frequency * deltaF
        return Line(dots) { sin( mul * it ) * amplitude }
    }
}
