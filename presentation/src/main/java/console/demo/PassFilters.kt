package console.demo

import core.Line
import core.analysis.CompositeStatistics
import core.analysis.CompositeStatistics.dftRemap
import core.analysis.CompositeStatistics.dft
import core.input.LineGenerator
import core.input.PassFilters
import core.model.add
import infrastructure.DataSetTransforms
import infrastructure.ShowCase
import javafx.application.Platform

object PassFilters {
    fun showLowPassDemo() {
        val fCut = 50.0
        val dt = 0.001
        val size = 64

        val halfLowPassFilter = Line(PassFilters.halfLowPassFilter(size, dt, fCut))
        val lowPass = Line(PassFilters.lowPassFilter(size, dt, fCut))
        val highPass = Line(PassFilters.highPassFilter(size, dt, fCut))
        val bandPass = Line(PassFilters.bandPassFilter(size, dt, fCut, 100.0))
        Platform.startup {
            ShowCase.multi(
                    DataSetTransforms.dataSetSingle("bandPass", bandPass),
                    DataSetTransforms.dataSetSingle("highPass", highPass),
                    DataSetTransforms.dataSetSingle("lowPass", lowPass),
                    DataSetTransforms.dataSetSingle("halfLowPass", halfLowPassFilter)
            ).show()
        }
    }
    fun highLowPassFilterDft() {
        val fCut = 50.0
        val dt = 0.001
        val size = 64

        val lowPass = Line(PassFilters.lowPassFilter(size, dt, fCut))
        val highPass = Line(PassFilters.highPassFilter(size, dt, fCut))

        Platform.startup {
            ShowCase.multi(
                    DataSetTransforms.dataSetSingle("highPass", highPass),
                    DataSetTransforms.dataSetSingle("lowPass", lowPass),
                    DataSetTransforms.dataSetSingle("highPassDft", highPass.dft().dftRemap(1000.0)),
                    DataSetTransforms.dataSetSingle("lowPassDft", lowPass.dft().dftRemap(1000.0))
            ).show()
        }
    }
}