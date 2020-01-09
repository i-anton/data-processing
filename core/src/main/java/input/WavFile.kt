package core.input

import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import kotlin.experimental.and
import kotlin.math.roundToInt

private fun FileChannel.writeString(data: String) =
        this.write(ByteBuffer.wrap(data.toByteArray(Charsets.US_ASCII)))

private fun FileChannel.writeDWordLE(data: Int) =
        this.write(ByteBuffer.allocate(4)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putInt(data)
                .flip())

private fun FileChannel.writeWordLE(data: Short) =
        this.write(ByteBuffer.allocate(2)
                .order(ByteOrder.LITTLE_ENDIAN)
                .putShort(data)
                .flip())

data class WavMeta(val audioFormat: Short,
                   val numChannels: Short,
                   val sampleRate: Int,
                   val numSamples: Int,
                   val bytesPerSample: Int) {
    private fun chunkSize() = 44 + dataChunkSize()
    private fun dataChunkSize() = numSamples * numChannels * bytesPerSample
    private fun byteRate() = sampleRate * numChannels * bytesPerSample
    private fun blockAlign() = (numChannels * bytesPerSample).toShort()
    private fun bitsPerSample() = (bytesPerSample * 8).toShort()

    fun write(bu: FileChannel) {
        bu.writeString("RIFF")
        bu.writeDWordLE(chunkSize())
        bu.writeString("WAVE")

        bu.writeString("fmt ")
        bu.writeDWordLE(16) //For PCM
        bu.writeWordLE(audioFormat)
        bu.writeWordLE(numChannels)
        bu.writeDWordLE(sampleRate)
        bu.writeDWordLE(byteRate())
        bu.writeWordLE(blockAlign())
        bu.writeWordLE(bitsPerSample())

        bu.writeString("data")
        bu.writeDWordLE(dataChunkSize())
    }

}

object WavFile {
    private fun readMeta(bu: BufferedInputStream): WavMeta {
        val intBuffer = ByteArray(4)
        // header chunk
        val headerData = ByteArray(12) // "RIFF SIZE WAVE"
        // TODO: add validation of input format
        bu.read(headerData)
        // fmt chunk
        bu.skip(4) // skip "fmt "
        bu.read(intBuffer)

        val subChunkMetaSize = ByteBuffer.wrap(intBuffer).order(ByteOrder.LITTLE_ENDIAN).int
        val subChunkMetaBuffer = ByteArray(subChunkMetaSize)
        bu.read(subChunkMetaBuffer)
        val wavFormat = subChunkMetaBuffer[0] // if 1 then PCM
        if (wavFormat.toInt() != 1) throw UnsupportedOperationException("Only PCM supported")
        val numChannels = subChunkMetaBuffer[2]
        subChunkMetaBuffer.copyInto(intBuffer, 0, 4, 7)
        val sampleRate = ByteBuffer.wrap(intBuffer).order(ByteOrder.LITTLE_ENDIAN).int
        val bytesPerSample = subChunkMetaBuffer[14] / 8
        // data chunks
        bu.skip(4) // skip "data"
        bu.read(intBuffer)
        val dataSizeInBytes = ByteBuffer.wrap(intBuffer).order(ByteOrder.LITTLE_ENDIAN).int

        val numSamples = dataSizeInBytes / (numChannels * bytesPerSample)
        return WavMeta(wavFormat.toShort(), numChannels.toShort(), sampleRate, numSamples, bytesPerSample)
    }

    fun readFromFile(fileName: String): Pair<WavMeta, DoubleArray> {
        FileInputStream(fileName).buffered().use { bu ->
            val meta = readMeta(bu)
            val numSamples = meta.numSamples
            val dataLine = DoubleArray(numSamples)
            var cursor = 0
            if (meta.bytesPerSample == 2) {
                val valueBuffer = ByteArray(2)
                while (bu.read(valueBuffer) > 0 && cursor < numSamples) {
                    dataLine[cursor++] = (((valueBuffer[1] and 0xff.toByte()).toInt() shl 8) +
                            (valueBuffer[0] and 0xff.toByte()).toInt()).toDouble()
                }
            } else if (meta.bytesPerSample == 1) {
                val valueBuffer = ByteArray(1)
                while (bu.read(valueBuffer) > 0 && cursor < numSamples) {
                    dataLine[cursor++] = ((valueBuffer[0].toInt() and 0xff) - 125).toDouble()
                }
            }
            return Pair(meta, dataLine)
        }
    }

    fun writeToFile(fileName: String, meta: WavMeta, data: DoubleArray) {
        if (meta.audioFormat.toInt() != 1)
            throw UnsupportedOperationException("Only PCM supported")
        if (meta.numChannels.toInt() != 1)
            throw UnsupportedOperationException("Only single channel supported")
        FileOutputStream(fileName).channel.use { bu ->
            meta.write(bu)
            val numSamples = meta.numSamples
            val valueBuffer = ByteArray(2)
            for (cursor in 0 until numSamples) {
                bu.write(ByteBuffer.wrap(valueBuffer)
                        .order(ByteOrder.LITTLE_ENDIAN)
                        .putShort((data[cursor].roundToInt().toShort()))
                        .flip())
            }
        }
    }

}