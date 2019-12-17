package core.input

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object PassFilters {

    private val SMOOTH_WINDOW_P310 = arrayOf(0.35577019, 0.24369830, 0.07211497, 0.00630165)
    private fun halfLowPassFilter(size: Int, dt: Double, fCut: Double): DoubleArray {
        val result = DoubleArray(size + 1)
        var param = 2.0 * dt * fCut
        result[0] = param
        param *= PI
        for (i in 1 until result.size) result[i] = sin(param * i) / (PI * i)
        result[size] /= 2.0

        result[0] *= SMOOTH_WINDOW_P310.sumByDouble { it } * 2.0
        var sumg = result[0]
        for (i in 1 until result.size) {
            val arg = (PI * i) / size
            val sum = SMOOTH_WINDOW_P310[0] +
                    (1 until SMOOTH_WINDOW_P310.size).sumByDouble { SMOOTH_WINDOW_P310[it] * cos(arg * it) }
            result[i] *= sum * 2.0
            sumg += result[i] * 2.0
        }
        for (i in result.indices) result[i] /= sumg
        return result
    }

    fun lowPassFilter(size: Int, dt: Double, fCut: Double): DoubleArray {
        val result = DoubleArray(2 * size + 1)
        halfLowPassFilter(size, dt, fCut).apply { copyInto(result, size) }
        for (i in 0..size) result[i] = result[result.size - i - 1]
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

    fun bandSelectFilter(size: Int, dt: Double, fCutLower: Double, fCutUpper: Double): DoubleArray {
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

    fun bandPassFilter(size: Int, dt: Double, fCutLower: Double, fCutUpper: Double): DoubleArray {
        assert(fCutLower < fCutUpper)
        val lpfLower = lowPassFilter(size, dt, fCutLower)
        val lpfUpper = lowPassFilter(size, dt, fCutUpper)
        for (i in lpfLower.indices) lpfLower[i] = lpfUpper[i] - lpfLower[i]
        return lpfLower
    }
}