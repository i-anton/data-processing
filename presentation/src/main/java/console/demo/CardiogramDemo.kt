package console.demo

import core.Line
import core.input.Cardiogram
import core.input.LineGenerator
import infrastructure.DataSetTransforms
import infrastructure.ShowCase
import javafx.application.Platform

object CardiogramDemo {
    fun cardiogramDemo() {
        val dots = 1000
        val freq = 10.0
        val dt = 0.005
        val interval = 200
        val relaxation = 15.0

        val cardioid = Cardiogram.cardiogram(dots, freq, dt, interval, relaxation)
        val baseFunction = Line(Cardiogram.baseFunction(dots, freq, dt, relaxation))
        val deltaFunction = Line(Cardiogram.deltaFunction(dots, interval))
        val combined = Line(LineGenerator.harmonic(1000, 5.0, 15.0))
        Platform.startup {
            ShowCase.multi(
                    DataSetTransforms.dataSetSingle("cardioid", cardioid),
                    DataSetTransforms.dataSetSingle("combined", combined),
                    DataSetTransforms.dataSetSingle("baseFunction",baseFunction),
                    DataSetTransforms.dataSetSingle("deltaFunction", deltaFunction)
            ).show()
        }
    }
}