package console.demo

import core.analysis.CompositeStatistics.dftSeparate
import core.analysis.CompositeStatistics.idft
import core.analysis.CompositeStatistics.dftRemap
import core.analysis.CompositeStatistics.toAmplitudes
import core.input.LineGenerator
import core.model.add
import infrastructure.DataSetTransforms
import infrastructure.ShowCase
import javafx.application.Platform

object IDFT {
    fun idftDemo() {
        val single = LineGenerator.harmonic(1000, 5.0, 15.0)
        val combined = single add LineGenerator.harmonic(1000, 10.0, 45.0)
        val dft = combined.dftSeparate()
        val dftCombined = toAmplitudes(dft).dftRemap(1000.0)
        val idftCombined = idft(dft)
        Platform.startup {
            ShowCase.multi(
                    DataSetTransforms.dataSetSingle("harmonic", single),
                    DataSetTransforms.dataSetSingle("combined", combined),
                    DataSetTransforms.dataSetSingle("dftCombined", dftCombined),
                    DataSetTransforms.dataSetSingle("idftCombined", idftCombined)
            ).show()
        }
    }
}