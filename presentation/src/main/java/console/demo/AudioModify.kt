package console.demo

import core.Line
import core.analysis.Fourier.dft
import core.analysis.Fourier.dftRemap
import core.input.WavFile.readFromFile
import core.input.WavFile.writeToFile
import infrastructure.DataSetTransforms
import infrastructure.ShowCase
import javafx.application.Platform

object AudioModify {
    fun openAudioFile() {
        val path = javaClass.getResource("ma.wav").path
        val data = readFromFile(path)

        val line = Line(data.second)
        val writeTo = "macopy2.wav"
        writeToFile(writeTo, data.first, data.second)
        val dataRestored = readFromFile(writeTo)
        val lineRestored = Line(dataRestored.second)//.mul(2.0)
        var start = System.nanoTime()
        val initialDft = line.dft()
        var end = System.nanoTime()
        initialDft.dftRemap(data.first.sampleRate.toDouble())
        println(end - start)
        start = System.nanoTime()
        val restoredDft = lineRestored.dft().dftRemap(data.first.sampleRate.toDouble())
        end = System.nanoTime()
        println(end - start)

        Platform.startup {
            ShowCase.multi(
                    DataSetTransforms.dataSetSingle("file", line),
                    DataSetTransforms.dataSetSingle("opened", lineRestored),
                    DataSetTransforms.dataSetSingle("file", initialDft),
                    DataSetTransforms.dataSetSingle("opened", restoredDft)
            ).show()
        }


    }
}