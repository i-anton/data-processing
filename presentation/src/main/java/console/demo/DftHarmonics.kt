package console.demo

import core.analysis.CompositeStatistics.dft
import core.analysis.CompositeStatistics.dftRemap
import core.input.LineGenerator.harmonic
import infrastructure.DataSetTransforms.dataSetSingle
import infrastructure.ShowCase
import javafx.application.Platform

object DftHarmonics {
    private val frequencies = listOf(11.0, 110.0, 250.0, 510.0)
    fun harmonicDemo() {
        /* 500 hz is limit*/
        val dataList = frequencies.map {
            dataSetSingle("${it}hz",
                    harmonic(1000, 100.0, it)
            )
        }.toTypedArray()
        Platform.startup { ShowCase.multi(*dataList).show() }
    }

    fun dftDemo() {
        val dataList = frequencies.map {
            dataSetSingle("dft ${it}hz",
                    dft(harmonic(1000, 100.0, it))
            )
        }.toTypedArray()
        Platform.startup { ShowCase.multi(*dataList).show() }
    }

    fun dftRemapDemo() {
        val dots = 1000
        val dataList = frequencies.map {
            dataSetSingle("fixed dft ${it}hz",
                    dftRemap(dft(harmonic(dots, 100.0, it)),
                            dots.toDouble())
            )
        }.toTypedArray()
        Platform.startup { ShowCase.multi(*dataList).show() }
    }
}