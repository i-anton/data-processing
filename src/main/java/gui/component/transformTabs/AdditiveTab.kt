package gui.component.transformTabs

import core.Line
import core.model.SingleTransforms
import gui.component.control.TextFieldFormat
import gui.component.control.ValidatableTextField
import gui.component.control.ParametersTab

class AdditiveTab : ParametersTab("Аддитивынй") {
    private val scale = ValidatableTextField("Коэфициент масштаба",TextFieldFormat.DOUBLE)
    init {
        getExtension().add(scale)
        isInvalidField.bind(scale.isInvalidProp)
    }

    override fun generateResult() =
            listOf(SingleTransforms.normalize(Line(inputLine), scale.text.toDouble()))
}
