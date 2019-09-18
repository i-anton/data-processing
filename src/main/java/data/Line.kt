package data

import de.gsi.dataset.spi.DoubleDataSet

class Line private constructor(
        var xs: DoubleArray,
        var ys: DoubleArray
) {
    val size get() = xs.size
    constructor(other: Line) : this(other.xs.clone(), other.ys.clone())
    constructor(size: Int) : this(DoubleArray(size), DoubleArray(size))

    fun addToDataset(dataSet: DoubleDataSet) {
        dataSet.setAutoNotifaction(false)
        for (i in xs.indices) {
            dataSet.add(xs[i], ys[i])
        }
        dataSet.setAutoNotifaction(true)
    }
}
