package console.demo

import core.Line
import core.input.Cardioid
import core.input.LineGenerator
import infrastructure.DataSetTransforms
import infrastructure.ShowCase
import javafx.application.Platform

object CardioidDemo {
    fun cardioidDemo() {
        val dots = 1000
        val freq = 10.0
        val amplitude = 120.0
        val dt = 0.005
        val interval = 200
        val relaxation = 15.0

        val cardioid = Cardioid.cardioid(dots, freq, amplitude, dt, interval, relaxation)
        val baseFunction = Line(Cardioid.baseFunction(dots, freq, amplitude, dt, relaxation))
        val deltaFunction = Line(Cardioid.deltaFunction(dots, interval))
        val combined = LineGenerator.harmonic(1000, 5.0, 15.0)
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