package andrii.app.univ.lexical.recognizer.impl;

import andrii.app.univ.entity.lexema.Lexema;
import andrii.app.univ.entity.lexema.LexemaClass;
import andrii.app.univ.lexical.recognizer.LexemaRecognizer;

public class StaticValueLexemaRecognizer extends LexemaRecognizer {

    private LexemaClass lexemaClass;

    public StaticValueLexemaRecognizer(LexemaClass lexemaClass) {
        this.lexemaClass = lexemaClass;
    }

    @Override
    public Lexema recognizeItem(String item) {
        return item.equals(lexemaClass.getRepresentaion()) ? new Lexema(lexemaClass.getRepresentaion(), lexemaClass)
                : (lexemaClass.getRepresentaion().startsWith(item) ? Lexema.PARTIAL : null);
    }
}
