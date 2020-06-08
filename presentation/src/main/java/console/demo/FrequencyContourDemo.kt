package console.demo

import core.input.ImageJPEG
import core.input.PassFilters
import core.model.ContourDetection.applyX
import core.model.ContourDetection.applyY
import core.model.LimitTransform.binary
import core.model.Noise
import core.model.copyImg
import core.model.mul
import core.model.sum
import java.io.File
import javax.imageio.ImageIO

object FrequencyContourDemo {
    private val imgPath = javaClass.getResource("MODEL.jpg").path
    private fun lpfContourExtract(img: Array<DoubleArray>, prefix: String) {
        val imgCopy = copyImg(img)
        binary(imgCopy, 230.0)
        val freq =80.0
        val filteredX = applyX(imgCopy, PassFilters.lowPass(8, 0.001, freq))
        ImageIO.write(ImageJPEG.toBufferedImage(filteredX, true), "png", File("${prefix}_X.png"))
        val filteredY = applyY(imgCopy, PassFilters.lowPass(8, 0.001, freq))
        ImageIO.write(ImageJPEG.toBufferedImage(filteredY, true), "png", File("${prefix}_Y.png"))
        val filtered = mul(sum(filteredX, filteredY), 0.5)
        ImageIO.write(ImageJPEG.toBufferedImage(filtered , true), "png", File("${prefix}_FILTERED.png"))
        val res = sum(mul(filtered, -1.0), mul(imgCopy, 1.0))
        ImageIO.write(ImageJPEG.toBufferedImage(res , true), "png", File("${prefix}_DIFF.png"))
        binary(res, 80.0)
        ImageIO.write(ImageJPEG.toBufferedImage(res , true), "png", File("${prefix}_RESULT.png"))
    }
    private fun hpfContourExtract(img: Array<DoubleArray>, prefix: String) {
        val imgCopy = copyImg(img)
        binary(imgCopy, 230.0)
        val freq =80.0
        val filteredX = applyX(imgCopy, PassFilters.highPass(8, 0.001, freq))
        ImageIO.write(ImageJPEG.toBufferedImage(filteredX, true), "png", File("${prefix}_X.png"))
        val filteredY = applyY(imgCopy, PassFilters.highPass(8, 0.001, freq))
        ImageIO.write(ImageJPEG.toBufferedImage(filteredY, true), "png", File("${prefix}_Y.png"))
        val filtered = mul(sum(filteredX, filteredY), 0.5)
        ImageIO.write(ImageJPEG.toBufferedImage(filtered , true), "png", File("${prefix}_FILTERED.png"))
        binary(filtered, 80.0)
        ImageIO.write(ImageJPEG.toBufferedImage(filtered , true), "png", File("${prefix}_RESULT.png"))
    }

    fun checkLpfContourExtraction() {
        val img = ImageJPEG.readFromFile(File(imgPath))
        lpfContourExtract(img, "LC")
        hpfContourExtract(img, "HC")
        val noised = copyImg(img)
        noised.forEach { Noise.gaussian(it, 0.10) }
        ImageIO.write(ImageJPEG.toBufferedImage(noised), "png", File("N.png"))
        lpfContourExtract(noised, "LN")
        hpfContourExtract(noised, "HN")
    }
}