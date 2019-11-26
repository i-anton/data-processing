package core.input

fun anySeed() = System.nanoTime().toInt()

class MyRandom(seed: Int = anySeed()) {
    private var prevValue = seed

    fun nextDouble(): Double {
        prevValue = (prevValue * 32719 + 3) % 32749
        return prevValue.toDouble() / 32749.0
    }
}
