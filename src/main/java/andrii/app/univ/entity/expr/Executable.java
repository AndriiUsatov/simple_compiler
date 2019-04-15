package andrii.app.univ.entity.expr;

import andrii.app.univ.entity.exe.ExecutionContext;
import andrii.app.univ.entity.lexema.Lexema;

import java.io.IOException;
import java.util.List;

public abstract class Executable {

    public abstract List<ExpressionConstructItem> getConstructItems();
    public abstract List<Lexema> execute(ExecutionContext context);
    public abstract Executable clone();
}
