package data.model

import data.Line

import java.util.Random

object LineData {
    fun linear(dots: Int, angle: Double, offset: Double): Line {
        val line = Line(dots)
        for (i in 0 until dots) {
            line.setX(i, i.toDouble())
            line.setY(i, angle * i + offset)
        }
        return line
    }

    fun exponent(dots: Int, koef: Double, degree: Double): Line {
        val line = Line(dots)
        for (i in 0 until dots) {
            line.setX(i, i.toDouble())
            line.setY(i, koef * Math.exp(degree * i))
        }
        return line
    }

    fun piecewise(dots: Int, stepSize: Double): Line {
        val line = Line(dots)
        for (i in 0 until dots) {
            val si = stepSize * i
            line.setX(i, si)
            val value: Double
            if (si < 20)
                value = (si - 10) * (si - 10)
            else if (si < 40)
                value = -5 * ((si - 30) * (si - 30) - 120)
            else
                value = 2.5 * si
            line.setY(i, value)
        }

        return line
    }

    fun random(dots: Int, seed: Int): Line {
        val line = Line(dots)
        val rnd = Random(seed.toLong())
        for (i in 0 until dots) {
            line.setX(i, i.toDouble())
            line.setY(i, rnd.nextDouble())
        }
        return line
    }

    fun myRandom(dots: Int, seed: Int): Line {
        val line = Line(dots)
        val rnd = MyRandom(seed)
        for (i in 0 until dots) {
            line.setX(i, i.toDouble())
            line.setY(i, rnd.nextDouble())
        }
        return line
    }
}
