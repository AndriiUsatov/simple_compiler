package andrii.app.univ.entity.exe.action;


import andrii.app.univ.entity.lexema.Lexema;
import andrii.app.univ.entity.lexema.LexemaClass;

import java.util.HashMap;
import java.util.Map;

import static andrii.app.univ.entity.lexema.LexemaClass.*;

public class OperationHolder {

    private Map<LexemaClass, Operation> operations;
    private static OperationHolder defaultHolder;

    public OperationHolder(Map<LexemaClass, Operation> mapping) {
        operations = mapping;
    }

    public Operation perform(Lexema lexema) {
        return operations.get(lexema.getLexemaClass());
    }

    public static OperationHolder defaultHolder() {
        if (defaultHolder == null) {
            Map<LexemaClass, Operation> mapping = new HashMap<LexemaClass, Operation>() {
                {
                    put(Plus, Operation.plus());
                    put(Minus, Operation.minus());
                    put(Multiply, Operation.multiply());
                    put(Divide, Operation.divide());
                    put(Equals, Operation.equalsOp());
                    put(Appropriation, Operation.appropriation());
                    put(LessThen, Operation.lessThen());
                    put(MoreThen, Operation.moreThen());
                    put(LessOrEqual, Operation.lessOrEqual());
                    put(MoreOrEqual, Operation.moreOrEqual());
                    put(NotEquals, Operation.notEqualsOp());
                    put(Not, Operation.not());
                    put(LogicalOr, Operation.logicalOr());
                    put(LogicalAnd, Operation.logicalAnd());
                    put(NUMBER_NEGATION, Operation.negation());
                    put(SystemOut, Operation.sysOut());
                    put(SystemInInteger, Operation.sysInInteger());
                    put(SystemInReal, Operation.sysInReal());
                }
            };
            defaultHolder = new OperationHolder(mapping);
        }

        return defaultHolder;
    }
}
