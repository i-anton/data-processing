package console.demo

import core.Line
import core.analysis.Convolution.deconvolution
import core.input.Cardiogram
import infrastructure.DataSetTransforms
import infrastructure.ShowCase
import javafx.application.Platform

object DeconvolveDemo {

    fun deconvolveDemo() {
        val dots = 1000
        val freq = 10.0
        val dt = 0.005
        val interval = 200
        val relaxation = 15.0

        val cardioid = Cardiogram.cardiogram(dots, freq, dt, interval, relaxation)
        val baseFunction = Line(Cardiogram.baseFunction(dots, freq, dt, relaxation))
        val deltaFunction = Line(Cardiogram.deltaFunction(dots, interval))
        val deconvolved = cardioid.deconvolution(baseFunction)

        Platform.startup {
            ShowCase.multi(
                    DataSetTransforms.dataSetSingle("cardioid", cardioid),
                    DataSetTransforms.dataSetSingle("deconvolved", deconvolved),
                    DataSetTransforms.dataSetSingle("baseFunction", baseFunction),
                    DataSetTransforms.dataSetSingle("deltaFunction", deltaFunction)
            ).show()
        }
    }
}