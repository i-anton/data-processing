package richgui.component.transformTabs

import core.model.SingleTransforms
import richgui.component.control.ParametersTab
import richgui.component.control.TextFieldFormat
import richgui.component.control.ValidatableTextField
import richgui.component.or

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
            SingleTransforms.spikes(inputLine,
                    spike.text.toInt(), scale.text.toDouble(), seed.text.toInt())
    )
}
