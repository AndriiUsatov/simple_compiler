package andrii.app.univ.entity.exe.action;

import andrii.app.univ.entity.exe.ExecutionContext;
import andrii.app.univ.entity.lexema.Lexema;
import andrii.app.univ.entity.lexema.LexemaClass;
import andrii.app.univ.util.LexHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import static andrii.app.univ.entity.lexema.LexemaClass.*;

@FunctionalInterface
public interface Operation {

    void pull(List<Lexema> lexemas, Integer idx, ExecutionContext context);

    static Operation plus() {
        return (lexemas, idx, context) -> {
            int idxFirstParam = secondConst(lexemas, idx), idxSecondParam = firstConst(lexemas, idx);
            Stream.of(idxFirstParam, idxSecondParam).forEach(num -> LexHelper.replaceIdentWithValue(lexemas, num, context));
            LexemaClass resultCls = LexHelper.numActionResultClass(lexemas.get(idxFirstParam), lexemas.get(idxSecondParam));
            String result = resultCls == ConstantInt
                    ? String.valueOf(lexToInt(lexemas.get(idxFirstParam)) + lexToInt(lexemas.get(idxSecondParam)))
                    : String.valueOf(lexToDouble(lexemas.get(idxFirstParam)) + lexToDouble(lexemas.get(idxSecondParam)));
            Lexema lex = new Lexema(result, resultCls);
            lexemas.set(idxFirstParam, null);
            lexemas.set(idxSecondParam, lex);
            lexemas.set(idx, null);
        };
    }

    static Operation minus() {
        return (lexemas, idx, context) -> {
            int idxFirstParam = secondConst(lexemas, idx), idxSecondParam = firstConst(lexemas, idx);
            Stream.of(idxFirstParam, idxSecondParam).forEach(num -> LexHelper.replaceIdentWithValue(lexemas, num, context));
            LexemaClass resultCls = LexHelper.numActionResultClass(lexemas.get(idxFirstParam), lexemas.get(idxSecondParam));
            String result = resultCls == ConstantInt
                    ? String.valueOf(lexToInt(lexemas.get(idxFirstParam)) - lexToInt(lexemas.get(idxSecondParam)))
                    : String.valueOf(lexToDouble(lexemas.get(idxFirstParam)) - lexToDouble(lexemas.get(idxSecondParam)));
            Lexema lex = new Lexema(result, resultCls);
            lexemas.set(idxFirstParam, null);
            lexemas.set(idxSecondParam, lex);
            lexemas.set(idx, null);
        };
    }

    static Operation multiply() {
        return (lexemas, idx, context) -> {
            int idxFirstParam = secondConst(lexemas, idx), idxSecondParam = firstConst(lexemas, idx);
            Stream.of(idxFirstParam, idxSecondParam).forEach(num -> LexHelper.replaceIdentWithValue(lexemas, num, context));
            LexemaClass resultCls = LexHelper.numActionResultClass(lexemas.get(idxFirstParam), lexemas.get(idxSecondParam));
            String result = resultCls == ConstantInt
                    ? String.valueOf(lexToInt(lexemas.get(idxFirstParam)) * lexToInt(lexemas.get(idxSecondParam)))
                    : String.valueOf(lexToDouble(lexemas.get(idxFirstParam)) * lexToDouble(lexemas.get(idxSecondParam)));
            Lexema lex = new Lexema(result, resultCls);
            lexemas.set(idxFirstParam, null);
            lexemas.set(idxSecondParam, lex);
            lexemas.set(idx, null);
        };
    }

