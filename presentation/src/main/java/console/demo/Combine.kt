package console.demo

import core.analysis.LineStatistics
import core.input.LineGenerator.linear
import core.input.LineGenerator.random
import core.model.Combine.multiNoiseAvg
import core.model.add
import core.model.mul
import infrastructure.DataSetTransforms.dataSetSingle
import infrastructure.ShowCase
import javafx.application.Platform

object Combine {
    fun combine() {
        val linear = linear(1000, -2.0, 0.0)
        val random = random(1000, -1000.0, 1000.0)
        Platform.startup {
            ShowCase.multi(
                    dataSetSingle("Random", random),
                    dataSetSingle("Add", random add linear),
                    dataSetSingle("Multiply", random mul linear),
                    dataSetSingle("Linear", linear)
            ).show()
        }
    }

    fun combineAvg() {
        val lines = listOf(2, 5, 10, 20).map {
            multiNoiseAvg(100.0, 1000, it)
        }
        Platform.startup {
            lines.map { dataSetSingle("", it) }
                    .toTypedArray()
                    .let { ShowCase.multi(*it).show() }
        }
        lines.forEach { println(LineStatistics.stdDev(it)) }
    }
}