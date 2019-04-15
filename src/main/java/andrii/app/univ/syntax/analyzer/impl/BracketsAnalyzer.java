package andrii.app.univ.syntax.analyzer.impl;

import andrii.app.univ.entity.error.SpecificError;
import andrii.app.univ.entity.error.TranslateError;
import andrii.app.univ.entity.lexema.Lexema;
import andrii.app.univ.entity.lexema.LexemaClass;
import andrii.app.univ.syntax.analyzer.Analyzer;

import java.util.*;

import static andrii.app.univ.entity.lexema.LexemaClass.*;

public class BracketsAnalyzer implements Analyzer {

    @Override
    public List<TranslateError> detectErrors(List<Lexema> lexemas) {

        List<LexemaClass> openBracketClasses = Arrays.asList(OpenBracket, OpenBlockBracket);
        List<LexemaClass> closeBracketClasses = Arrays.asList(CloseBracket, CloseBlockBracket);
        List<TranslateError> errors = new ArrayList<>();
        List<Integer> openBracketLexIdxList = new ArrayList<>();
        for (int i = 0; i < lexemas.size(); i++) {
            LexemaClass currentLexClass = lexemas.get(i).getLexemaClass();
            if (openBracketClasses.contains(currentLexClass) || closeBracketClasses.contains(currentLexClass)) {
                if (openBracketClasses.contains(currentLexClass)) {
                    openBracketLexIdxList.add(i);
                }

                if (closeBracketClasses.contains(currentLexClass)) {
                    LexemaClass lfClass = currentLexClass == CloseBracket ? OpenBracket : OpenBlockBracket;
                    int idx = -1;
                    for (int j = openBracketLexIdxList.size() - 1; j >= 0; j--) {
                        if (lexemas.get(openBracketLexIdxList.get(j)).getLexemaClass() == lfClass) {
                            idx = j;
                            break;
                        }
                    }
                    if (idx == -1) {
                        errors.add(openBracketNotFoundError(lexemas.get(i)));
                    } else {
                        openBracketLexIdxList.remove(idx);
                    }

                }
            }
        }

        for (Integer idx : openBracketLexIdxList) {
            errors.add(closeBracketNotFound(lexemas.get(idx)));
        }

        return errors;
    }

    private SpecificError openBracketNotFoundError(Lexema lexema) {
        Map<LexemaClass, LexemaClass> mapping = new HashMap<LexemaClass, LexemaClass>(){{
           put(CloseBracket, OpenBracket);
           put(CloseBlockBracket, OpenBlockBracket);
        }};

        return new SpecificError("Error in line " + lexema.getRowNumber() + ". Found ['" + lexema.getValue()
                + "'], but not found ['" + mapping.get(lexema.getLexemaClass()).getRepresentaion() + "']");
    }

    private SpecificError closeBracketNotFound(Lexema lexema) {
        Map<LexemaClass, LexemaClass> mapping = new HashMap<LexemaClass, LexemaClass>(){{
            put(OpenBracket, CloseBracket);
            put(OpenBlockBracket, CloseBlockBracket);
        }};

        return new SpecificError("Error in line " + lexema.getRowNumber() + ". Found ['" + lexema.getValue()
                + "'], but not found ['" + mapping.get(lexema.getLexemaClass()).getRepresentaion() + "']");
    }
}
