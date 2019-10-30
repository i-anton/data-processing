package console.demo

import core.analysis.CompositeStatistics.autoCorrelation
import core.analysis.CompositeStatistics.dft
import core.analysis.CompositeStatistics.dftRemap
import core.input.LineGenerator.harmonic
import core.input.LineGenerator.linear
import core.input.LineGenerator.random
import core.model.Filter.antiShift
import core.model.Filter.antiTrend
import core.model.SingleTransforms.shift
import core.model.SingleTransforms.spikes
import core.model.add
import infrastructure.DataSetTransforms.dataSetSingle
import infrastructure.ShowCase
import javafx.application.Platform

object DftWithDefects {
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


}