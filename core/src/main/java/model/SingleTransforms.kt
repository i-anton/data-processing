package core.model

import core.Line
import core.input.anySeed
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object SingleTransforms {
    fun Line.normalize(scale: Double): Line {
        var minVal = ys[0]
        var maxVal = ys[0]
        for (i in 1 until size) {
            val curr = ys[i]
            if (curr > maxVal) maxVal = curr
            if (curr < minVal) minVal = curr
        }
        return Line(size) {
            scale * ((ys[it] - minVal) / (maxVal - minVal) - 0.5)
        }
    }

    fun Line.spikes(spikeNum: Int, scale: Double, seed: Int = anySeed()): Line {
        val rnd = Random(seed)
        val result = Line(this)
        repeat(spikeNum - 1) {
            val spikeIdx = rnd.nextInt(size)
            for (j in max(spikeIdx - 1, 0) until min(spikeIdx + 1, size)) {
                val y = result.ys[j]
                result.ys[j] = y + y * scale
            }
        }
        return result
    }

    fun Line.shift(shift: Double, scale: Double,
              start: Double = xs.first(), end: Double = xs.last()): Line {
        val result = Line(this)
        var startIdx = -1
        for (i in result.xs.indices) {
            if (result.xs[i] >= start) {
                startIdx = i
                break
            }
        }
        if (startIdx == -1) return result
        for (i in startIdx until size) {
            if (result.xs[i] > end) break
            result.ys[i] = result.ys[i] * scale + shift
        }
        return result
    }

    fun Line.hammingWindowed(alpha: Double = 0.46) =
            Line(xs) { ys[it] * (alpha - (1.0 - alpha) * cos(2.0 * Math.PI * it / size)) }

    fun Line.zero(from: Int, to: Int = size) = Line(xs) {
        when {
            (it >= from) and (it <= to) -> 0.0
            else -> ys[it]
        }
    }
}
