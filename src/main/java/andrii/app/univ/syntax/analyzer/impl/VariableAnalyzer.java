package andrii.app.univ.syntax.analyzer.impl;

import andrii.app.univ.entity.error.SpecificError;
import andrii.app.univ.entity.Variable;
import andrii.app.univ.entity.error.TranslateError;
import andrii.app.univ.entity.lexema.Lexema;
import andrii.app.univ.entity.lexema.LexemaClass;
import andrii.app.univ.syntax.analyzer.Analyzer;

import java.util.*;
import java.util.stream.Collectors;

public class VariableAnalyzer implements Analyzer {

    private List<LexemaClass> nextContextMapping = Arrays.asList(LexemaClass.OpenBracket, LexemaClass.OpenBlockBracket);
    private List<LexemaClass> previousContextMapping = Arrays.asList(LexemaClass.CloseBracket, LexemaClass.CloseBlockBracket);
    private List<LexemaClass> variableTypeList = Arrays.asList(LexemaClass.IntegerType, LexemaClass.RealType, LexemaClass.BooleanType);
    private List<NanoCheck> nanoCheckList = new ArrayList<>();

    public VariableAnalyzer() {
        initCheckList();
    }


    @Override
    public List<TranslateError> detectErrors(List<Lexema> lexemas) {
        List<TranslateError> result = new ArrayList<>();
        int contextNumber = 0;
        Map<Integer, List<Variable>> contextIdentifiersMap = new HashMap<>();
        contextIdentifiersMap.put(contextNumber, new ArrayList<>());


        for (int i = 0; i < lexemas.size(); i++) {
            Lexema lexema = lexemas.get(i);

            if (nextContextMapping.contains(lexema.getLexemaClass())) {
                contextNumber++;
                contextIdentifiersMap.put(contextNumber, new ArrayList<>(contextIdentifiersMap.get(contextNumber - 1)));
            }
            if (previousContextMapping.contains(lexema.getLexemaClass())) {
                contextNumber -= contextNumber == 0 ? 0 : 1;
            }
            if (lexema.getLexemaClass() == LexemaClass.Identifier) {
                boolean hasError = false;
                for (NanoCheck check : nanoCheckList) {
                    SpecificError error = check.check(contextIdentifiersMap.get(contextNumber), i, lexemas);
                    if (error != null) {
                        hasError = true;
                        result.add(error);
                        break;
                    }
                }

                if (!hasError && !contextContainsIdentifier(contextIdentifiersMap.get(contextNumber), lexema)
                        && variableTypeList.contains(lexemas.get(i - 1).getLexemaClass())) {
                    contextIdentifiersMap.get(contextNumber).add(new Variable(lexema, lexemas.get(i - 1).getLexemaClass()));
                }


            }
        }

        return result;
    }

    private Boolean contextContainsIdentifier(List<Variable> definedLexemaList, Lexema lex) {
        return definedLexemaList.stream()
                .map(variable -> variable.getVariableIdentifier().getValue())
                .collect(Collectors.toList())
                .contains(lex.getValue());
    }

    private void initCheckList() {
        nanoCheckList.add((currentContext, currentIdx, lexemas) -> {
            Lexema lexema = lexemas.get(currentIdx);
            if (contextContainsIdentifier(currentContext, lexema)) {
                if (variableTypeList.contains(lexemas.get(currentIdx - 1).getLexemaClass())) {
                    int idx = currentContext.stream().filter(variable -> variable.getVariableIdentifier().getValue().equals(lexema.getValue()))
                            .findFirst().get().getVariableIdentifier().getRowNumber();
                    return new SpecificError("Error in row " + lexema.getRowNumber() + ". Variable " + lexema.getValue() +
                            " is already defined in row " + idx);
                }
            }
            return null;
        });

        nanoCheckList.add((currentContext, currentIdx, lexemas) -> {
            Lexema lexema = lexemas.get(currentIdx);
            if (!contextContainsIdentifier(currentContext, lexema)) {
                if (!variableTypeList.contains(lexemas.get(currentIdx - 1).getLexemaClass())) {
                    return new SpecificError("Error in row " + lexema.getRowNumber() + ". Variable " + lexema.getValue() +
                            " is not defined.");
                }
            }
            return null;
        });

        nanoCheckList.add((currentContext, currentIdx, lexemas) -> {
            Lexema lexema = lexemas.get(currentIdx);

            if (variableTypeList.contains(lexemas.get(currentIdx - 1).getLexemaClass())) {

                if (lexemas.get(currentIdx + 1).getLexemaClass() == LexemaClass.EndLineDivider) {
                    return null;
                }

                if (lexemas.get(currentIdx + 1).getLexemaClass() != LexemaClass.Appropriation) {
                    return new SpecificError("Error in row " + lexema.getRowNumber() + ".\nExpected " + LexemaClass.Appropriation.getRepresentaion() +
                            ".\nFound " + lexemas.get(currentIdx + 1).getValue());
                }
                int endLineIdx = -1;
                for (int i = currentIdx + 2; i < lexemas.size(); i++) {
                    if (lexemas.get(i).getLexemaClass() == LexemaClass.EndLineDivider) {
                        endLineIdx = i;
                    }
                }
                if (endLineIdx == -1) {
                    return new SpecificError("Error in row " + lexema.getRowNumber() + ".\nExpected " + LexemaClass.EndLineDivider.getRepresentaion() +
                            ".not found. ");
                }

            }

            return null;
        });

        nanoCheckList.add((currentContext, currentIdx, lexemas) -> {

            Lexema lexema = lexemas.get(currentIdx);
            if (lexemas.get(currentIdx + 1).getLexemaClass() == LexemaClass.Appropriation) {
                int endLineIdx = -1;
                for (int i = currentIdx + 2; i < lexemas.size(); i++) {
                    if (lexemas.get(i).getLexemaClass() == LexemaClass.EndLineDivider) {
                        endLineIdx = i;
                    }
                }

                lexemas.subList(currentIdx + 2, endLineIdx);


            }

            return null;
        });
    }

    //Константы где ИД В СПИСКЕ() ИЛИ ПЕРЕМЕННЫЕ  где ТИП КЛАССА В СПИСКЕ
    private Boolean nextLexIsRight(List<Lexema> appropriateConstantList, List<Lexema> appropriateIdentifierList, Lexema lexema) {
        Set<LexemaClass> constantClasses = appropriateConstantList.stream().map(Lexema::getLexemaClass).collect(Collectors.toSet());
        Set<LexemaClass> identifierClasses = appropriateIdentifierList.stream().map(Lexema::getLexemaClass).collect(Collectors.toSet());
        if (constantClasses.contains(lexema.getLexemaClass())) {
            return appropriateConstantList.stream().anyMatch(lex -> lex.getIndex().equals(lexema.getIndex()));
        } else if (identifierClasses.contains(lexema.getLexemaClass())) {
            return appropriateIdentifierList.stream().anyMatch(lex -> lex.getIndex().equals(lexema.getIndex()));
        }

        return false;
    }

    @FunctionalInterface
    interface NanoCheck {

        SpecificError check(List<Variable> currentContext, int currentIdx, List<Lexema> lexemas);
    }

}
