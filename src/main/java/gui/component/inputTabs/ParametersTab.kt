package gui.component.inputTabs

import data.Line
import javafx.beans.DefaultProperty
import javafx.beans.property.ReadOnlyBooleanWrapper
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
import java.util.*

@DefaultProperty("extension")
abstract class ParametersTab : Tab() {

    @FXML
    private lateinit var extension: VBox

    @FXML
    private lateinit var applyButton: Button
    private var onApplyClicked: EventHandler<ActionEvent>? = null

    var inputLine: Line? = null

    private val isInvalidField = ReadOnlyBooleanWrapper(this, "isInvalid", true)
    protected var isInvalid: Boolean
        get() = isInvalidField.get()
        set(value) { isInvalidField.value = value }

    init {
        loadFxml(ParametersTab::class.java.getResource("ParametersTab.fxml"), this)
        applyButton.disableProperty().bind(isInvalidField)
    }

    fun getExtension(): ObservableList<Node> {
        return extension.children
    }

    fun setOnApplyClicked(onApplyClicked: EventHandler<ActionEvent>) {
        this.onApplyClicked = onApplyClicked
        applyButton.onAction = onApplyClicked
    }

    abstract fun generateResult(): ArrayList<Line>

    protected fun loadFxml(fxmlFile: URL, rootController: Any) {
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
