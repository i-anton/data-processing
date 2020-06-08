package core.model

object LimitTransform {
    fun limit(data: DoubleArray, low: Double, high: Double) =
            data.forEachIndexed { index, value -> data[index] = if (value in low..high) value else 0.0 }

    fun binary(data: DoubleArray, low: Double) =
            data.forEachIndexed { index, value -> data[index] = if (value >= low) 255.0 else 0.0 }
    fun binary(data: Array<DoubleArray>, low: Double) = data.forEach { binary(it, low) }
}