package gui.component.transformTabs

import data.Line
import data.model.SimpleTransforms
import gui.component.control.TextFieldFormat
import gui.component.control.ValidatableTextField
import gui.component.control.ParametersTab

class NormalizeTab : ParametersTab("Нормализация") {
    private val scale = ValidatableTextField("Коэфициент масштаба",TextFieldFormat.DOUBLE)
    init {
        getExtension().add(scale)
        isInvalidField.bind(scale.isInvalidProp)
    }

    override fun generateResult() =
            listOf(SimpleTransforms.normalize(Line(inputLine!!), java.lang.Double.parseDouble(scale.text)))
}
