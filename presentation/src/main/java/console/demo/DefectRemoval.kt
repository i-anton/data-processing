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
        val shiftedRand = initialRand.shift(100.0, 1.0)
        val antiShift = shiftedRand.antiShift()
        Platform.startup {
            ShowCase.multi(
                    dataSetSingle("line", initialRand),
                    dataSetSingle("shifted", shiftedRand),
                    dataSetSingle("antiShift", antiShift),
                    emptyList()
            ).show()
        }
    }

    fun removeSpikesDemo() {
        val initialRand = random(100, -1.0, 1.0)
        val spikedRand = initialRand.spikes(10, 5.0)

        val antiSpike = spikedRand.antiSpike(1.0)
        val antiSpikeWin = spikedRand.antiSpikeWindowed(1.0, 1)

        Platform.startup {
            ShowCase.multi(
                    dataSetMulti(
                            Pair("line", initialRand),
                            Pair("antiSpike", antiSpike),
                            Pair("antiSpikeWin", antiSpikeWin)
                    ),
                    dataSetSingle("spiked", spikedRand),
                    dataSetSingle("antiSpike", antiSpike),
                    dataSetSingle("antiSpikeWin", antiSpikeWin)
            ).show()
        }
    }

    fun trendDetectionDemo() {
        val dots = 100
        val spikedRand = random(dots, -1.0, 1.0).spikes(10, 5.0)
        val trendy = spikedRand add linear(dots, 0.75, 50.0)
        val windowSize = 3
        val antiTrend = trendy.antiTrend(windowSize)
        val detectedTrend = Line(trendy.trendDetect(windowSize))
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