package gui.component.inputTabs

import data.model.LineData
import gui.component.control.ParametersTab
import gui.component.or
import gui.component.control.TextFieldFormat
import gui.component.control.ValidatableTextField

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
            LineData.exponent(dots.text.toInt(), koef.text.toDouble(), degree.text.toDouble())
    )
}
