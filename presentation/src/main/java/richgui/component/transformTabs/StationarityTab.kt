package richgui.component.transformTabs

import core.analysis.CompositeStatistics
import richgui.component.control.ParametersTab
import richgui.component.control.TextFieldFormat
import richgui.component.control.ValidatableTextField
import richgui.component.or

class StationarityTab : ParametersTab("Анализ стационарности") {
    private val intervals = ValidatableTextField("Количество интервалов",TextFieldFormat.UINT)
    private val error = ValidatableTextField("Погрешность", TextFieldFormat.DOUBLE)

    init {
        getExtension().addAll(intervals,error)
        isInvalidField.bind(intervals.isInvalidProp or error.isInvalidProp)
    }

    override fun generateResult() = CompositeStatistics.dataPerInterval(inputLine,
            intervals.text.toInt(), error.text.toDouble())
}
