package andrii.app.univ.syntax;

import andrii.app.univ.entity.error.SpecificError;
import andrii.app.univ.entity.error.TranslateError;
import andrii.app.univ.entity.lexema.Lexema;
import andrii.app.univ.entity.lexema.LexemaClass;
import andrii.app.univ.entity.lexema.LexemaRouteMapping;
import andrii.app.univ.syntax.analyzer.Analyzer;
import andrii.app.univ.syntax.analyzer.impl.BracketsAnalyzer;
import andrii.app.univ.syntax.analyzer.impl.VariableAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SyntaxAnalyzer {

    private List<Analyzer> analyzers = new ArrayList<>();

    public SyntaxAnalyzer() {
        init();
    }

    public List<TranslateError> detectErrors(List<Lexema> lexemas) {
        List<TranslateError> result = new ArrayList<>();


        LexemaRouteMapping routeMapping = LexemaRouteMapping.getDefaultMapping();
        List<LexemaClass> nextLexemaClasses = Arrays.asList(lexemas.get(0).getLexemaClass());
        Lexema previousLexema = null;
        for (Lexema lexema : lexemas) {
            if (!nextLexemaClasses.contains(lexema.getLexemaClass())) {
                try {
                    throw new IllegalArgumentException("Unexpected lexema in row number " + previousLexema.getRowNumber() +
                            " after: ['" + previousLexema.getValue() + "'].\nExpected: " +
                            nextLexemaClasses.stream()
                                    .map(lexCls -> "'" + (lexCls.getRepresentaion() == null ? lexCls.toString()
                                            : lexCls.getRepresentaion()) + "'")
                                    .collect(Collectors.toList())
                            + ",\nFound: ['" + lexema.getValue() + "']");
                } catch (Exception e) {
                    e.printStackTrace();
                    result.add(new SpecificError(e.getMessage()));
                }
            }
            nextLexemaClasses = routeMapping.getRouteMap().get(lexema.getLexemaClass());
            previousLexema = lexema;
        }

        for (Analyzer analyzer : analyzers) {
            result.addAll(analyzer.detectErrors(lexemas));
        }

        return result;
    }

    private void init() {
        analyzers.add(new BracketsAnalyzer());
        analyzers.add(new VariableAnalyzer());
    }
}
