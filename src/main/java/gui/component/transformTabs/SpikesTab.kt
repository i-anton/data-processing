package gui.component.transformTabs

import data.Line
import data.model.SimpleTransforms
import gui.component.control.TextFieldFormat
import gui.component.control.ValidatableTextField
import gui.component.control.ParametersTab
import gui.component.or

class SpikesTab : ParametersTab("Пики") {
    private val spike = ValidatableTextField("Всего пиков", TextFieldFormat.UINT)
    private val seed = ValidatableTextField("Значение инициализации", TextFieldFormat.UINT)
    private val scale = ValidatableTextField("Масштабирование", TextFieldFormat.DOUBLE)

    init {
        getExtension().addAll(spike, seed, scale)
        isInvalidField.bind(spike.isInvalidProp
                or seed.isInvalidProp
                or scale.isInvalidProp)
    }

    override fun generateResult() = listOf(
            SimpleTransforms.spikes(Line(inputLine), seed.text.toInt(),
                    spike.text.toInt(), scale.text.toDouble())
    )
}
