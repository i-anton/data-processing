package richgui.component.transformTabs

import core.model.SingleTransforms.shift
import richgui.component.control.ParametersTab
import richgui.component.control.TextFieldFormat
import richgui.component.control.ValidatableTextField
import richgui.component.or

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
            inputLine.shift(
                    start.text.toDouble(),
                    end.text.toDouble(),
                    shift.text.toDouble(),
                    scale.text.toDouble()
            )
    )
}
