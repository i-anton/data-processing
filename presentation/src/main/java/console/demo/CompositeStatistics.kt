package console.demo

import core.analysis.CompositeStatistics
import core.analysis.CompositeStatistics.autoCorrelation
import core.analysis.CompositeStatistics.crossCorrelation
import core.analysis.CompositeStatistics.isStationary
import core.input.LineGenerator
import core.input.LineGenerator.linear
import core.input.LineGenerator.random
import infrastructure.DataSetTransforms
import infrastructure.DataSetTransforms.dataSetSingle
import infrastructure.ShowCase
import javafx.application.Platform

object CompositeStatistics {
    fun stationarityDemo() = println(isStationary(
            random(100, -100.0, 100.0, 0),
            5, 0.05))

    fun autoCorrelationDemo() {
        val initial = linear(100, 1.0, 0.0)
        val initialRand = random(100)
        val stats = autoCorrelation(initial)
        val statsRand = autoCorrelation(initialRand)
        Platform.startup {
            ShowCase.multi(
                    dataSetSingle("f(line)", stats),
                    dataSetSingle("line", initial),
                    dataSetSingle("f(random)", statsRand),
                    dataSetSingle("random", initialRand)
            ).show()
        }
    }

    fun crossCorrelationDemo() {
        val initial = linear(100, 1.0, 0.0)
        val initialRand = random(100)
        val stats = autoCorrelation(initialRand)
        val statsRand = crossCorrelation(initialRand, initialRand)
        Platform.startup {
            ShowCase.multi(
                    dataSetSingle("f(line)", stats),
                    dataSetSingle("line", initial),
                    dataSetSingle("f(random)", statsRand),
                    dataSetSingle("random", initialRand)
            ).show()
        }
    }
}