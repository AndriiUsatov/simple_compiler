package andrii.app.univ.entity.expr;

import andrii.app.univ.entity.exe.ExecutionContext;
import andrii.app.univ.entity.exe.action.OperationHolder;
import andrii.app.univ.entity.lexema.Lexema;
import andrii.app.univ.entity.lexema.LexemaClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Expression extends Executable {

    private static OperationHolder operationHolder = OperationHolder.defaultHolder();
    private List<ExpressionConstructItem> constructItems = new ArrayList<>();
    private List<Lexema> expressionElems;
    private Integer from;
    private Integer to;

    public Expression() {
        expressionElems = new ArrayList<>();
    }

    public Expression(List<ExpressionConstructItem> constructItems, List<Lexema> expressionElems) {
        this.constructItems = constructItems;
        this.expressionElems = expressionElems;
    }

    public Expression(List<Lexema> expressionElems, Integer from, Integer to) {
        this.expressionElems = expressionElems;
        this.from = from;
        this.to = to;
    }

    public List<Lexema> getExpressionElems() {
        return expressionElems;
    }

    public Integer getFrom() {
        return expressionElems.stream().map(Lexema::getIndex).filter(Objects::nonNull).min(Integer::compareTo).get();
    }

    public Integer getTo() {
        return expressionElems.stream().map(Lexema::getIndex).filter(Objects::nonNull).max(Integer::compareTo).get();
    }

    public void setExpressionElems(List<Lexema> expressionElems) {
        this.expressionElems = expressionElems;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public void addLexemas(Lexema... lexemas) {
        expressionElems.addAll(Arrays.asList(lexemas));
    }

    public void addConstructItem(ExpressionConstructItem itm) {
        constructItems.add(itm);
    }

    public List<ExpressionConstructItem> getConstructItems() {
        return constructItems;
    }

    @Override
    public List<Lexema> execute(ExecutionContext context)  {
        List<Lexema> toExe = constructItems.get(constructItems.size() - 1).getReverseExpr()
                .stream()
                .filter(lexema -> !Arrays.asList(LexemaClass.OpenBracket, LexemaClass.CloseBracket).contains(lexema.getLexemaClass()))
                .collect(Collectors.toList());
//        System.out.println(toExe.stream().map(Lexema::getValue).reduce("", (s, s2) -> s + " " + s2).trim());
        for (int i = 0; i < toExe.size(); i++) {
            if (toExe.get(i) == null) {
                continue;
            }
            if (LexemaClass.operators().contains(toExe.get(i).getLexemaClass())) {
                operationHolder.perform(toExe.get(i)).pull(toExe, i, context);
            }
        }

        return toExe.stream().filter(lexema -> lexema != null).collect(Collectors.toList());
    }

    @Override
    public Executable clone() {
        return new Expression(new ArrayList<>(constructItems), new ArrayList<>(expressionElems));
    }

    @Override
    public String toString() {
        return "Expression{" +
                "constructItems=" + constructItems +
                ", expressionElems=" + expressionElems +
                '}';
    }

}
