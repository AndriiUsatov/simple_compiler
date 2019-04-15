package andrii.app.univ.entity.lexema;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Lexema {

    public static final Lexema PARTIAL = new Lexema();

    private Integer rowNumber;
    private String value;
    private LexemaClass lexemaClass;
    private Integer index;

    private Lexema() {
    }



    public Lexema(Integer rowNumber, String value, LexemaClass lexemaClass) {
        this.rowNumber = rowNumber;
        this.value = value;
        this.lexemaClass = lexemaClass;
    }

    public Lexema(Integer rowNumber, String value, LexemaClass lexemaClass, Integer index) {
        this.rowNumber = rowNumber;
        this.value = value;
        this.lexemaClass = lexemaClass;
        this.index = index;
    }

    public Lexema(String value, LexemaClass lexemaClass) {
        this.value = value;
        this.lexemaClass = lexemaClass;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setLexemaClass(LexemaClass lexemaClass) {
        this.lexemaClass = lexemaClass;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public String getValue() {
        return value;
    }

    public LexemaClass getLexemaClass() {
        return lexemaClass;
    }

    public Integer getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "Lexema{" +
                "rowNumber=" + rowNumber +
                ", value='" + value + '\'' +
                ", lexemaClass=" + lexemaClass +
                ", index=" + index +
                '}';
    }

    public Map<String, String> toMap() {
        Map<String, String> result = new LinkedHashMap<>();

        result.put("Номер рядка", String.valueOf(rowNumber));
        result.put("Підрядок", value);
        result.put("Код", String.valueOf(lexemaClass.getCode()));
        result.put("Індекс", index == null ? "" : String.valueOf(index));

        return result;
    }

}
