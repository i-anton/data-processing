package console

import data.input.LineGenerator
import data.model.Combine
import de.gsi.dataset.spi.DoubleDataSet
import gui.ShowCase
import javafx.application.Platform

fun combineDemo(){
    val linear = LineGenerator.linear(1000, -2.0, 0.0)
    val random = LineGenerator.random(1000,-1000.0, 1000.0)
    val dataList = listOf(
            listOf(DoubleDataSet("Random").addLine(random)),
            listOf(DoubleDataSet("Add").addLine(Combine.additive(random, linear))),
            listOf(DoubleDataSet("Multiply").addLine(Combine.multiplicative(random, linear))),
            listOf(DoubleDataSet("Linear").addLine(linear))
    )
    Platform.startup {
        ShowCase().multi(dataList).show()
    }
}