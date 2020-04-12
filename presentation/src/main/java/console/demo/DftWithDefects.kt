package console.demo

import core.analysis.Correlation.autoCorrelation
import core.analysis.Fourier.dft
import core.analysis.Fourier.dftRemap
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
        val shiftedRand = harmonic(1000, 10.0, 15.0).shift(100.0, 1.0)
        val dftShifted = shiftedRand.dft().dftRemap(500.0)
        val antiShifted = shiftedRand.antiShift()
        val dftAntiShifted = antiShifted.dft().dftRemap(500.0)
        Platform.startup {
            ShowCase.multi(
                    dataSetSingle("shifted", shiftedRand),
                    dataSetSingle("antiShifted", antiShifted),
                    dataSetSingle("dftShifted", dftShifted),
                    dataSetSingle("dftAntiShifted", dftAntiShifted)
            ).show()
        }
    }

    fun harmonicWithTrend() {
        val trendy = harmonic(1000, 10.0, 15.0) add
                linear(1000, -1.0, 0.0)
        val dft = trendy.dft().dftRemap(500.0)
        val antiTrend = trendy.antiTrend()
        val dftAntiTrend = antiTrend.dft().dftRemap(500.0)
        Platform.startup {
            ShowCase.multi(
                    dataSetSingle("trend", trendy),
                    dataSetSingle("antiTrend", antiTrend),
                    dataSetSingle("dftTrend", dft),
                    dataSetSingle("dftAntiTrend", dftAntiTrend)
            ).show()
        }
    }

    fun dftForSpikes() {
        val initialRand = random(1000, 10.0, 15.0)
        val spikes = initialRand.spikes(4, 100.0)
        val dft = initialRand.dft().dftRemap(500.0)
        val dftSpiked = spikes.dft().dftRemap(500.0)
        Platform.startup {
            ShowCase.multi(
                    dataSetSingle("line", initialRand),
                    dataSetSingle("spiked", spikes),
                    dataSetSingle("dft", dft),
                    dataSetSingle("dftSpiked", dftSpiked)
            ).show()
        }
    }

    fun dftHarmonicWithSpikes() {
        val spikes = harmonic(1000, 10.0, 15.0).spikes( 4, 100.0)
        val combined = spikes add random(1000, -100.0, 100.0, 42)
        val dftSpikes = spikes.dft().dftRemap(500.0)
        val dftCombined = combined.dft().dftRemap(500.0)
        Platform.startup {
            ShowCase.multi(
                    dataSetSingle("combined", combined),
                    dataSetSingle("spikes", spikes),
                    dataSetSingle("dftCombined", dftCombined),
                    dataSetSingle("dftSpikes", dftSpikes)
            ).show()
        }
    }

    fun autoCorrelationForHarmonic() {
        val initial = harmonic(1000, 10.0, 15.0)
        val combined = initial add random(1000, -30.0, 30.0, 42)
        val correlation = autoCorrelation(initial)
        val correlationNoisy = autoCorrelation(combined)
        Platform.startup {
            ShowCase.multi(
                    dataSetSingle("line", initial),
                    dataSetSingle("noisy", combined),
                    dataSetSingle("correlation", correlation),
                    dataSetSingle("correlationNoisy", correlationNoisy)
            ).show()
        }
    }
}