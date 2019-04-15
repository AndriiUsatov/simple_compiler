package andrii.app.univ.entity.rule;

import andrii.app.univ.entity.error.TranslateError;
import andrii.app.univ.entity.error.SemanticError;
import java.util.ArrayList;
import java.util.List;

public class RuleResults {

    private Boolean result;
    private List<TranslateError> errors = new ArrayList<>();

    public RuleResults(Boolean result) {
        this.result = result;
    }

    public RuleResults(Boolean result, TranslateError err) {
        this.result = result;
        errors.add(err);
    }

    public Boolean result() {
        return result;
    }

    public List<TranslateError> errors() {
        return errors;
    }

    public void addError(SemanticError err) {
        errors.add(err);
    }

    public void addAllErrors(List<TranslateError> errors) {
        this.errors.addAll(errors);
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RuleResults{" +
                "result=" + result +
                ", errors=" + errors +
                '}';
    }
}
