package andrii.app.univ.semantic;

import andrii.app.univ.entity.error.TranslateError;
import andrii.app.univ.entity.error.SemanticError;
import andrii.app.univ.entity.error.SpecificError;
import andrii.app.univ.entity.lexema.Lexema;
import andrii.app.univ.entity.rule.Rule;
import andrii.app.univ.entity.rule.RuleContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SemanticAnalyzer {

    public List<TranslateError> analyze(List<Lexema> lexemas, Rule mainRule) {

        RuleContext ruleContext = new RuleContext(lexemas);
        List<TranslateError> result = new ArrayList<>();
        try {

//            System.out.println("1");
//            Thread.sleep(1000L);
//            Rule mainRule = sa.program();
//            Rule mainRule = sa.declarations();
//            System.out.println("2");
//            Thread.sleep(1000L);

            result.addAll(mainRule.isCorrect(ruleContext).errors());

//            System.out.println("3");
//            Thread.sleep(1000L);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<TranslateError> ret = new ArrayList<>();
        if (ruleContext.getIndex() + 1 < ruleContext.getLexemas().size()) {
            int idx = ruleContext.getErrors().keySet().stream().max(Integer::compareTo).get();
            result.addAll(ruleContext.getErrors().get(idx).stream().filter(scmpError -> scmpError instanceof SpecificError).distinct().collect(Collectors.toList()));
            List<SemanticError> serr = ruleContext.getErrors().get(idx).stream()
                    .filter(scmpError -> scmpError instanceof SemanticError).map(sErr -> (SemanticError) sErr).collect(Collectors.toList());
            ruleContext.getErrors().get(idx).stream().filter(scmpError -> scmpError instanceof SemanticError).forEach(scmpError -> System.out.println(scmpError.getText()));
            if (!serr.isEmpty()) {
                SemanticError sr = serr.get(0);
                for (int i = 1; i < serr.size(); i++) {
                    sr.addExpected(serr.get(i).getExpected());
                }
                result.add(sr);
            }
        }


        System.out.println(ruleContext.getIndex());
        System.out.println(ruleContext.getLexemas().size());

//        System.out.println(ruleContext.currentLexema());

        return result;
    }

    public List<TranslateError> analyze(List<Lexema> lexemas) {
      return analyze(lexemas, new SemanticAnalyzeRules().program());
    }

}
