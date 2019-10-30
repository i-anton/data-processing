package infrastructure

import core.Line
import de.gsi.dataset.spi.DoubleDataSet

object DataSetTransforms {
    fun dataSetSingle(name: String, line: Line) =
            listOf(DoubleDataSet(name).addLine(line))

    fun dataSetMulti(vararg param: Pair<String, Line>) =
            param.map { (name, line) ->
                DoubleDataSet(name).addLine(line)
            }

    private fun DoubleDataSet.addLine(line: Line): DoubleDataSet {
        this.setAutoNotifaction(false)
        line.xs.indices.forEach { i -> this.add(line.xs[i], line.ys[i]) }
        this.setAutoNotifaction(true)
        return this
    }
}
