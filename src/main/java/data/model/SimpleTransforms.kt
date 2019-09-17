package data.model

import data.Line

import java.util.Random

object SimpleTransforms {
    fun normalize(line: Line, scale: Double) {
        var minVal = line.getY(0)
        var maxVal = line.getY(0)
        val size = line.size
        for (i in 1 until size) {
            val curr = line.getY(i)
            if (curr > maxVal) maxVal = curr
            if (curr < minVal) minVal = curr
        }
        for (i in 0 until size) {
            var curr = line.getY(i)
            curr = scale * ((curr - minVal) / (maxVal - minVal) - 0.5)
            line.setY(i, curr)
        }
    }

    fun spikes(line: Line, seed: Int, spikeNum: Int, scale: Double) {
        val rnd = Random(seed.toLong())
        val size = line.size
        val halfScale = scale * 0.5
        for (i in spikeNum downTo 1) {
            val spikeIdx = rnd.nextInt(size)
            val value = line.getY(spikeIdx)
            if (spikeIdx != 0) {
                val y = line.getY(spikeIdx - 1)
                line.setY(spikeIdx - 1, y + y * halfScale)
            }
            if (spikeIdx != size) {
                val y = line.getY(spikeIdx + 1)
                line.setY(spikeIdx + 1, y + y * halfScale)
            }
            line.setY(spikeIdx, value + value * scale)
        }
    }

    fun shift(line: Line, start: Double, end: Double, shift: Double, scale: Double) {
        val size = line.size
        var i = 0
        var curr = line.getX(i)
        while (i < size && curr < start) {
            i++
            curr = line.getX(i)
        }
        while (i < size && curr < end) {
            val `val` = line.getY(i)
            curr = line.getX(i)
            line.setY(i, (`val` + shift) * scale)
            i++
        }
    }
}
