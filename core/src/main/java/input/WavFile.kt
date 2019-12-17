package core.input

import java.io.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.experimental.and

data class WavMeta(val chunkSize: Int,
                   val wavFormat: Byte,
                   val numChannels: Byte,
                   val sampleRate: Int,
                   val byteRate: Int,
                   val numSamples: Int,
                   val bytesPerSample: Int)

object WavFile {
    private fun readMeta(bu: BufferedInputStream): WavMeta {
        val intBuffer = ByteArray(4)
        // header chunk
        val headerData = ByteArray(12) // "RIFFSIZEWAVE"
        // TODO: add validation of input format
        bu.read(headerData)
        headerData.copyInto(intBuffer, 0, 4, 7)
        val chunkSize = ByteBuffer.wrap(intBuffer).order(ByteOrder.LITTLE_ENDIAN).int
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
        subChunkMetaBuffer.copyInto(intBuffer, 0, 8, 11)
        val byteRate = ByteBuffer.wrap(intBuffer).order(ByteOrder.LITTLE_ENDIAN).int

        val blockAlign = subChunkMetaBuffer[12]
        val bytesPerSample = subChunkMetaBuffer[14] / 8

        // data chunks
        bu.skip(4) // skip "data"
        bu.read(intBuffer)
        val dataSizeInBytes = ByteBuffer.wrap(intBuffer).order(ByteOrder.LITTLE_ENDIAN).int

        val numSamples = dataSizeInBytes / (numChannels * bytesPerSample)
        return WavMeta(chunkSize, wavFormat, numChannels, sampleRate, byteRate, numSamples, bytesPerSample)
    }

    fun readFromFile(fileName: String): Pair<WavMeta, FloatArray> {
        FileInputStream(fileName).buffered().use { bu ->
            val meta = readMeta(bu)
            val numSamples = meta.numSamples
            val dataLine = FloatArray(numSamples)
            var cursor = 0
            if (meta.bytesPerSample == 2) {
                val valueBuffer = ByteArray(2)
                while (bu.read(valueBuffer) > 0 && cursor < numSamples) {
                    dataLine[cursor++] = (((valueBuffer[1] and 0xff.toByte()).toInt() shl 8) +
                            (valueBuffer[0] and 0xff.toByte()).toInt()).toFloat()
                }
            } else if (meta.bytesPerSample == 1) {
                val valueBuffer = ByteArray(1)
                while (bu.read(valueBuffer) > 0 && cursor < numSamples) {
                    dataLine[cursor++] = ((valueBuffer[0].toInt() and 0xff) - 125).toFloat()
                }
            }
            return Pair(meta, dataLine)
        }
    }


}