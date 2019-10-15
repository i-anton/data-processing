package console

import data.analysis.CompositeStatistics
import data.analysis.LineStatistics
import data.input.LineGenerator
import data.model.Combine
import data.model.Combine.multiNoiseAvg
import de.gsi.dataset.spi.DoubleDataSet
import gui.ShowCase
import javafx.application.Platform

fun combineDemo(){
    val linear = LineGenerator.linear(1000, -2.0, 0.0)
    val random = LineGenerator.random(1000,-1000.0, 1000.0)
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
    for (item in combinesRaw) {
        println(LineStatistics.stdDev(item))
    }
}

fun stationarityDemo() {
    println(CompositeStatistics.isStationary(
            LineGenerator.random(100, -100.0, 100.0, 0),
            5, 0.05))
}