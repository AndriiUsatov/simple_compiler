package andrii.app.univ.entity.error;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SemanticError implements TranslateError {

    private String found;
    private Integer row;
    private List<String> expected = new ArrayList<>();

    public SemanticError(String found, Integer row, String... expected) {
        this.found = found;
        this.row = row;
        this.expected.addAll(Arrays.asList(expected));
    }

    public SemanticError(String found, Integer row, List<String> expected) {
        this.found = found;
        this.row = row;
        this.expected.addAll(expected);
    }

    public List<String> getExpected() {
        return expected;
    }

    public String getFound() {
        return found;
    }

    public Integer getRow() {
        return row;
    }

    public void addExpected(List<String> expected) {
        this.expected.addAll(expected);
    }

    public void addExpected(String ... expected) {
        this.expected.addAll(Arrays.asList(expected));
    }

    public String getText() {
        return "Expected: " + expected.stream().distinct().map(s -> "'" + s + "'").collect(Collectors.toList()) + " in row number " + row + ", Found: ['" + found + "']";
    }

    @Override
    public String toString() {
        return "Expected: " + expected.stream().distinct().map(s -> "'" + s + "'").collect(Collectors.toList()) + " in row number " + row + ", Found: ['" + found + "']";
    }
}
