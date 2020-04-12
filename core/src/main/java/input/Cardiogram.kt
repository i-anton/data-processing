package core.input

import core.Line
import core.analysis.Convolution.convolution
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

object Cardiogram {
    fun baseFunction(dots: Int, frequency: Double,
                     dt: Double, relaxation: Double): DoubleArray {
        val multiplier = 2.0 * PI * frequency * dt
        return DoubleArray(dots) {
            sin(multiplier * it) * exp(-relaxation * dt * it)
        }
    }

    fun deltaFunction(dots: Int, interval: Int) = Line(dots) {
        when {
            it % interval == 0 -> 1.0
            else -> 0.0
        }
    }

    fun cardiogram(dots: Int, frequency: Double,
                   dt: Double, interval: Int, relaxation: Double): Line {
        val line = baseFunction(dots, frequency, dt, relaxation)
        val delta = deltaFunction(dots, interval)
        return convolution(delta, line)
    }
}