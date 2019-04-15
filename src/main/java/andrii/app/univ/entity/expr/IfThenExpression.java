package andrii.app.univ.entity.expr;

import andrii.app.univ.entity.exe.ExecutionContext;
import andrii.app.univ.entity.lexema.Lexema;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static andrii.app.univ.util.ExpressionUtils.*;


public class IfThenExpression extends Executable {

    private Executable condition;
    private List<Executable> executeBody;

    public IfThenExpression(Executable condition, List<Executable> executeBody) {
        this.condition = condition;
        this.executeBody = executeBody;
    }

    @Override
    public List<ExpressionConstructItem> getConstructItems() {
        List<ExpressionConstructItem> items = new ArrayList<>(condition.getConstructItems());
        executeBody.forEach(executable -> items.addAll(executable.getConstructItems()));

        return items;
    }

    @Override
    public List<Lexema> execute(ExecutionContext context) {
        if (toBool(condition.execute(context).get(0), context)) {
            executeBody.forEach(executable -> executable.execute(context));
        }
        return new ArrayList<>();
    }

    @Override
    public Executable clone() {
        return new IfThenExpression(condition.clone(), executeBody.stream().map(Executable::clone).collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return "IfThenExpression{" +
                "condition=" + condition +
                ", executeBody=" + executeBody +
                '}';
    }
}

