package gui.component.transformTabs

import core.analysis.CompositeStatistics
import gui.component.control.ParametersTab
import gui.component.control.TextFieldFormat
import gui.component.control.ValidatableTextField
import gui.component.or

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
