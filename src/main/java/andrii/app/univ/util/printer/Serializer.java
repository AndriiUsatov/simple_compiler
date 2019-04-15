package andrii.app.univ.util.printer;


import andrii.app.univ.entity.lexema.Lexema;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Serializer {

    private static void removeOnExists(String ... filePath) {
        File tmp = null;
        for (String path : filePath) {
            tmp = new File(path);

            if (tmp.exists()) {
                tmp.delete();
            }
        }
    }

    public static void serialize(Map toSaveMap) {
        removeOnExists("lex.txt", "err.txt");
        File lex = new File("lex.txt");
        File err = new File("err.txt");

        try {
            lex.createNewFile();
            err.createNewFile();

            Gson gson = new Gson();

            String lexJsonValues = gson.toJson(toSaveMap.get("lex"));
            String errJsonValues = gson.toJson(toSaveMap.get("err"));

            if (!((List)toSaveMap.get("lex")).isEmpty()) {
                try (FileOutputStream fos = new FileOutputStream(lex)) {
                    fos.write(lexJsonValues.getBytes());
                }
            }
            if (!((List)toSaveMap.get("err")).isEmpty()) {
                try (FileOutputStream fos = new FileOutputStream(err)) {
                    fos.write(errJsonValues.getBytes());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map deserialize() {
        Gson gson = new Gson();
        File lex = new File("lex.txt");
        File err = new File("err.txt");
        Map result = new HashMap();
        try {
            if (lex.exists()) {
                String fileContent = "";
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(lex)))) {
                    while (reader.ready()) {
                        fileContent += reader.readLine();
                    }
                }
                if (!fileContent.isEmpty()) {
                    result.put("lex" ,gson.fromJson(fileContent, new TypeToken<List<Lexema>>(){}.getType()));
                }

            }

            if (err.exists()) {
                String fileContent = "";
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(err)))) {
                    while (reader.ready()) {
                        fileContent += reader.readLine();
                    }
                }
                if (!fileContent.isEmpty()) {
                    result.put("err" ,gson.fromJson(fileContent, new TypeToken<List<String>>(){}.getType()));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static void main(String[] args) {
        Map res = deserialize();
        System.out.println(res.get("lex"));
        System.out.println(res.get("err"));
    }

}
