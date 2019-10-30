package richgui.component.inputTabs

import core.input.LineGenerator
import richgui.component.control.ParametersTab
import richgui.component.control.TextFieldFormat
import richgui.component.control.ValidatableTextField
import richgui.component.or

class ExponentParametersTab : ParametersTab("Экспонента") {
    private val dots = ValidatableTextField("Всего точек", TextFieldFormat.UINT)
    private val koef = ValidatableTextField("Коэффициент", TextFieldFormat.DOUBLE)
    private val degree = ValidatableTextField("Степень", TextFieldFormat.DOUBLE)

    init {
        getExtension().addAll(dots, degree, koef)
        isInvalidField.bind(dots.isInvalidProp
                or degree.isInvalidProp
                or koef.isInvalidProp)
    }

    override fun generateResult() = listOf(
            LineGenerator.exponent(dots.text.toInt(), koef.text.toDouble(), degree.text.toDouble())
    )
}
