package core.input

import kotlin.math.E
import kotlin.math.log
import kotlin.math.sqrt
import kotlin.random.Random

fun anySeed() = System.nanoTime().toInt()

class GaussianRandom(private val mean: Double = 0.0, private val stdDev: Double = 1.0) {
    fun nextDouble(): Double {
        var v1: Double
        var v2: Double
        var s: Double
        do {
            v1 = Random.nextDouble(-1.0, 1.0)
            v2 = Random.nextDouble(-1.0, 1.0)
            s = v1 * v1 + v2 * v2
        } while (s >= 1 || s == 0.0)
        return stdDev * v1 * sqrt(-2.0 * log(s, E) / s) + mean
    }
}
