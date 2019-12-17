package console.demo

import core.Line
import core.analysis.CompositeStatistics.autoCorrelation
import core.analysis.CompositeStatistics.crossCorrelation
import core.analysis.CompositeStatistics.dft
import core.analysis.CompositeStatistics.dftRemap
import core.analysis.CompositeStatistics.dftSeparate
import core.analysis.CompositeStatistics.idft
import core.input.BinFile
import core.input.LineGenerator.harmonic
import core.input.PassFilters
import core.model.Combine.convolution
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

    fun correlationDemo() {
        val path = javaClass.getResource("pgp_f4-1K-1ms.dat").path
        val data = BinFile.readFloatsArray(path)

        val combined = harmonic(1000, 5.0, 15.0) add
                harmonic(1000, 10.0, 45.0)
        val idft = idft(combined.dftSeparate())
        Platform.startup {
            ShowCase.multi(
                    dataSetSingle("data", data),
                    dataSetSingle("idft", idft),
                    dataSetSingle("autoCorrelation", autoCorrelation(data)),
                    dataSetSingle("crossCorrelation", crossCorrelation(data, idft))
            ).show()
        }
    }

    fun filtersDemo() {
        val path = javaClass.getResource("pgp_f4-1K-1ms.dat").path
        val data = BinFile.readFloatsArray(path)
        val size = 64
        val dt = 0.001

        val lpf = PassFilters.lowPassFilter(size, dt, 25.0)
        val hpf = PassFilters.highPassFilter(size, dt, 100.0)
        val bpf = PassFilters.bandPassFilter(size, dt, 25.0, 100.0)
        val bsf = PassFilters.bandSelectFilter(size, dt, 25.0, 100.0)

        val lpfApply = convolution(data, lpf)
        val hpfApply = convolution(data, hpf)
        val bpfApply = convolution(data, bpf)
        val bsfApply = convolution(data, bsf)
        Platform.startup {
            ShowCase.multi(
                    dataSetSingle("lpfApply", lpfApply),
                    dataSetSingle("hpfApply", hpfApply),
                    dataSetSingle("bpfApply", bpfApply),
                    dataSetSingle("bsfApply", bsfApply)
            ).show()
        }

//        Platform.startup {
//            ShowCase.multi(
//                    dataSetSingle("lpfApply", lpfApply.dft().dftRemap(1000.0)),
//                    dataSetSingle("hpfApply", hpfApply.dft().dftRemap(1000.0)),
//                    dataSetSingle("bpfApply", bpfApply.dft().dftRemap(1000.0)),
//                    dataSetSingle("bsfApply", bsfApply.dft().dftRemap(1000.0))
//            ).show()
//        }

//        Platform.startup {
//            ShowCase.multi(
//                    dataSetSingle("lpfApply", Line(lpf).dft().dftRemap(1000.0)),
//                    dataSetSingle("hpfApply", Line(hpf).dft().dftRemap(1000.0)),
//                    dataSetSingle("bpfApply", Line(bpf).dft().dftRemap(1000.0)),
//                    dataSetSingle("bsfApply", Line(bsf).dft().dftRemap(1000.0))
//            ).show()
//        }

//        Platform.startup {
//            ShowCase.multi(
//                    dataSetSingle("lpfApply", Line(lpf).dft().dftRemap(1000.0)),
//                    dataSetSingle("hpfApply", Line(hpf).dft().dftRemap(1000.0)),
//                    dataSetSingle("bpfApply", Line(bpf).dft().dftRemap(1000.0)),
//                    dataSetSingle("bsfApply", Line(bsf).dft().dftRemap(1000.0))
//            ).show()
//        }

//        Platform.startup {
//            ShowCase.multi(
//                    dataSetSingle("dataDft", data.dft().dftRemap(1000.0)),
//                    dataSetSingle("data", data.dft().dftRemap(1000.0)),
//                    dataSetSingle("bpfApply", Line(bpf).dft().dftRemap(1000.0)),
//                    dataSetSingle("bsfApply", Line(bsf).dft().dftRemap(1000.0))
//            ).show()
//        }
    }
}