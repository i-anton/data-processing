package console

import core.Line
import core.analysis.CompositeStatistics.autoCorrelation
import core.analysis.CompositeStatistics.crossCorrelation
import core.analysis.CompositeStatistics.dft
import core.analysis.CompositeStatistics.dftRemap
import core.analysis.CompositeStatistics.isStationary
import core.analysis.LineStatistics.stdDev
import core.input.LineGenerator.harmonic
import core.input.LineGenerator.linear
import core.input.LineGenerator.random
import core.model.Combine.multiNoiseAvg
import core.model.Filter.antiShift
import core.model.Filter.antiSpike
import core.model.Filter.antiSpikeWindowed
import core.model.Filter.antiTrend
import core.model.Filter.trendDetect
import core.model.SingleTransforms.shift
import core.model.SingleTransforms.spikes
import core.model.add
import core.model.mul
import infrastructure.DataSetTransforms.dataSetMulti
import infrastructure.DataSetTransforms.dataSetSingle
import infrastructure.ShowCase
import javafx.application.Platform

fun combineDemo() {
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

fun combineAvgDemo() {
    val lines = listOf(2, 5, 10, 20).map {
        multiNoiseAvg(100.0, 1000, it)
    }
    Platform.startup {
        lines.map { dataSetSingle("", it) }
                .toTypedArray()
                .let { ShowCase.multi(*it).show() }
    }
    lines.forEach { println(stdDev(it)) }
}

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

fun harmonicDemo() {
    /* 500 hz is limit*/
    val frequencies = listOf(11.0, 110.0, 250.0, 510.0)
    val dataList = frequencies.map {
        dataSetSingle("${it}hz",
                harmonic(1000, 100.0, it)
        )
    }.toTypedArray()
    Platform.startup { ShowCase.multi(*dataList).show() }
}

fun dftDemo() {
    val frequencies = listOf(11.0, 110.0, 250.0, 510.0)
    val dataList = frequencies.map {
        dataSetSingle("dft ${it}hz",
                dft(harmonic(1000, 100.0, it))
        )
    }.toTypedArray()
    Platform.startup { ShowCase.multi(*dataList).show() }
}

fun dftRemapDemo() {
    val frequencies = listOf(11.0, 110.0, 250.0, 510.0)
    val dots = 1000
    val dataList = frequencies.map {
        dataSetSingle("fixed dft ${it}hz",
                dftRemap(dft(harmonic(dots, 100.0, it)),
                        dots.toDouble())
        )
    }.toTypedArray()
    Platform.startup { ShowCase.multi(*dataList).show() }
}

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

fun harmonicWithShift() {
    val initialRand = harmonic(1000, 10.0, 15.0)
    val shiftedRand = shift(initialRand, 100.0, 1.0)

    val dft = dftRemap(dft(shiftedRand), 500.0)
    val antiShifted = antiShift(shiftedRand)
    val dft2 = dftRemap(dft(antiShifted), 500.0)
    Platform.startup {
        ShowCase.multi(
                dataSetSingle("line", shiftedRand),
                dataSetSingle("shifted", antiShifted),
                dataSetSingle("f(random)", dft),
                dataSetSingle("random", dft2)
        ).show()
    }
}

fun harmonicWithTrend() {
    val newRand = harmonic(1000, 10.0, 15.0) add
            linear(1000, -1.0, 0.0)
    val dft = dftRemap(dft(newRand), 500.0)
    val antiShifted = antiTrend(newRand)
    val dft2 = dftRemap(dft(antiShifted), 500.0)
    Platform.startup {
        ShowCase.multi(
                dataSetSingle("line", newRand),
                dataSetSingle("shifted", antiShifted),
                dataSetSingle("f(random)", dft),
                dataSetSingle("random", dft2)
        ).show()
    }
}

fun dftForSpikes() {
    val initialRand = random(1000, 10.0, 15.0)
    val spikes = spikes(initialRand, 4, 100.0)
    val dft = dftRemap(dft(initialRand), 500.0)
    val dft2 = dftRemap(dft(spikes), 500.0)
    Platform.startup {
        ShowCase.multi(
                dataSetSingle("line", initialRand),
                dataSetSingle("spiked", spikes),
                dataSetSingle("dft", dft),
                dataSetSingle("dft2", dft2)
        ).show()
    }
}

fun dftHarmonicWithSpikes() {
    val initialRand = harmonic(1000, 10.0, 15.0)
    val spikes = spikes(initialRand, 4, 100.0)
    val combined = spikes add random(1000, -30.0, 30.0, 42)
    val dft = dftRemap(dft(spikes), 500.0)
    val dft2 = dftRemap(dft(combined), 500.0)
    Platform.startup {
        ShowCase.multi(
                dataSetSingle("line", combined),
                dataSetSingle("spiked", spikes),
                dataSetSingle("dft", dft),
                dataSetSingle("dft2", dft2)
        ).show()
    }
}

fun autoCorrelationForHarmonic() {
    val initialRand = harmonic(1000, 10.0, 15.0)
    val combined = initialRand add random(1000, -30.0, 30.0, 42)
    val dft = autoCorrelation(initialRand)
    val dft2 = autoCorrelation(combined)
    Platform.startup {
        ShowCase.multi(
                dataSetSingle("line", combined),
                dataSetSingle("spiked", initialRand),
                dataSetSingle("dft", dft2),
                dataSetSingle("dft2", dft)
        ).show()
    }
}
