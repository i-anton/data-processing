package data.model

import data.Line
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object SingleTransforms {
    fun normalize(line: Line, scale: Double): Line {
        var minVal = line.ys[0]
        var maxVal = line.ys[0]
        val size = line.size
        for (i in 1 until size) {
            val curr = line.ys[i]
            if (curr > maxVal) maxVal = curr
            if (curr < minVal) minVal = curr
        }
        for (i in 0 until size)
            line.ys[i] = scale * ((line.ys[i] - minVal) / (maxVal - minVal) - 0.5)
        return line
    }

    fun spikes(line: Line, seed: Int, spikeNum: Int, scale: Double): Line {
        val rnd = Random(seed.toLong())
        val size = line.size
        for (i in spikeNum downTo 1) {
            val spikeIdx = rnd.nextInt(size)
            for (j in max(spikeIdx - 1, 0) until min(spikeIdx + 1, size)) {
                val y = line.ys[j]
                line.ys[spikeIdx - 1] = y + y * scale
            }
        }
        return line
    }

    fun shift(line: Line, start: Double, end: Double, shift: Double, scale: Double): Line {
        var startIdx = -1
        for (i in 0 until line.size) {
            if (line.xs[i] >= start) {
                startIdx = i
                break
            }
        }
        if (startIdx == -1) return line
        for (i in startIdx until line.size) {
            if (line.xs[i] > end) break
            line.ys[i] = line.ys[i] * scale + shift
        }
        return line
    }
}
