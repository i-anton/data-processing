package console.demo

import core.analysis.CompositeStatistics.autoCorrelation
import core.analysis.CompositeStatistics.crossCorrelation
import core.analysis.CompositeStatistics.dft
import core.analysis.CompositeStatistics.dftRemap
import core.input.BinFile
import core.input.LineGenerator.harmonic
import core.model.add
import infrastructure.DataSetTransforms.dataSetSingle
import infrastructure.ShowCase
import javafx.application.Platform

object FloatArrayAnalysis {
    fun testLoad() {
        val path = javaClass.getResource("pgp_f4-1K-1ms.dat").path
        val data = BinFile.readFloatsArray(path)
        val dft = data.dft()
        val dftRemapped = dft.dftRemap(1000.0)
        val amplitudesFrequenciesPairs = (0 until dftRemapped.size)
                .filter { dftRemapped.ys[it] >= 0.001 }
                .map { Pair(dftRemapped.xs[it], dftRemapped.ys[it]) }
        amplitudesFrequenciesPairs.forEach {
            println("Frequency: ${it.first} \t Amplitude:${it.second}")
        }
        val restored = amplitudesFrequenciesPairs
                .map { harmonic(1000, it.second * 2.0, it.first) }
                .reduce { acc, line -> acc add line }
        val some = harmonic(1000, 30.0, 5.0)
        Platform.startup {
            ShowCase.multi(
                    dataSetSingle("input", data),
                    dataSetSingle("dft", some),
                    dataSetSingle("dftRemap", dftRemapped),
                    dataSetSingle("combined", crossCorrelation(data, some))
            ).show()
        }
    }
}