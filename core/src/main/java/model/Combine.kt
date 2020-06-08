package core.model

import core.Line
import core.input.LineGenerator

object Combine {

    fun additive(one: Line, other: Line) = lineCombine(one, other) { x, y -> x + y }

    fun additive(one: Line, other: Double) = lineApply(one) { y -> other + y }

    fun multiplicative(one: Line, other: Line) = lineCombine(one, other) { x, y -> x * y }

    fun multiplicative(one: Line, other: Double) = lineApply(one) { y -> y * other }

    private fun lineApply(one: Line, f: (Double) -> Double): Line {
        val resultLine = Line(one)
        for (i in 0 until one.size)
            resultLine.ys[i] = f(resultLine.ys[i])
        return resultLine
    }

    private fun average(lines: List<DoubleArray>): Line {
        require(lines.all { line -> line.size == lines.first().size })
        return Line(DoubleArray(lines.first().size) {
            lines.sumByDouble { line -> line[it] } / lines.size
        })
    }

    fun multiNoiseAvg(amplitude: Double, dots: Int, noisyIterations: Int) = average(
            (0..noisyIterations).map {
                LineGenerator.random(dots, -amplitude, amplitude)
            }
    )

    private fun lineCombine(one: Line, other: Line, f: (Double, Double) -> Double): Line {
        val isOne = one.size > other.size
        val resultLine: Line = when {
            isOne -> Line(one)
            else -> Line(other)
        }
        val copyFrom: Line = when {
            isOne -> other
            else -> one
        }
        for (i in 0 until copyFrom.size)
            resultLine.ys[i] = f(resultLine.ys[i], copyFrom.ys[i])
        return resultLine
    }
}

infix fun Line.sum(other: Line) = Combine.additive(this, other)
infix fun Line.mul(other: Line) = Combine.multiplicative(this, other)

infix fun Line.mul(number: Double) = Combine.multiplicative(this, number)
infix fun Line.sum(number: Double) = Combine.additive(this, number)


fun div(first: Pair<DoubleArray, DoubleArray>, second: Pair<DoubleArray, DoubleArray>)
        : Pair<DoubleArray, DoubleArray> {
    val size = first.first.size
    val newLineRe = DoubleArray(size)
    val newLineIm = DoubleArray(size)
    for (i in newLineRe.indices) {
        val a = first.first[i]
        val b = first.second[i]
        val c = second.first[i]
        val d = second.second[i]
        newLineRe[i] = (a * c + b * d) /
                (c * c + d * d)

        newLineIm[i] = (b * c - a * d) /
                (c * c + d * d)
    }
    return Pair(newLineRe, newLineIm)
}

fun mul(first: Pair<DoubleArray, DoubleArray>, second: Pair<DoubleArray, DoubleArray>)
        : Pair<DoubleArray, DoubleArray> {
    val size = first.first.size
    val newLineRe = DoubleArray(size)
    val newLineIm = DoubleArray(size)
    for (i in newLineRe.indices) {
        val a = first.first[i]
        val b = first.second[i]
        val c = second.first[i]
        val d = second.second[i]
        newLineRe[i] = a * c - b * d
        newLineIm[i] = a * d + b * c
    }
    return Pair(newLineRe, newLineIm)
}

fun DoubleArray.div(number: Double): DoubleArray {
    val new = this.copyOf()
    new.forEachIndexed { index, value ->
        new[index] = value / number
    }
    return new
}

fun DoubleArray.div(array: DoubleArray): DoubleArray {
    val new = this.copyOf()
    new.forEachIndexed { index, value ->
        new[index] = value / array[index]
    }
    return new
}

fun DoubleArray.mul(number: Double): DoubleArray {
    val new = this.copyOf()
    new.forEachIndexed { index, value ->
        new[index] = value * number
    }
    return new
}

fun DoubleArray.mul(other: DoubleArray): DoubleArray {
    val new = this.copyOf()
    new.forEachIndexed { index, value ->
        new[index] = value * other[index]
    }
    return new
}

fun DoubleArray.sum(number: Double): DoubleArray {
    val new = this.copyOf()
    new.forEachIndexed { index, value ->
        new[index] = value + number
    }
    return new
}

fun DoubleArray.sum(other: DoubleArray): DoubleArray {
    val new = this.copyOf()
    new.forEachIndexed { index, value ->
        new[index] = value + other[index]
    }
    return new
}

fun copyImg(data: Array<DoubleArray>): Array<DoubleArray> = Array(data.size) { data[it].copyOf() }

fun sum(one: Array<DoubleArray>, other: Array<DoubleArray>):
        Array<DoubleArray> = Array(one.size) {
    val oneRow = one[it]
    val otherRow = other[it]
    oneRow.sum(otherRow)
}
fun mul(one: Array<DoubleArray>, multiplier: Double):
        Array<DoubleArray> = Array(one.size) {
    val oneRow = one[it]
    oneRow.mul(multiplier)
}


