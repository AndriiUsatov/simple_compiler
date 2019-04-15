package andrii.app.univ.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SourceScrapper {

    public static String getSources(String[] args) throws IOException {

        if ("-s".equals(args[0])) {
            return args[1];
        }

        if ("-f".equals(args[0])) {

            File inputFile = new File(args[1]);
            if (inputFile.exists() && inputFile.getName().endsWith(".slng")) {
                return getSourceText(inputFile);
            }
            throw new RuntimeException("Incorrect file.");
        }

        throw new RuntimeException("Sources not found");
    }

    private static String getSourceText(File inputFile) throws IOException {
        InputStream stream = new FileInputStream(inputFile);
        byte[] array = new byte[stream.available()];
        stream.read(array);
        return new String(array);
    }
}

//-f C://Users//Andrii_Usatov//Desktop//source.slng
//-s "Integer i = 0; do { sysOut(i); i = i + 1; } while ( i < 100);"