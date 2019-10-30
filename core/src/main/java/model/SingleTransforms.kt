package core.model

import core.Line
import core.input.anySeed
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object SingleTransforms {
    fun normalize(line: Line, scale: Double): Line {
        var minVal = line.ys[0]
        var maxVal = line.ys[0]
        for (i in 1 until line.size) {
            val curr = line.ys[i]
            if (curr > maxVal) maxVal = curr
            if (curr < minVal) minVal = curr
        }
        return Line(line.size) {
            scale * ((line.ys[it] - minVal) / (maxVal - minVal) - 0.5)
        }
    }

    fun spikes(line: Line, spikeNum: Int, scale: Double, seed: Int = anySeed()): Line {
        val rnd = Random(seed)
        val size = line.size
        val result = Line(line)
        repeat(spikeNum - 1) {
            val spikeIdx = rnd.nextInt(size)
            for (j in max(spikeIdx - 1, 0) until min(spikeIdx + 1, size)) {
                val y = result.ys[j]
                result.ys[j] = y + y * scale
            }
        }
        return result
    }

    fun shift(line: Line, shift: Double, scale: Double,
              start: Double = line.xs.first(), end: Double = line.xs.last()): Line {
        val result = Line(line)
        var startIdx = -1
        for (i in result.xs.indices) {
            if (result.xs[i] >= start) {
                startIdx = i
                break
            }
        }
        if (startIdx == -1) return line
        for (i in startIdx until line.size) {
            if (result.xs[i] > end) break
            result.ys[i] = result.ys[i] * scale + shift
        }
        return result
    }
}
