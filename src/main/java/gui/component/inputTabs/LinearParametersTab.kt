package gui.component.inputTabs

import data.input.LineGenerator
import gui.component.control.ParametersTab
import gui.component.or
import gui.component.control.TextFieldFormat
import gui.component.control.ValidatableTextField

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
