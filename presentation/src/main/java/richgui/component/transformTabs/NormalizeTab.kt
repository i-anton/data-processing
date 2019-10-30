package richgui.component.transformTabs

import core.model.SingleTransforms.normalize
import richgui.component.control.TextFieldFormat
import richgui.component.control.ValidatableTextField
import richgui.component.control.ParametersTab

class NormalizeTab : ParametersTab("Нормализация") {
    private val scale = ValidatableTextField("Коэфициент масштаба",TextFieldFormat.DOUBLE)
    init {
        getExtension().add(scale)
        isInvalidField.bind(scale.isInvalidProp)
    }

    override fun generateResult() =
            listOf(inputLine.normalize(scale.text.toDouble()))
}
