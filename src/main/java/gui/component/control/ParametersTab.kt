package gui.component.control

import data.Line
import javafx.beans.DefaultProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.Tab
import javafx.scene.layout.VBox
import java.io.IOException
import java.net.URL

@DefaultProperty("extension")
abstract class ParametersTab(text: String) : Tab(text) {
    @FXML
    private lateinit var extension: VBox

    @FXML
    private lateinit var applyButton: Button
    var onApplyClicked: EventHandler<ActionEvent>? = null
        set(value){
            field = value
            applyButton.onAction = onApplyClicked
        }

    var inputLine: Line? = null

    val isInvalidField = SimpleBooleanProperty(this, "isInvalid", true)

    init {
        loadFxml(ParametersTab::class.java.getResource("ParametersTab.fxml"), this)
        applyButton.disableProperty().bind(isInvalidField)
    }

    fun getExtension(): ObservableList<Node> = extension.children

    abstract fun generateResult(): List<Line>

    private fun loadFxml(fxmlFile: URL, rootController: Any) {
        val loader = FXMLLoader(fxmlFile)
        loader.setController(rootController)
        loader.setRoot(rootController)
        try {
            loader.load<Any>()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}