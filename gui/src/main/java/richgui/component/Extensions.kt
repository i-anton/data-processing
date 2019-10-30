package richgui.component

import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
import javafx.beans.binding.BooleanExpression
import javafx.beans.value.ObservableBooleanValue

infix fun BooleanExpression.or(other: ObservableBooleanValue): BooleanBinding {
    return Bindings.or(this, other)
}