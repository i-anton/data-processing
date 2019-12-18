package console.demo

import core.Line
import core.analysis.CompositeStatistics.dft
import core.analysis.CompositeStatistics.dftRemap
import core.input.PassFilters
import core.input.WavFile.readFromFile
import core.input.WavFile.writeToFile
import core.model.Combine
import core.model.mul
import infrastructure.DataSetTransforms
import infrastructure.ShowCase
import javafx.application.Platform

object AudioModify {
    fun openAudioFile() {
        val path = javaClass.getResource("ma.wav").path
        val data = readFromFile(path)

        val line = Line(data.second)
        writeToFile("macopy2.wav", data.first, data.second)
        val dataRestored = readFromFile("macopy2.wav")
        val lineRestored = Line(dataRestored.second)//.mul(2.0)
        Platform.startup {
            ShowCase.multi(
                    DataSetTransforms.dataSetSingle("file", line),
                    DataSetTransforms.dataSetSingle("opened", lineRestored),
                    DataSetTransforms.dataSetSingle("file", line.dft().dftRemap(data.first.sampleRate.toDouble())),
                    DataSetTransforms.dataSetSingle("opened", lineRestored.dft().dftRemap(data.first.sampleRate.toDouble()))
            ).show()
        }


    }
}