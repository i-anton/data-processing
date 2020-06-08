package console.demo

import core.analysis.Fourier.dft
import core.analysis.Fourier.idftD
import core.input.BinFile
import core.input.ImageJPEG.toBufferedImage
import core.model.sum
import core.model.div
import core.model.mul
import java.io.File
import javax.imageio.ImageIO

object ImageDeconvolution {
    fun demoWithoutNoise() {
        val width = 259
        val height = 185
        val imgPath = javaClass.getResource("blur259x185L.dat").path
        val img = BinFile.readFloatsMatrix(imgPath, width, height)
        ImageIO.write(toBufferedImage(img), "png", File("blurL.png"))
        val kernelPath = javaClass.getResource("kernL64_f4.dat").path
        val kernel = BinFile.readFloatsArray(kernelPath, width)
        val kernelFt = dft(kernel)
//        Platform.startup {
//            ShowCase.multi(
//                    dataSetSingle("kernel", Line(kernel)),
//                    dataSetSingle("|fourier|", Line(toAmplitudes(kernelFt))),
//                    dataSetSingle("fourierRe", Line(kernelFt.first)),
//                    dataSetSingle("fourierIm", Line(kernelFt.second))
//            ).show()
//        }
//        Platform.startup {
//            ShowCase.single(
//                    dataSetSingle("auc", autoCorrelation(Line(img[90])))
//            ).show()
//        }
        // Ideal filter
        val result = Array(img.size) { y ->
            val rowFt = dft(img[y])
            idftD(div(rowFt, kernelFt))
        }
        ImageIO.write(toBufferedImage(result), "png", File("deblurL.png"))
    }

    fun demoWithNoise() {
        val width = 259
        val height = 185
        val imgPath = javaClass.getResource("blur259x185L_N.dat").path
        val img = BinFile.readFloatsMatrix(imgPath, width, height)
        ImageIO.write(toBufferedImage(img), "png", File("blurLN.png"))
//        val middle = Line(img[90])
//        Platform.startup {
//            ShowCase.multi(
//                    dataSetSingle("auc", autoCorrelation(middle)),
//                    dataSetSingle("dft", middle.dft()),
//                    dataSetSingle("data", middle),
//                    dataSetSingle("data", middle)
//            ).show()
//        }
        val kernelPath = javaClass.getResource("kernL64_f4.dat").path
        val kernel = BinFile.readFloatsArray(kernelPath, width)
        val alpha = 0.000016
        // Optimal filter
        val kernelFt = dft(kernel)
        val square = kernelFt.first.mul(kernelFt.first)
                .sum(kernelFt.second.mul(kernelFt.second))
                .sum(alpha)
        val filter = Pair(
                kernelFt.first.div(square),
                kernelFt.second.mul(-1.0)
                        .div(square)
        )
//        Platform.startup {
//            ShowCase.single(
//                    dataSetMulti(Pair("divRe", Line(divisor.first)), Pair("divIm", Line(divisor.second)))
//            ).show()
//        }
        val result = Array(img.size) { y ->
            val rowFt = dft(img[y])
            idftD(mul(rowFt, filter))
        }
        ImageIO.write(toBufferedImage(result), "png", File("deblurLN.png"))
    }
}