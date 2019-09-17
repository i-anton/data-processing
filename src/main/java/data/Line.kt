package data

import de.gsi.dataset.spi.DoubleDataSet

class Line private constructor(
        private var xs: DoubleArray,
        private var ys: DoubleArray
) {

    val size get() = xs.size

    constructor(other: Line) : this(other.xs.clone(), other.ys.clone())

    constructor(size: Int) : this(DoubleArray(size), DoubleArray(size))

    fun getX(index: Int) = xs[index]

    fun getY(index: Int) = ys[index]

    fun setX(index: Int, value: Double) {
        xs[index] = value
    }

    fun setY(index: Int, value: Double) {
        ys[index] = value
    }

    fun addToDataset(dataSet: DoubleDataSet) {
        dataSet.setAutoNotifaction(false)
        for (i in xs.indices) {
            dataSet.add(xs[i], ys[i])
        }
        dataSet.setAutoNotifaction(true)
    }
}
