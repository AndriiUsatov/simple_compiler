package andrii.app.univ.lexical.recognizer.impl;

import andrii.app.univ.entity.lexema.Lexema;
import andrii.app.univ.entity.lexema.LexemaClass;
import andrii.app.univ.lexical.recognizer.LexemaRecognizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdentifierRecognizer extends LexemaRecognizer {

    @Override
    public Lexema recognizeItem(String text) {
        Pattern pattern = Pattern.compile("^[$_a-zA-Z]{1}[$_\\w]*$");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String found = matcher.group().replaceAll("[ =;)+]", "");
            if (found.equals(text)) {
                return new Lexema(text, LexemaClass.Identifier);
            }
        }
        return null;
    }

}
