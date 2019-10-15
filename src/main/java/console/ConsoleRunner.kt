package console

import data.Line
import data.input.LineGenerator
import de.gsi.dataset.spi.DoubleDataSet

fun main() {
    dftDemo()
}

fun DoubleDataSet.addLine(line: Line): DoubleDataSet {
    this.setAutoNotifaction(false)
    line.xs.indices.forEach { i -> this.add(line.xs[i], line.ys[i]) }
    this.setAutoNotifaction(true)
    return this
}

class MyRnd(seed: Int = LineGenerator.anySeed()) {
    private var prevValue = seed * 25923

    fun nextDouble(): Double {
        var value = prevValue * 63846723
        val shift = ((value ushr 12) xor prevValue) ushr 24
        val rot = prevValue ushr 52
        value = (shift ushr rot) or (shift shl (-rot and 31))
        prevValue = value
        return value.toDouble()
    }
}