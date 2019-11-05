package console.demo

import core.Line
import core.input.BinFile
import infrastructure.DataSetTransforms.dataSetSingle
import core.analysis.CompositeStatistics.dft
import core.analysis.CompositeStatistics.dftRemap
import core.input.LineGenerator
import core.input.LineGenerator.harmonic
import core.model.add
import infrastructure.ShowCase
import javafx.application.Platform

object FloatArrayAnalysis {
    fun testLoad() {
        val path = javaClass.getResource("pgp_f4-1K-1ms.dat").path
        val data = BinFile.readFloatsArray(path)
        val dft = data.dft()
        val dftRemapped = dft.dftRemap(1000.0)
        val amplitudesFrequenciesPairs = (0 until dftRemapped.size)
                .filter { dftRemapped.ys[it] >= 0.1 }
                .map { Pair(dftRemapped.xs[it], dftRemapped.ys[it]) }

        amplitudesFrequenciesPairs.forEach {
            println("Frequency: ${it.first} \t Amplitude:${it.second}")
        }

        val restored = amplitudesFrequenciesPairs
                .map { harmonic(1000, it.second*2.0, it.first) }
                .reduce { acc, line -> acc add line }
        Platform.startup {
            ShowCase.multi(
                    dataSetSingle("input", data),
                    dataSetSingle("dft", dft),
                    dataSetSingle("dftRemap", dftRemapped),
                    dataSetSingle("combined", restored)
            ).show()
        }
    }
}