    static Operation divide() {
        return (lexemas, idx, context) -> {
            int idxFirstParam = secondConst(lexemas, idx), idxSecondParam = firstConst(lexemas, idx);
            Stream.of(idxFirstParam, idxSecondParam).forEach(num -> LexHelper.replaceIdentWithValue(lexemas, num, context));
            LexemaClass resultCls = LexHelper.numActionResultClass(lexemas.get(idxFirstParam), lexemas.get(idxSecondParam));
            String result = resultCls == ConstantInt
                    ? String.valueOf(lexToInt(lexemas.get(idxFirstParam)) / lexToInt(lexemas.get(idxSecondParam)))
                    : String.valueOf(lexToDouble(lexemas.get(idxFirstParam)) / lexToDouble(lexemas.get(idxSecondParam)));
            Lexema lex = new Lexema(result, resultCls);
            lexemas.set(idxFirstParam, null);
            lexemas.set(idxSecondParam, lex);
            lexemas.set(idx, null);
        };
    }

    static Operation equalsOp() {
        return (lexemas, idx, context) -> {
            int idxFirstParam = secondConst(lexemas, idx), idxSecondParam = firstConst(lexemas, idx);
            Stream.of(idxFirstParam, idxSecondParam).forEach(num -> LexHelper.replaceIdentWithValue(lexemas, num, context));
            boolean result;
            try {
                LexemaClass paramTypes = LexHelper.numActionResultClass(lexemas.get(idxFirstParam), lexemas.get(idxSecondParam));
                result = paramTypes == ConstantInt
                        ? lexToInt(lexemas.get(idxFirstParam)).intValue() == lexToInt(lexemas.get(idxSecondParam)).intValue()
                        : lexToDouble(lexemas.get(idxFirstParam)).doubleValue() == lexToDouble(lexemas.get(idxSecondParam)).doubleValue();
            } catch (RuntimeException e) {
                result = lexemas.get(idxFirstParam).getLexemaClass() == lexemas.get(idxSecondParam).getLexemaClass();
            }
            Lexema lex = new Lexema(String.valueOf(result), result ? True : False);
            lexemas.set(idxFirstParam, null);
            lexemas.set(idxSecondParam, lex);
            lexemas.set(idx, null);
        };
    }

    static Operation notEqualsOp() {
        return (lexemas, idx, context) -> {
            int idxFirstParam = secondConst(lexemas, idx), idxSecondParam = firstConst(lexemas, idx);
            Stream.of(idxFirstParam, idxSecondParam).forEach(num -> LexHelper.replaceIdentWithValue(lexemas, num, context));
            boolean result;
            try {
                LexemaClass paramTypes = LexHelper.numActionResultClass(lexemas.get(idxFirstParam), lexemas.get(idxSecondParam));
                result = paramTypes == ConstantInt
                        ? lexToInt(lexemas.get(idxFirstParam)).intValue() != lexToInt(lexemas.get(idxSecondParam)).intValue()
                        : lexToDouble(lexemas.get(idxFirstParam)).doubleValue() != lexToDouble(lexemas.get(idxSecondParam)).doubleValue();
            } catch (RuntimeException e) {
                result = lexemas.get(idxFirstParam).getLexemaClass() != lexemas.get(idxSecondParam).getLexemaClass();
            }
            Lexema lex = new Lexema(String.valueOf(result), result ? True : False);
            lexemas.set(idxFirstParam, null);
            lexemas.set(idxSecondParam, lex);
            lexemas.set(idx, null);
        };
    }

    static Operation appropriation() {
        return (lexemas, idx, context) -> {
            int idxFirstParam = secondConst(lexemas, idx), idxSecondParam = firstConst(lexemas, idx);
            LexHelper.replaceIdentWithValue(lexemas, idxSecondParam, context);
            context.getVariableByIdx(lexemas.get(idxFirstParam).getIndex())
                    .setVariableValue(new Lexema(lexemas.get(idxSecondParam).getValue(), lexemas.get(idxSecondParam).getLexemaClass()));
            Stream.of(idx, idxFirstParam, idxSecondParam).forEach(num -> lexemas.set(num, null));
        };
    }

