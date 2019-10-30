package gui

import core.Line
import de.gsi.chart.XYChart
import gui.component.inputTabs.*
import gui.component.transformTabs.NormalizeTab
import gui.component.transformTabs.ShiftTab
import gui.component.transformTabs.SpikesTab
import gui.component.transformTabs.StationarityTab
import infrastructure.DataSetTransforms.dataSetSingle
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.ComboBox
import javafx.scene.control.TabPane
import java.net.URL
import java.util.*

class FXMLController : Initializable {
    @FXML
    private lateinit var firstChart: XYChart
    @FXML
    private lateinit var secondChart: XYChart
    @FXML
    private lateinit var thirdChart: XYChart
    @FXML
    private lateinit var fourthChart: XYChart
    @FXML
    private lateinit var inputsTabs: TabPane
    @FXML
    private lateinit var transformTabs: TabPane
    @FXML
    private lateinit var generateToCombo: ComboBox<String>
    @FXML
    private lateinit var transformFromCombo: ComboBox<String>
    @FXML
    private lateinit var transformToCombo: ComboBox<String>

    private var lines = arrayOf(emptyList<Line>(), emptyList(), emptyList(), emptyList())
    private lateinit var charts: Array<XYChart>

    private val inputParameters = listOf(LinearParametersTab(),
            ExponentParametersTab(),
            PiecewiseParametersTab(),
            MyRandomParametersTab(),
            RandomParametersTab()
    )
    private val transformParameters = listOf(NormalizeTab(),
            ShiftTab(),
            SpikesTab(),
            StationarityTab()
    )

    private fun addPlugins(vararg charts: XYChart) {
        for (c in charts) {
            c.xAxis.label = "x"
            c.yAxis.label = "y"
        }
    }

    private fun handleInputsTabs() {
        inputParameters.forEach { comp ->
            comp.onApplyClicked = EventHandler {
                val selectedIdx = generateToCombo.selectionModel
                        .selectedItem.toInt()
                val chart = charts[selectedIdx]
                lines[selectedIdx] = comp.generateResult()
                addToDataset(lines[selectedIdx], chart)

                if (!transformFromCombo.items.contains(selectedIdx.toString())) {
                    transformFromCombo.items.add(selectedIdx.toString())
                }
            }
        }
        inputsTabs.tabs.addAll(inputParameters)
        generateToCombo.items.addAll("0", "1")
        generateToCombo.selectionModel.select(0)
    }

    private fun addToDataset(lines: List<Line>, chart: XYChart) {
        chart.datasets.clear()
        for ((i, l) in lines.withIndex()) {
            chart.datasets.add(dataSetSingle(i.toString(), l).first())
        }
    }

    private fun handleTransformTabs() {
        for (comp in transformParameters) {
            comp.onApplyClicked = EventHandler {
                val selectedFromIdx = transformFromCombo.selectionModel
                        .selectedItem.toInt()
                val selectedToIdx = transformToCombo.selectionModel
                        .selectedItem.toInt()
                comp.inputLine = lines[selectedFromIdx][0]
                val chart = charts[selectedToIdx]
                lines[selectedToIdx] = comp.generateResult()
                addToDataset(lines[selectedToIdx], chart)
            }
        }
        transformTabs.tabs.addAll(transformParameters)
        transformToCombo.items.addAll("2", "3")
        transformToCombo.selectionModel.select(0)
        transformTabs.disableProperty().bind(transformFromCombo.selectionModel
                .selectedItemProperty().isNull)
    }

    override fun initialize(url: URL, rb: ResourceBundle?) {
        charts = arrayOf(firstChart, secondChart, thirdChart, fourthChart)
        handleInputsTabs()
        handleTransformTabs()
        addPlugins(firstChart, secondChart, thirdChart, fourthChart)
    }
}