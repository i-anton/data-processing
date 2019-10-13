package console

import data.Line
import data.analysis.LineStatistics.avg
import data.analysis.LineStatistics.stdDev
import data.input.LineGenerator
import data.model.Combine
import de.gsi.dataset.spi.DoubleDataSet
import gui.ShowCase
import javafx.application.Platform

fun combineIterativeNoise(first: Line, amplitude: Double,
                          dots: Int, noisyIterations: Int): Line {
    var accLine = first
    for (i in 0 until noisyIterations) {
        accLine = Combine.additive(accLine,
                LineGenerator.random(dots, -amplitude, amplitude))
    }
    return accLine
}

fun main() {
    // signal to noise ration
    println("Test")
    val line = { l : Int ->
        LineGenerator.linear(l, 3.7, 20.0)
    }
    val combines = listOf(
            combineIterativeNoise(line(1000), 1.0, 1000, 1),
            combineIterativeNoise(line(100), 1.0, 100, 1),
            combineIterativeNoise(line(10), 1.0, 10, 1)
    )

    Platform.startup {
        ShowCase().single(combines.map { l ->
            DoubleDataSet("").addLine(l)
        }).show()
    }
    for (item in combines) {
        println(avg(item)/stdDev(item))
    }
    val iterated = combineIterativeNoise(LineGenerator.random(1000, -100.0, 100.0),
            100.0, 1000, 10)
    println(avg(iterated)/stdDev(iterated))
    // noise stddev(s(100), N=1000, n=1) = 60
    // noise stddev(s(100), N=100, n=1) = 5.8
    // noise stddev = sqrt(N)
    // ensembleAvg (for N ensembles)

    //move
    //stationarity (has trends?)
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