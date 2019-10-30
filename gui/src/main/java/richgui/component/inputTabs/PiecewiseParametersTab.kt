package richgui.component.inputTabs

import core.input.LineGenerator
import richgui.component.control.ParametersTab
import richgui.component.control.TextFieldFormat
import richgui.component.control.ValidatableTextField
import richgui.component.or

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
