package gui

import de.gsi.chart.XYChart
import de.gsi.dataset.DataSet
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.layout.ColumnConstraints
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.scene.layout.RowConstraints
import javafx.stage.Stage

class ShowCase {
    private fun genRowConstraint(): RowConstraints {
        val result = RowConstraints()
        result.vgrow = Priority.ALWAYS
        return result
    }

    private fun genColumnConstraint(): ColumnConstraints {
        val result = ColumnConstraints()
        result.hgrow = Priority.ALWAYS
        return result
    }

    private fun createLayout(dataSets: List<List<DataSet>>): Parent {
        val grid = GridPane()
        grid.rowConstraints.addAll(genRowConstraint(), genRowConstraint())
        grid.columnConstraints.addAll(genColumnConstraint(), genColumnConstraint())
        for ((index, set) in dataSets.withIndex()) {
            val chart = XYChart()
            chart.datasets.addAll(set)
            grid.add(chart, index and 1, (index shr 1) and 1)
        }
        return grid
    }

    fun single(data: List<DataSet>): Stage {
        val chart = XYChart()
        chart.datasets.addAll(data)
        val stage = Stage()
        stage.title = "Chart"
        stage.scene = Scene(chart)
        return stage
    }

    fun multi(dataSets: List<List<DataSet>>): Stage {
        require(dataSets.count() == 4) {
            "Input must contain 4 data sets!"
        }
        val group = createLayout(dataSets)
        val scene = Scene(group)
        val stage = Stage()
        stage.title = "Chart"
        stage.scene = scene
        return stage
    }
}