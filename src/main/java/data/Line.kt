package data

import de.gsi.dataset.spi.DoubleDataSet

class Line constructor(
        var xs: DoubleArray,
        var ys: DoubleArray
) {
    val size get() = xs.size

    constructor(other: Line) : this(other.xs.clone(), other.ys.clone())
    constructor(size: Int) : this(DoubleArray(size), DoubleArray(size))
    constructor(arrYs: DoubleArray) : this(DoubleArray(arrYs.size) { it.toDouble() }, arrYs)
    constructor(arrYs: FloatArray) :
            this(DoubleArray(arrYs.size) { it.toDouble() },
            DoubleArray(arrYs.size) { arrYs[it].toDouble() })

    fun addToDataSet(dataSet: DoubleDataSet) {
        dataSet.setAutoNotifaction(false)
        xs.indices.forEach { i -> dataSet.add(xs[i], ys[i]) }
        dataSet.setAutoNotifaction(true)
    }
}
