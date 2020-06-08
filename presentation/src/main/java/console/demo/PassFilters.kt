package console.demo

import core.Line
import core.analysis.Fourier.dftRemap
import core.analysis.Fourier.dft
import core.analysis.max
import core.input.PassFilters
import infrastructure.DataSetTransforms
import infrastructure.ShowCase
import javafx.application.Platform

object PassFilters {
    private fun createLines(): List<Line> {
        val fCut = 50.0
        val fCutUpper = 100.0
        val dt = 0.001
        val size = 64
        return listOf(
                Line(PassFilters.highPass(size, dt, fCut)),
                Line(PassFilters.lowPass(size, dt, fCut)),
                Line(PassFilters.bandSelect(size, dt, fCut, fCutUpper)),
                Line(PassFilters.bandPass(size, dt, fCut, fCutUpper))
        )
    }

    fun showFiltersDemo() {
        val lines = createLines()
        lines.forEach { println(it.max())}
        Platform.startup {
            ShowCase.multi(
                    DataSetTransforms.dataSetSingle("hpf", lines[0]),
                    DataSetTransforms.dataSetSingle("lpf", lines[1]),
                    DataSetTransforms.dataSetSingle("bsf", lines[2]),
                    DataSetTransforms.dataSetSingle("bpf", lines[3])
            ).show()
        }
    }
    fun showFiltersDftDemo() {
        val lines = createLines().map { it.dft().dftRemap(1000.0) }
        lines.forEach { println(it.max())}
        Platform.startup {
            ShowCase.multi(
                    DataSetTransforms.dataSetSingle("hpf", lines[0]),
                    DataSetTransforms.dataSetSingle("lpf", lines[1]),
                    DataSetTransforms.dataSetSingle("bsf", lines[2]),
                    DataSetTransforms.dataSetSingle("bpf", lines[3])
            ).show()
        }
    }

    fun highLowPassFilterDft() {
        val fCut = 50.0
        val dt = 0.001
        val size = 64

        val lowPass = Line(PassFilters.lowPass(size, dt, fCut))
        val highPass = Line(PassFilters.highPass(size, dt, fCut))

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