package richgui.component.inputTabs

import core.input.LineGenerator
import richgui.component.control.ParametersTab
import richgui.component.or
import richgui.component.control.TextFieldFormat
import richgui.component.control.ValidatableTextField

class MyRandomParametersTab : ParametersTab("Cлучайный+") {
    private val dots = ValidatableTextField("Всего точек", TextFieldFormat.UINT)
    private val seed = ValidatableTextField("Значение инициализации", TextFieldFormat.UINT)

    init {
        getExtension().addAll(dots, seed)
        isInvalidField.bind(dots.isInvalidProp or seed.isInvalidProp)
    }

    override fun generateResult() = listOf(
            LineGenerator.myRandom(dots.text.toInt())
    )
}
