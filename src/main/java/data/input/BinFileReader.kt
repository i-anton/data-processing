package data.input

import data.Line

import java.nio.file.Path
import java.nio.file.Paths

object BinFileReader {
    fun readFromFile(filename: String): Line {
        val path = Paths.get(filename)
        //Files.readAllBytes(path);

        return Line(1)
        //InputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
    }

    fun saveToFile(filename: String) {
        val path = Paths.get(filename)
        //Files.readAllBytes(path);
        //InputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
    }
}
