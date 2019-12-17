package console.demo

import core.Line
import core.analysis.CompositeStatistics.dft
import core.analysis.CompositeStatistics.dftRemap
import core.input.WavFile.readFromFile
import infrastructure.DataSetTransforms
import infrastructure.ShowCase
import javafx.application.Platform

object AudioModify {
    fun openAudioFile() {
        val path = javaClass.getResource("r2d2_01.wav").path
        val data = readFromFile(path)

        //val res = FloatArray(data.data.size)
        val line = Line(data.second)
        Platform.startup {
            ShowCase.single(
                    DataSetTransforms.dataSetSingle("file", line)
            ).show()
        }

    }
}