package andrii.app.univ.util;

import andrii.app.univ.entity.Variable;
import andrii.app.univ.entity.exe.ExecutionContext;
import andrii.app.univ.entity.lexema.Lexema;
import andrii.app.univ.entity.lexema.LexemaClass;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static andrii.app.univ.entity.lexema.LexemaClass.*;

public class LexHelper {

    public static String identifierStringType(Lexema lexema, List<Lexema> lexemaList) {
        Map<LexemaClass, String> classToTypeMapping = new HashMap<LexemaClass, String>() {
            {
                put(IntegerType, "INTEGER");
                put(RealType, "REAL");
                put(BooleanType, "BOOLEAN");
            }
        };
        return classToTypeMapping.get(identifierType(lexema, lexemaList));
    }

    public static LexemaClass identifierType(Lexema lexema, List<Lexema> lexemaList) {
        for (int i = 1; i < lexemaList.size()/* && lexema.getIndex() != null*/; i++) {
            if (lexema.getIndex() != null && lexemaList.get(i).getLexemaClass() == Identifier && lexema.getIndex().equals(lexemaList.get(i).getIndex())) {
                return lexemaList.get(i - 1).getLexemaClass();
            }
        }
        return null;
    }

    public static void replaceIdentWithValue(List<Lexema> lexemas, Integer idx, ExecutionContext context) {
        if (lexemas.get(idx) != null && lexemas.get(idx).getLexemaClass() == Identifier) {
            Variable var = context.getVariableByIdx(lexemas.get(idx).getIndex());
            lexemas.set(idx, var.getVariableValue());
        }
    }
    //isSomething true && = 32 2 / 0 ==

    public static LexemaClass numActionResultClass(Lexema first, Lexema second) {
        if (Arrays.asList(ConstantInt, ConstantReal).containsAll(Arrays.asList(first.getLexemaClass(), second.getLexemaClass()))) {
            return first.getLexemaClass() == ConstantInt && second.getLexemaClass() == ConstantInt ? ConstantInt : ConstantReal;
        }

        throw new RuntimeException("Ooops");
    }
}
