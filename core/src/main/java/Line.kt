package core

class Line constructor(
        var xs: DoubleArray,
        var ys: DoubleArray
) {
    val size get() = xs.size

    constructor(other: Line) : this(other.xs.copyOf(), other.ys.copyOf())

    constructor(size: Int, arrXGen: (Int) -> Double, arrY: DoubleArray) :
            this(DoubleArray(size, arrXGen), arrY.copyOf(size))

    constructor(size: Int) : this(DoubleArray(size), DoubleArray(size))
    constructor(arrYs: DoubleArray) : this(DoubleArray(arrYs.size) { it.toDouble() }, arrYs)

    constructor(size: Int, arrYGen: (Int) -> Double) :
            this(DoubleArray(size) { it.toDouble() }, DoubleArray(size, arrYGen))

    constructor(arrXs: DoubleArray, arrYGen: (Int) -> Double) :
            this(arrXs.copyOf(), DoubleArray(arrXs.size, arrYGen))

    constructor(arrYs: FloatArray) :
            this(DoubleArray(arrYs.size) { it.toDouble() },
                    DoubleArray(arrYs.size) { arrYs[it].toDouble() })
}
