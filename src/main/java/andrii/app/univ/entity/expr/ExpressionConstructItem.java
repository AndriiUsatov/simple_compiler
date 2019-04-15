package andrii.app.univ.entity.expr;

import andrii.app.univ.entity.lexema.Lexema;

import java.util.List;

public class ExpressionConstructItem {

    private Lexema enter;
    private List<Lexema> stack;
    private List<Lexema> reverseExpr;

    public ExpressionConstructItem(Lexema enter, List<Lexema> stack, List<Lexema> reverseExpr) {
        this.enter = enter;
        this.stack = stack;
        this.reverseExpr = reverseExpr;
    }

    public Lexema getEnter() {
        return enter;
    }

    public List<Lexema> getStack() {
        return stack;
    }

    public List<Lexema> getReverseExpr() {
        return reverseExpr;
    }

    @Override
    public String toString() {
        return "ExpressionConstructItem{" +
                "enter='" + enter.getValue() + "'" +
                ", stack='" + stack.stream().map(Lexema::getValue).reduce("", (s, s2) -> s + s2 + " ").trim() + "'" +
                ", reverseExpr='" + reverseExpr.stream().map(Lexema::getValue).reduce("", (s, s2) -> s + s2 + " ").trim() + "'" +
                '}';
    }
}
