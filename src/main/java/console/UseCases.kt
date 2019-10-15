package console

import data.Line
import data.analysis.CompositeStatistics
import data.analysis.LineStatistics
import data.input.LineGenerator
import data.model.Combine
import data.model.Combine.multiNoiseAvg
import de.gsi.dataset.spi.DoubleDataSet
import gui.ShowCase
import javafx.application.Platform

fun combineDemo() {
    val linear = LineGenerator.linear(1000, -2.0, 0.0)
    val random = LineGenerator.random(1000, -1000.0, 1000.0)
    val dataList = listOf(
            listOf(DoubleDataSet("Random").addLine(random)),
            listOf(DoubleDataSet("Add").addLine(Combine.additive(random, linear))),
            listOf(DoubleDataSet("Multiply").addLine(Combine.multiplicative(random, linear))),
            listOf(DoubleDataSet("Linear").addLine(linear))
    )
    Platform.startup {
        ShowCase().multi(dataList).show()
    }
}

fun combineAvgDemo() {
    val combinesRaw = listOf(
            multiNoiseAvg(100.0, 1000, 2),
            multiNoiseAvg(100.0, 1000, 5),
            multiNoiseAvg(100.0, 1000, 10),
            multiNoiseAvg(100.0, 1000, 20)
    )
    val combines = combinesRaw.map {
        listOf(DoubleDataSet("").addLine(it))
    }
    Platform.startup {
        ShowCase().multi(combines).show()
    }
    combinesRaw.forEach {
        println(LineStatistics.stdDev(it))
    }
}

fun stationarityDemo() {
    println(CompositeStatistics.isStationary(
            LineGenerator.random(100, -100.0, 100.0, 0),
            5, 0.05))
}

fun autoCorrelationDemo() {
    val initial = Line(LineGenerator.linear(100, 1.0, 0.0))
    val initialRand = LineGenerator.random(100)
    val stats = CompositeStatistics.autoCorrelation(initial)
    val statsRand = CompositeStatistics.autoCorrelation(initialRand)
    Platform.startup {
        ShowCase().multi(listOf(
                listOf(DoubleDataSet("f(line)").addLine(stats)),
                listOf(DoubleDataSet("line").addLine(initial)),
                listOf(DoubleDataSet("f(random)").addLine(statsRand)),
                listOf(DoubleDataSet("random").addLine(initialRand))
        )
        ).show()
    }
}

fun harmonicDemo() {
    /* 500 hz is limit*/
    val frequencies = listOf(11.0, 110.0, 250.0, 510.0)
    val dataList = frequencies.map {
        listOf(DoubleDataSet("").addLine(
                LineGenerator.harmonic(1000, 100.0, it))
        )
    }
    Platform.startup {
        ShowCase().multi(dataList).show()
    }
}

fun dftDemo() {
    val frequencies = listOf(11.0, 110.0, 250.0, 510.0)
    val dataList = frequencies.map {
        listOf(DoubleDataSet("").addLine(
                CompositeStatistics.dft(LineGenerator.harmonic(1000, 100.0, it)))
        )
    }
    Platform.startup {
        ShowCase().multi(dataList).show()
    }
}