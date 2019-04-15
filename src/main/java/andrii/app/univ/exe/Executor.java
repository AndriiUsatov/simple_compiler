package andrii.app.univ.exe;

import andrii.app.univ.entity.exe.ExecutionContext;
import andrii.app.univ.entity.expr.Executable;
import andrii.app.univ.entity.lexema.Lexema;

import java.util.List;

public class Executor {

    public static void exe(List<Executable> executables, List<Lexema> lexemas) {
        ExecutionContext context = new ExecutionContext(lexemas);
        executables.forEach(executable -> executable.execute(context));
    }
}