    static Operation lessThen() {
        return (lexemas, idx, context) -> {
            int idxFirstParam = secondConst(lexemas, idx), idxSecondParam = firstConst(lexemas, idx);
            Stream.of(idxFirstParam, idxSecondParam).forEach(num -> LexHelper.replaceIdentWithValue(lexemas, num, context));
            LexemaClass paramTypes = LexHelper.numActionResultClass(lexemas.get(idxFirstParam), lexemas.get(idxSecondParam));
            boolean result = paramTypes == ConstantInt
                    ? lexToInt(lexemas.get(idxFirstParam)) < lexToInt(lexemas.get(idxSecondParam))
                    : lexToDouble(lexemas.get(idxFirstParam)) < lexToDouble(lexemas.get(idxSecondParam));
            Lexema lex = new Lexema(String.valueOf(result), result ? True : False);
            lexemas.set(idxFirstParam, null);
            lexemas.set(idxSecondParam, lex);
            lexemas.set(idx, null);
        };
    }

    static Operation moreThen() {
        return (lexemas, idx, context) -> {
            int idxFirstParam = secondConst(lexemas, idx), idxSecondParam = firstConst(lexemas, idx);
            Stream.of(idxFirstParam, idxSecondParam).forEach(num -> LexHelper.replaceIdentWithValue(lexemas, num, context));
            LexemaClass paramTypes = LexHelper.numActionResultClass(lexemas.get(idxFirstParam), lexemas.get(idxSecondParam));
            boolean result = paramTypes == ConstantInt
                    ? lexToInt(lexemas.get(idxFirstParam)) > lexToInt(lexemas.get(idxSecondParam))
                    : lexToDouble(lexemas.get(idxFirstParam)) > lexToDouble(lexemas.get(idxSecondParam));
            Lexema lex = new Lexema(String.valueOf(result), result ? True : False);
            lexemas.set(idxFirstParam, null);
            lexemas.set(idxSecondParam, lex);
            lexemas.set(idx, null);
        };
    }

    static Operation lessOrEqual() {
        return (lexemas, idx, context) -> {
            int idxFirstParam = secondConst(lexemas, idx), idxSecondParam = firstConst(lexemas, idx);
            Stream.of(idxFirstParam, idxSecondParam).forEach(num -> LexHelper.replaceIdentWithValue(lexemas, num, context));
            LexemaClass paramTypes = LexHelper.numActionResultClass(lexemas.get(idxFirstParam), lexemas.get(idxSecondParam));
            boolean result = paramTypes == ConstantInt
                    ? lexToInt(lexemas.get(idxFirstParam)) <= lexToInt(lexemas.get(idxSecondParam))
                    : lexToDouble(lexemas.get(idxFirstParam)) <= lexToDouble(lexemas.get(idxSecondParam));
            Lexema lex = new Lexema(String.valueOf(result), result ? True : False);
            lexemas.set(idxFirstParam, null);
            lexemas.set(idxSecondParam, lex);
            lexemas.set(idx, null);
        };
    }

    static Operation moreOrEqual() {
        return (lexemas, idx, context) -> {
            int idxFirstParam = secondConst(lexemas, idx), idxSecondParam = firstConst(lexemas, idx);
            Stream.of(idxFirstParam, idxSecondParam).forEach(num -> LexHelper.replaceIdentWithValue(lexemas, num, context));
            LexemaClass paramTypes = LexHelper.numActionResultClass(lexemas.get(idxFirstParam), lexemas.get(idxSecondParam));
            boolean result = paramTypes == ConstantInt
                    ? lexToInt(lexemas.get(idxFirstParam)) >= lexToInt(lexemas.get(idxSecondParam))
                    : lexToDouble(lexemas.get(idxFirstParam)) >= lexToDouble(lexemas.get(idxSecondParam));
            Lexema lex = new Lexema(String.valueOf(result), result ? True : False);
            lexemas.set(idxFirstParam, null);
            lexemas.set(idxSecondParam, lex);
            lexemas.set(idx, null);
        };
    }

    static Operation not() {
        return (lexemas, idx, context) -> {
            int param = firstConst(lexemas, idx);
            LexHelper.replaceIdentWithValue(lexemas, param, context);
            Lexema lex = new Lexema(String.valueOf(lexemas.get(param).getLexemaClass() == False), lexemas.get(param).getLexemaClass() == False ? True : False);
            lexemas.set(param, lex);
            lexemas.set(idx, null);
        };
    }

