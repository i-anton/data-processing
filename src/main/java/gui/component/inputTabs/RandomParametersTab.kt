package gui.component.inputTabs

import core.input.LineGenerator
import gui.component.control.ParametersTab
import gui.component.or
import gui.component.control.TextFieldFormat
import gui.component.control.ValidatableTextField

class RandomParametersTab : ParametersTab("Cлучайный") {
    private val dots = ValidatableTextField("Всего точек", TextFieldFormat.UINT)
    private val seed = ValidatableTextField("Значение инициализации", TextFieldFormat.UINT)

    init {
        getExtension().addAll(dots, seed)
        isInvalidField.bind(dots.isInvalidProp or seed.isInvalidProp)
    }

    override fun generateResult() = listOf(
            LineGenerator.random(dots.text.toInt(), 0.0, 1.0, seed.text.toInt())
    )
}
