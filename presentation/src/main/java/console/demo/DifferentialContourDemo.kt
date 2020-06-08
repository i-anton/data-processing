package console.demo

import core.analysis.Convolution.convolution
import core.input.ImageJPEG
import core.model.ContourDetection.Derivative
import core.model.ContourDetection.Laplacian1D
import core.model.ContourDetection.Laplacian2d
import core.model.ContourDetection.SobelX
import core.model.ContourDetection.SobelY
import core.model.ContourDetection.applyX
import core.model.ContourDetection.applyY
import core.model.ContourDetection.magnitude
import core.model.LimitTransform.binary
import core.model.Noise
import core.model.copyImg
import java.io.File
import javax.imageio.ImageIO

object DifferentialContourDemo {

    private fun derivativeApply(img: Array<DoubleArray>, resultPrefix: String,
                                filterX: (Array<DoubleArray>) -> Array<DoubleArray>,
                                filterY: (Array<DoubleArray>) -> Array<DoubleArray>) {
        val imgCopy = copyImg(img)
        binary(imgCopy, 230.0)
        ImageIO.write(ImageJPEG.toBufferedImage(imgCopy), "png", File("${resultPrefix}Bin.png"))

        val matrixY = filterY(imgCopy)
        ImageIO.write(ImageJPEG.toBufferedImage(matrixY, true), "png", File("${resultPrefix}Y.png"))

        val matrixX = filterX(imgCopy)
        ImageIO.write(ImageJPEG.toBufferedImage(matrixX, true), "png", File("${resultPrefix}X.png"))

        ImageIO.write(ImageJPEG.toBufferedImage(magnitude(matrixX, matrixY)), "png",
                File("${resultPrefix}Magnitude.png"))
    }

    private fun filterApply(img: Array<DoubleArray>, resultPrefix: String,
                            filter: (Array<DoubleArray>) -> Array<DoubleArray>) {
        val imgCopy = copyImg(img)
        binary(imgCopy, 230.0)
        val matrixF = filter(imgCopy)
        ImageIO.write(ImageJPEG.toBufferedImage(matrixF, true), "png",
                File("${resultPrefix}X.png"))
        ImageIO.write(ImageJPEG.toBufferedImage(matrixF), "png",
                File("${resultPrefix}Magnitude.png"))
    }

    // 125-150, 200-225, 226-250
    fun modelDerivativeDemo() {
        val imgPath = javaClass.getResource("MODEL.jpg").path
        val img = ImageJPEG.readFromFile(File(imgPath))
        val noised = copyImg(img)
        noised.forEach { Noise.gaussian(it, 0.15) }
        ImageIO.write(ImageJPEG.toBufferedImage(noised), "png", File("results/MODELNOISE.png"))

        derivativeApply(img, "results/D1_", { data -> applyX(data, Derivative) },
                { data -> applyY(data, Derivative) })
        derivativeApply(noised, "results/D1N_", { data -> applyX(data, Derivative) },
                { data -> applyY(data, Derivative) })

        derivativeApply(img, "results/L1_", { data -> applyX(data, Laplacian1D) },
                { data -> applyY(data, Laplacian1D) })
        derivativeApply(noised, "results/L1N_", { data -> applyX(data, Laplacian1D) },
                { data -> applyY(data, Laplacian1D) })

        derivativeApply(img, "results/S_", { data -> convolution(data, SobelX) },
                { data -> convolution(data, SobelY) })
        derivativeApply(noised, "results/SN_", { data -> convolution(data, SobelX) },
                { data -> convolution(data, SobelY) })
        filterApply(img, "results/L2_") { data -> convolution(data, Laplacian2d) }
        filterApply(noised, "results/L2N_") { data -> convolution(data, Laplacian2d) }
    }
}