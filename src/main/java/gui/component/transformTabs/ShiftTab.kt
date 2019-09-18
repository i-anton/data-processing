package gui.component.transformTabs

import data.Line
import data.model.SimpleTransforms
import gui.component.control.TextFieldFormat
import gui.component.control.ValidatableTextField
import gui.component.control.ParametersTab
import gui.component.or

class ShiftTab : ParametersTab("Cмещение") {
    private val start = ValidatableTextField("Начальное X", TextFieldFormat.DOUBLE)
    private val end = ValidatableTextField("Конечное X", TextFieldFormat.DOUBLE)
    private val shift = ValidatableTextField("Смещение", TextFieldFormat.DOUBLE)
    private val scale = ValidatableTextField("Коэфициент масштаба", TextFieldFormat.DOUBLE)

    init {
        getExtension().addAll(start, end, shift, scale)
        isInvalidField.bind(start.isInvalidProp
                or end.isInvalidProp
                or shift.isInvalidProp
                or scale.isInvalidProp)
    }

    override fun generateResult() = listOf(
            SimpleTransforms.shift(Line(inputLine!!), start.text.toDouble(),
                    end.text.toDouble(), shift.text.toDouble(), scale.text.toDouble())
    )
}
