package andrii.app.univ.entity.rule;

import andrii.app.univ.entity.error.TranslateError;
import andrii.app.univ.entity.lexema.Lexema;
import java.util.*;

public class RuleContext {

    private List<Lexema> lexemas;
    private Integer index;
    private Map<Integer, List<TranslateError>> errors;

    public RuleContext(List<Lexema> lexemas, Integer index, Map<Integer, List<TranslateError>> errors) {
        this.lexemas = lexemas;
        this.index = index;
        this.errors = errors;
    }

    public RuleContext(List<Lexema> lexemas) {
        this(lexemas, 0, new HashMap<>());
    }

    public Lexema currentLexema() {
        return lexemas.get(index);
    }

    public List<Lexema> getLexemas() {
        return lexemas;
    }

    public Integer getIndex() {
        return index;
    }

    public Map<Integer, List<TranslateError>> getErrors() {
        return errors;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void addError(TranslateError error, Integer idx) {
        errors.computeIfAbsent(idx, k -> new ArrayList<>()).add(error);
    }

    public void addErrors(Collection<TranslateError> errors, Integer idx) {
        errors.forEach(error -> addError(error, idx));
    }

    public RuleContext clone() {
        return new RuleContext(lexemas, index.intValue(), errors);
    }

    public void iterateIdx() {
        index++;
    }
}
