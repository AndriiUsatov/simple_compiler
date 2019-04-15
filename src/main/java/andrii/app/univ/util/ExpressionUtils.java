package andrii.app.univ.util;

import andrii.app.univ.entity.Variable;
import andrii.app.univ.entity.exe.ExecutionContext;
import andrii.app.univ.entity.lexema.Lexema;

import java.util.Arrays;

import static andrii.app.univ.entity.lexema.LexemaClass.*;

public class ExpressionUtils {

    public static Boolean toBool(Lexema lex, ExecutionContext context) {
        if (Arrays.asList(True, False).contains(lex.getLexemaClass())) {
            return lex.getLexemaClass() == True;
        }
        Variable var = context.getVariableByIdx(lex.getIndex());
        if (lex.getLexemaClass() == Identifier && var != null && var.getVariableType() == BooleanType && var.getVariableValue() != null) {
            return var.getVariableValue().getLexemaClass() == True;
        }

        throw new RuntimeException("Unexpected lex class, expected: [True, False], found: " + lex.getLexemaClass());
    }
}
