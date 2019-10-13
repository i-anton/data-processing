package gui.component.transformTabs

import data.Line
import data.model.SingleTransforms
import gui.component.control.TextFieldFormat
import gui.component.control.ValidatableTextField
import gui.component.control.ParametersTab

class MultiplicativeTab : ParametersTab("Мультипликативный") {
    private val scale = ValidatableTextField("Коэфициент масштаба",TextFieldFormat.DOUBLE)
    init {
        getExtension().add(scale)
        isInvalidField.bind(scale.isInvalidProp)
    }

    override fun generateResult() =
            listOf(SingleTransforms.normalize(Line(inputLine), scale.text.toDouble()))
}
