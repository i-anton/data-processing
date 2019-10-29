package gui.component.inputTabs

import core.input.LineGenerator
import gui.component.control.ParametersTab
import gui.component.control.TextFieldFormat
import gui.component.control.ValidatableTextField
import gui.component.or

class PiecewiseParametersTab : ParametersTab("Кусочный") {
    private val dots = ValidatableTextField("Всего точек", TextFieldFormat.UINT)
    private val step = ValidatableTextField("Шаг", TextFieldFormat.DOUBLE)

    init {
        getExtension().addAll(dots, step)
        isInvalidField.bind(dots.isInvalidProp or step.isInvalidProp)
    }

    override fun generateResult() = listOf(
            LineGenerator.piecewise(dots.text.toInt(), step.text.toDouble())
    )
}
