package richgui.component.inputTabs

import core.input.LineGenerator
import richgui.component.or
import richgui.component.control.ParametersTab
import richgui.component.control.TextFieldFormat
import richgui.component.control.ValidatableTextField

class LinearParametersTab : ParametersTab("Линейный") {
    private val dots = ValidatableTextField("Всего точек", TextFieldFormat.UINT)
    private val angle = ValidatableTextField("Наклон", TextFieldFormat.DOUBLE)
    private val offset = ValidatableTextField("Смещение", TextFieldFormat.DOUBLE)

    init {
        getExtension().addAll(dots, angle, offset)
        isInvalidField.bind(dots.isInvalidProp
                or angle.isInvalidProp
                or offset.isInvalidProp)
    }

    override fun generateResult() = listOf(
            LineGenerator.linear(dots.text.toInt(), angle.text.toDouble(), offset.text.toDouble())
    )
}
