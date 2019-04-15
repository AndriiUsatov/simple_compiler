package andrii.app.univ.lexical;

import andrii.app.univ.entity.lexema.Lexema;
import andrii.app.univ.entity.lexema.LexemaClass;
import andrii.app.univ.exception.ParseException;
import andrii.app.univ.lexical.recognizer.LexemaRecognizer;

import java.util.*;
import java.util.stream.Collectors;

public class LexicalAnalyzer {

    private List<LexemaRecognizer> recognizers;

    public LexicalAnalyzer(List<LexemaRecognizer> recognizers) {
        this.recognizers = recognizers;
    }

    public List<Lexema> recognizeText(String text) {

        List<Lexema> result = new ArrayList<>();
        List<LexemaRecognizer> tempRecognizers = new ArrayList<>();
        List<Lexema> lastLexemasList = new ArrayList<>();
        List<Lexema> tempLexemaList = new ArrayList<>();

        int recognizeFrom = 0;
        int recognizeTo = 0;
        List<LexemaRecognizer> nextRecognizers = new ArrayList<>(recognizers);

        while (recognizeFrom < text.length()) {
            recognizeTo += 1;

            if ((int) text.charAt(recognizeFrom) < 33) {
                recognizeFrom += 1;
                continue;
            }
            try {
                for (LexemaRecognizer recognizer : nextRecognizers) {
                    Lexema recognizeResult = recognizer.recognizeItem(text.substring(recognizeFrom, recognizeTo));
                    if (recognizeResult != null) {
                        tempRecognizers.add(recognizer);
                        if (recognizeResult != Lexema.PARTIAL) {
                            tempLexemaList.add(recognizeResult);
                        }
                    }
                }
            } catch (Exception e) { }
                if (!tempRecognizers.isEmpty()) {
                    lastLexemasList = tempLexemaList;
                    tempLexemaList = new ArrayList<>();
                    nextRecognizers = tempRecognizers;
                    tempRecognizers = new ArrayList<>();
                } else {
                    int tempRecognizeFrom = recognizeFrom;
                    int idx = rowNumber(text, recognizeFrom);
                    recognizeFrom = recognizeTo - 1;
                    recognizeTo = recognizeTo - 1;
                    nextRecognizers = new ArrayList<>(recognizers);
                    try {
                        result.add(lastLexemasList.stream().sorted(Comparator.comparingInt(o -> o.getLexemaClass().getCode())).findFirst().get());
                    } catch (Exception e) {
                        throw new ParseException("Cannot parse text '" + text.substring(tempRecognizeFrom) + "' in line " + idx, e);
                    }
                    result.get(result.size() - 1).setRowNumber(idx);
                    lastLexemasList = new ArrayList<>();
                }

        }
        setIndexes(result, LexemaClass.ConstantInt, LexemaClass.ConstantReal, LexemaClass.True, LexemaClass.False);
        setIndexes(result, LexemaClass.Identifier);
        return result;
    }

    private void setIndexes(List<Lexema> lexemas, LexemaClass ... classes) {
        int nextIdx = 1;
        Map<String, Integer> idxValueMap = new HashMap<>();
        List<Lexema> lexemasToIndex = lexemas.stream()
                .filter(lex -> Arrays.asList(classes).contains(lex.getLexemaClass()))
                .collect(Collectors.toList());
        for (Lexema lex : lexemasToIndex) {
            lex.setIndex(idxValueMap.get(lex.getValue()));
            if (lex.getIndex() == null) {
                idxValueMap.put(lex.getValue(), nextIdx);
                lex.setIndex(nextIdx++);
            }
        }
    }

    private int rowNumber(String text, int charIndex) {
        String[] splittedText = text.split("\n");
        int idx = 0;
        String temp = splittedText[idx] + '\n';
        if (temp.length() - 1 < charIndex) {
            for (int i = 1; i < splittedText.length; i++) {
                idx = i;
                temp += splittedText[idx] + '\n';
                if (temp.length() - 1 >= charIndex) {
                    break;
                }
            }
        }

        return idx + 1;
    }
}
