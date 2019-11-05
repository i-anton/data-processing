package richgui.component.control

import javafx.scene.control.TextFormatter
import javafx.util.converter.NumberStringConverter
import java.text.NumberFormat

enum class TextFieldFormat(val formatter: TextFormatter<out Any>) {
    UINT(
            TextFormatter<Any> {
                if (it.controlNewText.matches("([1-9][0-9]*)?".toRegex())) it else null
            }
    ),
    DOUBLE(
            TextFormatter<Number>(
                    NumberStringConverter(
                            NumberFormat.getNumberInstance().apply {
                                isGroupingUsed = false
                            }
                    )
            )
    )
}