    static Operation logicalOr() {
        return (lexemas, idx, context) -> {
            int idxFirstParam = secondConst(lexemas, idx), idxSecondParam = firstConst(lexemas, idx);
            Stream.of(idxFirstParam, idxSecondParam).forEach(num -> LexHelper.replaceIdentWithValue(lexemas, num, context));
            boolean result = Stream.of(idxFirstParam, idxSecondParam).anyMatch(i -> lexemas.get(i).getLexemaClass() == True);
            Lexema lex = new Lexema(String.valueOf(result), result ? True : False);
            lexemas.set(idx, null);
            lexemas.set(idxFirstParam, null);
            lexemas.set(idxSecondParam, lex);
        };
    }

    static Operation logicalAnd() {
        return (lexemas, idx, context) -> {
            int idxFirstParam = secondConst(lexemas, idx), idxSecondParam = firstConst(lexemas, idx);
            Stream.of(idxFirstParam, idxSecondParam).forEach(num -> LexHelper.replaceIdentWithValue(lexemas, num, context));
            boolean result = Stream.of(idxFirstParam, idxSecondParam).allMatch(i -> lexemas.get(i).getLexemaClass() == True);
            Lexema lex = new Lexema(String.valueOf(result), result ? True : False);
            lexemas.set(idx, null);
            lexemas.set(idxFirstParam, null);
            lexemas.set(idxSecondParam, lex);
        };
    }

    static Operation negation() {
        return (lexemas, idx, context) -> {
            int param = firstConst(lexemas, idx);
            LexHelper.replaceIdentWithValue(lexemas, param, context);
            Lexema lexema = new Lexema(lexemas.get(param).getLexemaClass() == ConstantInt
                    ? String.valueOf(0 - lexToInt(lexemas.get(param)))
                    : String.valueOf(0 - lexToDouble(lexemas.get(param))), lexemas.get(param).getLexemaClass());

            lexemas.set(param, lexema);
            lexemas.set(idx, null);
        };
    }

    static Operation sysOut() {
        return (lexemas, idx, context) -> {
            int param = firstConst(lexemas, idx);
            LexHelper.replaceIdentWithValue(lexemas, param, context);
            System.out.println(lexemas.get(param).getValue());
            Stream.of(idx, param).forEach(i -> lexemas.set(i, null));
        };
    }

    static Operation sysInInteger() {
        return (lexemas, idx, context) -> {
            lexemas.set(idx, new Lexema(String.valueOf(new Scanner(System.in).nextInt()), ConstantInt));
        };
    }

    static Operation sysInReal() {
        return (lexemas, idx, context) -> {
            try {
                lexemas.set(idx, new Lexema(String.valueOf(Double.parseDouble(new BufferedReader(new InputStreamReader(System.in)).readLine())), ConstantReal));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    static Integer lexToInt(Lexema lex) {
        return Integer.parseInt(lex.getValue());
    }

    static Double lexToDouble(Lexema lex) {
        return Double.parseDouble(lex.getValue());
    }

    static Integer firstConst(List<Lexema> lexemas, Integer idxFor) {
        return constaBeforeCurrentNum(lexemas, idxFor, 1);
    }

    static Integer secondConst(List<Lexema> lexemas, Integer idxFor) {
        return constaBeforeCurrentNum(lexemas, idxFor, 2);
    }

    static Integer constaBeforeCurrentNum(List<Lexema> lexemas, Integer idxFor, Integer num) {
        int counter = 0;
        for (int i = idxFor - 1; i >= 0; i--) {
            if (lexemas.get(i) == null) {
                continue;
            }
            if (!LexemaClass.operators().contains(lexemas.get(i).getLexemaClass())) {
                if (++counter == num) {
                    return i;
                }
            }
        }
        System.out.println(lexemas.get(idxFor));
        throw new RuntimeException("Not found");
    }

}
