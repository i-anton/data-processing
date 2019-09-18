package gui.component.control

import javafx.beans.property.ReadOnlyBooleanWrapper
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.scene.layout.VBox
import javafx.util.converter.NumberStringConverter
import java.text.NumberFormat

class ValidatableTextField(name: String, fieldFormat: TextFieldFormat) : VBox(), IValidatable {
    private val textField = TextField()
    val isInvalidProp = ReadOnlyBooleanWrapper(this, "isInvalid", true)

    override var isInvalid: Boolean
        get() = isInvalidProp.get()
        set(value) { isInvalidProp.value = value }

    val text: String
        get() = textField.text

    private fun textFormatter(field: TextField, format: TextFieldFormat) {
        when (format) {
            TextFieldFormat.DOUBLE -> {
                val nf = NumberFormat.getNumberInstance()
                nf.isGroupingUsed = false
                field.textFormatter = TextFormatter<Number>(NumberStringConverter(nf))
            }
            TextFieldFormat.UINT ->
                field.textFormatter = TextFormatter<Any> { change ->
                if (change.controlNewText.matches("([1-9][0-9]*)?".toRegex())) change else null }
        }
    }

    init {
        children.add(Label(name))
        textFormatter(textField,fieldFormat)
        textField.textProperty().addListener { _, _, newValue ->
            isInvalid = newValue.isEmpty()
            if (isInvalid) {
                textField.styleClass.add("error")
            } else {
                textField.styleClass.remove("error")
            }
        }
        children.add(textField)
    }
}