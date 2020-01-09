import core.Line
import core.analysis.max
import core.input.PassFilters
import core.model.Combine
import infrastructure.DataSetTransforms
import infrastructure.ShowCase
import javafx.application.Platform
import kotlin.math.*
import kotlin.random.Random
import core.analysis.CompositeStatistics.dft

const val PI_DOUBLED = PI * 2.0

fun Line.rectify() = Line(ys.size) { abs(ys[it]) }

fun Line.rectifyPositives() = Line(ys.size) {
    when {
        ys[it] > 0.01 -> ys[it]
        else -> 0.0
    }
}

fun Line.frequencyShiftKeyingModulation(carrierFrequencyOn: Double,
                                        carrierFrequencyOff: Double,
                                        carrierAmplitude: Double,
                                        dt: Double = 0.001) = Line(size) {
    when {
        ys[it] > 0.01 -> carrierAmplitude * cos(PI_DOUBLED * carrierFrequencyOn * dt * it)
        else -> carrierAmplitude * cos(PI_DOUBLED * carrierFrequencyOff * dt * it)
    }
}

fun Line.amplitudeShiftKeyingModulation(carrierFrequency: Double,
                                        carrierAmplitude: Double,
                                        dt: Double = 0.01) = Line(size) {
    when {
        ys[it] > 0.01 -> carrierAmplitude * sin(carrierFrequency * dt * it)
        else -> 0.0
    }
}

fun Line.phaseShiftKeyingModulation(carrierFrequency: Double,
                                    carrierAmplitude: Double,
                                    phaseOn: Double,
                                    phaseOff: Double,
                                    dt: Double = 0.001) = Line(size) {
    when {
        ys[it] > 0.01 -> carrierAmplitude * cos(PI_DOUBLED * carrierFrequency * dt * it + phaseOn)
        else -> carrierAmplitude * cos(PI_DOUBLED * carrierFrequency * dt * it + phaseOff)
    }
}

// Frequency in kHZ
fun absorption(frequency: Double): Double {
    val square = frequency * frequency
    return 0.11 * square / (1.0 + square) + 44.0 * square / (4100.0 + square) +
            0.000275 * square + 0.003
}

fun attentuationFactor(distance: Double, frequency: Double, spreadingLoss: Double = 1.5) =
        distance.pow(spreadingLoss) * absorption(frequency).pow(distance)

fun noiseEnv(frequency: Double, shippingDensity: Double, windSpeed: Double): Double {
    val turbulenceNoise = 17.0 - 30.0 * ln(frequency)
    val shippingNoise = 40.0 + 20.0 * (shippingDensity - 0.5) + 26.0 * ln(frequency) -
            60.0 * ln(frequency + 0.03)
    val windNoise = 50.0 + 7.5 * sqrt(windSpeed) + 20.0 * ln(frequency) -
            40.0 * ln(frequency + 0.4)
    return 10.0.pow(turbulenceNoise / 10.0) +
            10.0.pow(shippingNoise / 10.0) +
            10.0.pow(windNoise / 10.0)
}

fun noisify(data: Line, distance: Double, frequency: Double): Line {
    val freqAsKhz = frequency / 1000.0
    val shippingDensity = 0.6
    val windSpeed = 5.0 // m/s
    val noiseAll = noiseEnv(freqAsKhz, shippingDensity, windSpeed)
    val rnd = Random(50)
    val attentuation = attentuationFactor(distance, frequency)
    println(attentuation)
    println(noiseAll)
    return Line(data.size) {
        data.ys[it] / attentuation + rnd.nextDouble(noiseAll)
    }
}

fun Line.amplitudeShiftKeyingDemodulation(dt: Double = 0.000001): Line {
    val kernelSize = 32
    val bpfApply = Combine.convolution(rectify(),
            PassFilters.lowPassFilter(kernelSize, dt, 2.0))
    val size = size
    val threshold = bpfApply.max() / 2.0
    return Line(size) {
        when {
            it + kernelSize > size -> 0.0
            bpfApply.ys[it + kernelSize] > threshold -> 1.0
            else -> 0.0
        }
    }
}

fun Line.frequencyShiftKeyingDemodulation(carrierFrequencyOn: Double,
                                          carrierFrequencyOff: Double,
                                          frequencyDelta: Double = 2.0,
                                          dt: Double = 0.000001): Line {
    val kernelSize = 32
    val smoothingThreshold = 50.0
    val filteredOn = Combine.convolution(Combine.convolution(this,
            PassFilters.bandPassFilter(kernelSize, dt, carrierFrequencyOn - frequencyDelta,
                    carrierFrequencyOn + frequencyDelta)).rectify(),
            PassFilters.lowPassFilter(kernelSize, dt, smoothingThreshold))

    val filteredOff = Combine.convolution(Combine.convolution(this,
            PassFilters.bandPassFilter(kernelSize, dt, carrierFrequencyOff - frequencyDelta,
                    carrierFrequencyOff + frequencyDelta)).rectify(),
            PassFilters.lowPassFilter(kernelSize, dt, smoothingThreshold))

    val multiplier = filteredOn.max() / filteredOff.max()
    val shift = kernelSize * 2 + 1
    val size = size
    return Line(size) {
        when {
            it + shift > size -> 0.0
            filteredOff.ys[it + shift] * multiplier < filteredOn.ys[it + shift] -> 1.0
            else -> 0.0
        }
    }
}

