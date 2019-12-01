package core.input

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object PassFilters {

    val SMOOTH_WINDOW_P310 = arrayOf(0.35577019, 0.24369830, 0.07211497, 0.00630165)
    fun halfLowPassFilter(size: Int, dt: Double, fCut: Double): DoubleArray {
        val result = DoubleArray(size + 1)
        var param = 2.0 * dt * fCut
        result[0] = param
        param *= PI
        for (i in 1 until size) result[i] = sin(param * i) / (PI * i)
        result[size] /= 2.0

        var sumg = result[0]
        for (i in 1..size) {
            var sum = SMOOTH_WINDOW_P310[0]
            val arg = (PI * i) / size
            for (k in 1..3) sum += 2.0 * SMOOTH_WINDOW_P310[k] * cos(arg * k)
            result[i] *= sum
            sumg += result[i] * 2.0
        }
        for (i in 0..size) result[i] = result[i] / sumg
        return result
    }

    fun lowPassFilter(size: Int, dt: Double, fCut: Double): DoubleArray {
        val result = DoubleArray(2 * size + 1)
        val halfLowPass = halfLowPassFilter(size, dt, fCut)
        halfLowPass.copyInto(result, size)
        for (i in 1..size) result[i] = result[result.size - i]
        return result
    }

    fun highPassFilter(size: Int, dt: Double, fCut: Double): DoubleArray {
        val result = lowPassFilter(size, dt, fCut)
        for (i in result.indices) {
            result[i] = when (i) {
                size -> 1 - result[i]
                else -> -result[i]
            }
        }
        return result
    }

    fun bandPassFilter(size: Int, dt: Double, fCutLower: Double, fCutUpper: Double): DoubleArray {
        assert(fCutLower < fCutUpper)
        val lpfLower = lowPassFilter(size, dt, fCutLower)
        val lpfUpper = lowPassFilter(size, dt, fCutUpper)
        for (i in lpfLower.indices) {
            lpfLower[i] = when (i) {
                size -> 1 + lpfLower[i] - lpfUpper[i]
                else -> lpfLower[i] - lpfUpper[i]
            }
        }
        return lpfLower
    }
}