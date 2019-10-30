package console.demo

import core.Line
import core.input.LineGenerator.linear
import core.input.LineGenerator.random
import core.model.Filter.antiShift
import core.model.Filter.antiSpike
import core.model.Filter.antiSpikeWindowed
import core.model.Filter.antiTrend
import core.model.Filter.trendDetect
import core.model.SingleTransforms.shift
import core.model.SingleTransforms.spikes
import core.model.add
import infrastructure.DataSetTransforms.dataSetMulti
import infrastructure.DataSetTransforms.dataSetSingle
import infrastructure.ShowCase
import javafx.application.Platform

object DefectRemoval {
    fun removeConstantShiftDemo() {
        val initialRand = random(100, -1.0, 1.0)
        val shiftedRand = shift(initialRand, 100.0, 1.0)
        val fixed = antiShift(shiftedRand)
        Platform.startup {
            ShowCase.multi(
                    dataSetSingle("line", initialRand),
                    dataSetSingle("shifted", shiftedRand),
                    dataSetSingle("f(random)", fixed),
                    dataSetSingle("random", fixed)
            ).show()
        }
    }

    fun removeSpikesDemo() {
        val initialRand = random(100, -1.0, 1.0)
        val spikedRand = spikes(initialRand, 10, 5.0)

        val fixed = antiSpike(spikedRand, 1.0)
        val fixed2 = antiSpikeWindowed(spikedRand, 1.0, 1)

        Platform.startup {
            ShowCase.multi(
                    dataSetMulti(
                            Pair("line", initialRand),
                            Pair("filtered", fixed),
                            Pair("filtered2", fixed2)
                    ),
                    dataSetSingle("spiked", spikedRand),
                    dataSetSingle("filtered", fixed),
                    dataSetSingle("filtered", fixed)
            ).show()
        }
    }

    fun trendDetectionDemo() {
        val dots = 100
        val initialRand = random(dots, -1.0, 1.0)
        val spikedRand = spikes(initialRand, 10, 5.0)
        val trendy = spikedRand add linear(dots, 0.75, 50.0)
        val windowSize = 3
        val antiTrend = antiTrend(trendy, windowSize)
        val detectedTrend = Line(trendDetect(trendy, windowSize))
        Platform.startup {
            ShowCase.multi(
                    dataSetSingle("line", spikedRand),
                    dataSetSingle("trendy", trendy),
                    dataSetSingle("antiTrend", antiTrend),
                    dataSetSingle("detectedTrend", detectedTrend)
            ).show()
        }
    }

}