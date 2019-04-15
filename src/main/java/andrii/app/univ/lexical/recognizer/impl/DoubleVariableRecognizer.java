package andrii.app.univ.lexical.recognizer.impl;


import andrii.app.univ.entity.lexema.Lexema;
import andrii.app.univ.entity.lexema.LexemaClass;
import andrii.app.univ.lexical.recognizer.LexemaRecognizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoubleVariableRecognizer extends LexemaRecognizer {

    @Override
    public Lexema recognizeItem(String text) {
        Lexema result = null;

        Pattern pattern = Pattern.compile("^[\\d]+$");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String found = matcher.group().replaceAll("[ =;)+]", "");
            if (found.equals(text)) {
                result = Lexema.PARTIAL;
            }
        }

        pattern = Pattern.compile("^[\\d]+[.][\\d]*$");
        matcher = pattern.matcher(text);
        while (matcher.find()) {
            String found = matcher.group().replaceAll("[ =;)+]", "");
            if (found.equals(text)) {
                return new Lexema(text, LexemaClass.ConstantReal);
            }
        }

        return result;
    }

}