fun Line.phaseShiftKeyingDemodulation(carrierFrequency: Double,
                                      frequencyDelta: Double = 2.0,
                                      dt: Double = 0.000001): Line {

    val kernelSize = 32
    val filteredOn = Combine.convolution(
            Combine.convolution(this,
                    PassFilters.bandPassFilter(kernelSize, dt, carrierFrequency - frequencyDelta,
                            carrierFrequency + frequencyDelta)).rectify(),
            PassFilters.lowPassFilter(kernelSize, dt, 10.0))

    val size = size
    var delta = filteredOn.ys[0]
    var isOff = false
    var prepForChanges = false
    return Line(size) {
        when {
            it + kernelSize + 1 >= size -> 0.0
            else -> {
                val deltaNew = filteredOn.ys[it + kernelSize - 1] -
                        filteredOn.ys[it + kernelSize]
                println(deltaNew)
                if (!prepForChanges and (deltaNew > 0.0) and (delta < 0.0))
                    prepForChanges = true

                if (prepForChanges and (abs(deltaNew) > 10.0)) {
                    isOff = !isOff
                    prepForChanges = false
                }
                delta = deltaNew
//                if (filteredOn.ys[it + kernelSize] < 0.05) isOff = !isOff
                when {
                    isOff -> 0.0
                    else -> 1.0
                }
            }
        }
    }
}

private fun Line.fill(from: Int, to: Int, value: Double = 1.0) =
        (from until to).forEach { i -> ys[i] = value }

fun sosSignal(n: Int): Line {
    val line = Line(n)
    for (i in line.xs.indices)
        line.xs[i] = i.toDouble()
    val dashSize = (n / 12.5).toInt()
    val dotSize = dashSize / 2
    var cursor = 0
    line.fill(cursor, cursor + dashSize)
    cursor += dashSize + dotSize
    line.fill(cursor, cursor + dashSize)
    cursor += dashSize + dotSize
    line.fill(cursor, cursor + dashSize)
    cursor += dashSize + dotSize

    line.fill(cursor, cursor + dotSize)
    cursor += dotSize + dotSize
    line.fill(cursor, cursor + dotSize)
    cursor += dotSize + dotSize
    line.fill(cursor, cursor + dotSize)
    cursor += dotSize + dotSize

    line.fill(cursor, cursor + dashSize)
    cursor += dashSize + dotSize
    line.fill(cursor, cursor + dashSize)
    cursor += dashSize + dotSize
    line.fill(cursor, cursor + dashSize)
    cursor += dashSize + dotSize
    return line
}

fun modulationDemo(n: Int, distance: Double,
                   signalFrequency: Double,
                   modulate: (Line) -> Line,
                   demodulate: (Line) -> Line) {
    val rawData = sosSignal(n)
    val modulatedData = modulate(rawData)
    val receivedData = noisify(modulatedData, distance, signalFrequency)
//    val dftRemapped = receivedData //.dft()
//    val amplitudesFrequenciesPairs = (0 until dftRemapped.size)
//            .filter { dftRemapped.ys[it] >= 400.0 }
//            .map { Pair(dftRemapped.xs[it], dftRemapped.ys[it]) }
//    amplitudesFrequenciesPairs.forEach {
//        println("Frequency: ${it.first} \t Amplitude:${it.second}")
//    }
    val demodulatedData = demodulate(receivedData)
    Platform.startup {
        ShowCase.multi(
                DataSetTransforms.dataSetSingle("raw", rawData),
                DataSetTransforms.dataSetSingle("modulated", modulatedData),
                DataSetTransforms.dataSetSingle("demodulated", demodulatedData),
                DataSetTransforms.dataSetSingle("received", receivedData)
        ).show()
    }
}

fun main() {
    val n = 1000
    val carrierAmplitude = 1000.0
    val carrierFrequencyMain = 35.0
    val carrierFrequencySecondary = 60.0
    val data = Line(100) {
        noiseEnv(it.toDouble(),0.6, 5.0)
    }
    Platform.startup {
        ShowCase.single(
                DataSetTransforms.dataSetSingle("raw", data)
        ).show()
    }
//    val dataTransforms = listOf(
//            Pair({ line: Line ->
//                line.phaseShiftKeyingModulation(carrierFrequencyMain, carrierAmplitude, 0.0, PI)
//            }, { line: Line -> line.phaseShiftKeyingDemodulation(carrierFrequencyMain) }),
//            Pair({ line: Line -> line.amplitudeShiftKeyingModulation(carrierFrequencyMain, carrierAmplitude) },
//                    { line: Line -> line.amplitudeShiftKeyingDemodulation() }),
//            Pair({ line: Line ->
//                line.frequencyShiftKeyingModulation(carrierFrequencyMain,
//                        carrierFrequencySecondary, carrierAmplitude)
//            }, { line: Line -> line.frequencyShiftKeyingDemodulation(carrierFrequencyMain, carrierFrequencySecondary) })
//    )
//
//    val transform = dataTransforms[2]
//    modulationDemo(n, 1.0, carrierFrequencyMain, transform.first, transform.second)
}