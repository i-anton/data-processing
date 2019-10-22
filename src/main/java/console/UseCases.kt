package console

import data.Line
import data.analysis.CompositeStatistics
import data.analysis.LineStatistics
import data.input.LineGenerator
import data.model.Combine
import data.model.Combine.multiNoiseAvg
import data.model.Filter.antiShift
import data.model.Filter.antiSpike
import data.model.Filter.antiTrend
import data.model.Filter.trendDetect
import data.model.SingleTransforms
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

fun removeConstantShiftDemo() {
    val initialRand = LineGenerator.random(100, -1.0, 1.0)
    val shiftedRand = SingleTransforms.shift(Line(initialRand), -100.0,
            initialRand.size.toDouble() + 1,
            100.0, 1.0)

    val fixed = antiShift(shiftedRand)

    Platform.startup {
        ShowCase().multi(listOf(
                listOf(DoubleDataSet("line").addLine(initialRand)),
                listOf(DoubleDataSet("shifted").addLine(shiftedRand)),
                listOf(DoubleDataSet("f(random)").addLine(fixed)),
                listOf(DoubleDataSet("random").addLine(fixed))
        )).show()
    }
}

fun removeSpikesDemo() {
    val initialRand = LineGenerator.random(100, -1.0, 1.0)
    val spikedRand = SingleTransforms.spikes(Line(initialRand), LineGenerator.anySeed(),
            10,
            5.0)

    val fixed = antiSpike(spikedRand,1.0)

    Platform.startup {
        ShowCase().multi(listOf(
                listOf(DoubleDataSet("line").addLine(initialRand)),
                listOf(DoubleDataSet("shifted").addLine(spikedRand)),
                listOf(DoubleDataSet("f(random)").addLine(fixed)),
                listOf(DoubleDataSet("random").addLine(fixed))
        )).show()
    }
}

fun trendDetectionDemo() {
    val dots = 100
    val initialRand = LineGenerator.random(dots, -1.0, 1.0)
    val spikedRand = SingleTransforms.spikes(Line(initialRand), LineGenerator.anySeed(),
            10,
            5.0)
    val trendLine = LineGenerator.linear(dots, 0.75, 50.0)
    val trendy = Combine.additive(spikedRand, trendLine)

    val windowSize = 3
    val antiTrend = antiTrend(trendy, windowSize)
    val detectedTrend = Line(trendDetect(trendy, windowSize))
    Platform.startup {
        ShowCase().multi(listOf(
                listOf(DoubleDataSet("line").addLine(spikedRand)),
                listOf(DoubleDataSet("trendy").addLine(trendy)),
                listOf(DoubleDataSet("antiTrend").addLine(antiTrend)),
                listOf(DoubleDataSet("detectedTrend").addLine(detectedTrend))
        )).show()
    }
}