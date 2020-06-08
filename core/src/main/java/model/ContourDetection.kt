package core.model

import core.analysis.Convolution
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

object ContourDetection {
    val SobelX = arrayOf(
            doubleArrayOf(-1.0, 0.0, 1.0),
            doubleArrayOf(-2.0, 0.0, 2.0),
            doubleArrayOf(-1.0, 0.0, 1.0))

    val SobelY = arrayOf(
            doubleArrayOf(1.0, 2.0, 1.0),
            doubleArrayOf(0.0, 0.0, 0.0),
            doubleArrayOf(-1.0, -2.0, -1.0))

    val Laplacian2d = arrayOf(
            doubleArrayOf(0.0, -1.0, 0.0),
            doubleArrayOf(-1.0, 4.0, -1.0),
            doubleArrayOf(0.0, -1.0, 0.0))

    val Laplacian1D = doubleArrayOf(-1.0, 2.0, -1.0)
    val Derivative = doubleArrayOf(-1.0, 0.0, 1.0)

    private fun copyYtoArray(data: Array<DoubleArray>, buffer: DoubleArray, column: Int) {
        for (i in data.indices) buffer[i] = data[i][column]
    }

    private fun copyArrayToY(data: Array<DoubleArray>, buffer: DoubleArray, column: Int) {
        for (i in data.indices) data[i][column] = buffer[i]
    }

    fun applyX(data: Array<DoubleArray>, filter: DoubleArray) = Array(data.size) {
        Convolution.convolution(data[it], filter)
    }

    fun applyY(data: Array<DoubleArray>, filter: DoubleArray): Array<DoubleArray> {
        val buffer = DoubleArray(data.size)
        val result = Array(data.size) { DoubleArray(data[0].size) }
        result[0].indices.forEach { y ->
            copyYtoArray(data, buffer, y)
            val convolutionResult = Convolution.convolution(buffer, filter)
            copyArrayToY(result, convolutionResult, y)
        }
        return result
    }

    fun magnitude(first: Array<DoubleArray>, second: Array<DoubleArray>): Array<DoubleArray> {
        val sizeY = max(first.size, second.size)
        val sizeX = max(first[0].size, second[0].size)
        val result = Array(sizeY) { DoubleArray(sizeX) }
        val sizeMinY = min(first.size, second.size)
        val sizeMinX = min(first[0].size, second[0].size)
        for (y in 0 until sizeMinY) {
            for (x in 0 until sizeMinX) {
                result[y][x] = sqrt(first[y][x].pow(2) + second[y][x].pow(2))
            }
        }
        return result
    }
}