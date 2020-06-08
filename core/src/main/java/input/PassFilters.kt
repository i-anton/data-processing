package core.input

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object PassFilters {

    private val SMOOTH_WINDOW_P310 = arrayOf(0.35577019, 0.24369830, 0.07211497, 0.00630165)
    private fun halfLowPass(size: Int, dt: Double, fCut: Double): DoubleArray {
        val result = DoubleArray(size + 1)
        var param = 2.0 * dt * fCut
        result[0] = param * SMOOTH_WINDOW_P310.sumByDouble { it } * 2.0
        param *= PI
        for (i in 1 until result.size) result[i] = sin(param * i) / (PI * i)
        result[size] /= 2.0

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

    fun lowPass(size: Int, dt: Double, fCut: Double): DoubleArray {
        val result = DoubleArray(2 * size + 1)
        halfLowPass(size, dt, fCut).apply { copyInto(result, size) }
        for (i in 0..size) result[i] = result[result.size - i - 1]
        return result
    }

    fun highPass(size: Int, dt: Double, fCut: Double): DoubleArray {
        val result = lowPass(size, dt, fCut)
        for (i in result.indices) result[i] = -result[i]
        result[size] = result[size] + 1
        return result
    }

    fun bandSelect(size: Int, dt: Double, fCutLower: Double, fCutUpper: Double): DoubleArray {
        require(fCutLower < fCutUpper)
        val lpfLower = lowPass(size, dt, fCutLower)
        val lpfUpper = lowPass(size, dt, fCutUpper)
        for (i in lpfLower.indices) lpfLower[i] = lpfLower[i] - lpfUpper[i]
        lpfLower[size] = lpfLower[size] + 1
        return lpfLower
    }

    fun bandPass(size: Int, dt: Double, fCutLower: Double, fCutUpper: Double): DoubleArray {
        require(fCutLower < fCutUpper)
        val lpfLower = lowPass(size, dt, fCutLower)
        val lpfUpper = lowPass(size, dt, fCutUpper)
        for (i in lpfLower.indices) lpfLower[i] = lpfUpper[i] - lpfLower[i]
        return lpfLower
    }
}