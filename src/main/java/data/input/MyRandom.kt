package data.input

import data.input.LineGenerator.anySeed

class MyRandom(seed: Int = anySeed()) {
    private var prevValue = seed * 25923L

    fun nextDouble(): Double {
        var value = prevValue * 6364136223846723L
        val shift = ((value ushr 16) xor prevValue) ushr 27
        val rot = prevValue ushr 59
        value = (shift ushr rot.toInt()) or (shift shl (-rot.toInt() and 31))
        prevValue = value
        return value.toDouble() / Int.MAX_VALUE
    }
}
