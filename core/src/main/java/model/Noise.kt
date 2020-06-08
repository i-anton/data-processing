package core.model

import core.input.GaussianRandom
import kotlin.random.Random

object Noise {
    fun impulse(data: DoubleArray, probability: Double) {
        data.indices.forEach {
            val chance = Random.nextDouble(0.0, 1.0)
            data[it] = when {
                chance <= probability -> when {
                    Random.nextBoolean() -> 255.0
                    else -> 0.0
                }
                else -> data[it]
            }
        }
    }

    fun gaussian(data: DoubleArray, probability: Double) {
        val rnd = GaussianRandom(0.0, 255.0 * probability)
        data.indices.forEach {
            val chance = rnd.nextDouble()
            data[it] += when {
                chance <= probability -> rnd.nextDouble()
                else -> 0.0
            }
        }
    }
}