package data.input;

import data.Line;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BinFileReader {
    public static Line ReadFromFile(String filename) {
        Path path = Paths.get(filename);
            //Files.readAllBytes(path);
        Line line = new Line(1);

        return  line;
        //InputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
    }
}
