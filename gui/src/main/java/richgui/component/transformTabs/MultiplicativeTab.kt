package richgui.component.transformTabs

import core.Line
import core.model.SingleTransforms
import richgui.component.control.TextFieldFormat
import richgui.component.control.ValidatableTextField
import richgui.component.control.ParametersTab

class MultiplicativeTab : ParametersTab("Мультипликативный") {
    private val scale = ValidatableTextField("Коэфициент масштаба",TextFieldFormat.DOUBLE)
    init {
        getExtension().add(scale)
        isInvalidField.bind(scale.isInvalidProp)
    }

    override fun generateResult() =
            listOf(SingleTransforms.normalize(Line(inputLine), scale.text.toDouble()))
}
