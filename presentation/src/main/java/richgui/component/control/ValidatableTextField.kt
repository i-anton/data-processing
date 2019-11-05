package richgui.component.control

import javafx.beans.property.ReadOnlyBooleanWrapper
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.VBox

class ValidatableTextField(name: String, fieldFormat: TextFieldFormat) : VBox(), IValidatable {
    private val textField = TextField()
    val isInvalidProp = ReadOnlyBooleanWrapper(this, "isInvalid", true)

    override var isInvalid: Boolean
        get() = isInvalidProp.get()
        set(value) {
            isInvalidProp.value = value
        }

    val text: String
        get() = textField.text

    init {
        textField.textFormatter = fieldFormat.formatter
        textField.textProperty().addListener { _, _, newValue ->
            isInvalid = newValue.isEmpty()
            if (isInvalid) {
                textField.styleClass.add("error")
            } else {
                textField.styleClass.remove("error")
            }
        }
        children.addAll(Label(name), textField)
    }
}