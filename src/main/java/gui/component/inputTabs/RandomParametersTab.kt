package gui.component.inputTabs

import data.model.LineData
import gui.component.control.ParametersTab
import gui.component.or
import gui.component.control.TextFieldFormat
import gui.component.control.ValidatableTextField

class RandomParametersTab : ParametersTab("Cлучайный") {
    private val dots = ValidatableTextField("Всего точек", TextFieldFormat.UINT)
    private val seed =  ValidatableTextField("Значение инициализации", TextFieldFormat.UINT)

    init {
        getExtension().addAll(dots, seed)
        isInvalidField.bind(dots.isInvalidProp or seed.isInvalidProp)
    }

    override fun generateResult() = listOf(
            LineData.random(dots.text.toInt(), seed.text.toInt())
    )
}
