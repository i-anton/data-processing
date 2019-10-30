package infrastructure

import de.gsi.chart.XYChart
import de.gsi.dataset.DataSet
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.scene.layout.RowConstraints
import javafx.stage.Stage

object ShowCase {
    private fun genRowConstraint() = RowConstraints().apply { vgrow = Priority.ALWAYS }

    private fun genColumnConstraint() = ColumnConstraints().apply { hgrow = Priority.ALWAYS }

    private fun createLayout(vararg dataSets: List<DataSet>): Parent {
        val grid = GridPane().apply {
            rowConstraints.addAll(genRowConstraint(), genRowConstraint())
            columnConstraints.addAll(genColumnConstraint(), genColumnConstraint())
        }
        for ((index, set) in dataSets.withIndex())
            grid.add(XYChart().apply { datasets.addAll(set) },
                    index and 1, (index shr 1) and 1)
        return grid
    }

    fun single(data: List<DataSet>) = Stage().apply {
        title = "Chart"
        scene = Scene(XYChart().apply {
            datasets.addAll(data)
        })
    }

    fun multi(vararg dataSets: List<DataSet>): Stage {
        require(dataSets.count() == 4) {
            "Input must contain 4 data sets!"
        }
        return Stage().apply {
            title = "Chart"
            scene = Scene(createLayout(*dataSets))
        }
    }
}