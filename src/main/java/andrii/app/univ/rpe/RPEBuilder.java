package andrii.app.univ.rpe;

import andrii.app.univ.entity.expr.*;
import andrii.app.univ.entity.lexema.LexPriorities;
import andrii.app.univ.entity.lexema.Lexema;
import andrii.app.univ.entity.lexema.LexemaClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static andrii.app.univ.entity.lexema.LexemaClass.*;

public class RPEBuilder {

    private LexPriorities priorities = LexPriorities.getDefaultPrioritiesMapping();

    public List<Executable> toReverseExp(List<Lexema> lexemas) {
        List<Executable> result = new ArrayList<>();

        for (int i = 0; i < lexemas.size(); i++) {
            Expression expression = new Expression();
            LinkedList<Lexema> stack = new LinkedList<>();
            boolean isRegularExpr = true;

            if (isExpressionPart(lexemas.get(i))) {
                while (i < lexemas.size() && isExpressionPart(lexemas.get(i))) {
                    expression.addConstructItem(new ExpressionConstructItem(lexemas.get(i), new ArrayList<>(stack),
                            new ArrayList<>(expression.getExpressionElems().stream().filter(lex -> !Arrays.asList(OpenBracket, CloseBracket).contains(lex.getLexemaClass())).collect(Collectors.toList()))));
                    if (isConstant(lexemas.get(i))) {
                        expression.addLexemas(lexemas.get(i++));
                        continue;
                    }

                    if (lexemas.get(i).getLexemaClass() == If) {
                        int idx = ifThenExpressionEndIdx(lexemas, i) + 1;
                        List<Executable> expressions = toReverseExp(lexemas.subList(i + 1, idx));
                        result.add(new IfThenExpression(expressions.get(0), new ArrayList<>(expressions.subList(1, expressions.size()))));
                        i = idx - 1;
                        isRegularExpr = false;
                        break;
                    }

                    if (lexemas.get(i).getLexemaClass() == DoWhileLoopStart) {
                        int idx = doWhileExpressionEndIdx(lexemas, i) + 1;
                        List<Executable> expressions = toReverseExp(lexemas.subList(i + 1, idx));
                        result.add(new DoWhileExpression(expressions.get(expressions.size() - 1), new ArrayList<>(expressions.subList(0, expressions.size() - 1))));
                        i = idx - 1;
                        isRegularExpr = false;
                        break;

                    }

                    int prior = priorities.priority(lexemas.get(i).getLexemaClass());
                    while (!stack.isEmpty() && priorities.priority(stack.getFirst().getLexemaClass()) >= prior && stack.getFirst().getLexemaClass() != OpenBracket && lexemas.get(i).getLexemaClass() != OpenBracket) {
                        Lexema fromStack = stack.poll();
                        if (!Arrays.asList(OpenBracket, CloseBracket).contains(fromStack.getLexemaClass())) {
                            expression.addLexemas(fromStack);
                        }
                    }
                    stack.addFirst(lexemas.get(i++));
                }
                while (!stack.isEmpty()) {
                    expression.addLexemas(stack.poll());
                }
                if (isRegularExpr) {
                    expression.addConstructItem(new ExpressionConstructItem(new Lexema("", null), new ArrayList<>(),
                            new ArrayList<>(expression.getExpressionElems().stream().filter(lex -> !Arrays.asList(OpenBracket, CloseBracket).contains(lex.getLexemaClass())).collect(Collectors.toList()))));
                    result.add(expression);
                }
            }
        }

        return result;
    }

    public static List<Lexema> normalizedLexemas(List<Lexema> lexemas) {
        LinkedList<Lexema> result = new LinkedList<>();
        for (int i = 0; i < lexemas.size(); i++) {
            Lexema lex = lexemas.get(i);
            if (Arrays.asList(Minus, Plus).contains(lexemas.get(i).getLexemaClass())) {
                lex = new Lexema("+", Plus);
                while (Arrays.asList(Minus, Plus).contains(lexemas.get(i).getLexemaClass())) {
                    lex = multiplyLex(lex, lexemas.get(i++));
                }
                i--;
            }
            if (result.isEmpty()) {
                result.add(lex);
            } else if (Arrays.asList(Plus, Minus, Multiply, Divide, OpenBracket, Equals, Appropriation, LessOrEqual, LessThen, MoreOrEqual, MoreThen, NotEquals, LogicalOr, LogicalAnd)
                    .contains(result.getLast().getLexemaClass())) {
                if (lex.getLexemaClass() == Minus) {
                    lex.setValue("@");
                    lex.setLexemaClass(NUMBER_NEGATION);
                }
                if (lex.getLexemaClass() != Plus) {
                    result.add(lex);
                }
            } else {
                result.add(lex);
            }
        }
        return result;
    }

    private static Lexema multiplyLex(Lexema l1, Lexema l2) {
        if (Arrays.asList(Minus, Plus).containsAll(Arrays.asList(l1.getLexemaClass(), l2.getLexemaClass()))) {
            return Arrays.asList(l1.getLexemaClass(), l2.getLexemaClass()).contains(Minus)
                    ? (l1.getLexemaClass() == l2.getLexemaClass() ? new Lexema("+", Plus) : new Lexema("-", Minus))
                    : new Lexema("+", Plus);
        }
        throw new RuntimeException();
    }

    private boolean isExpressionPart(Lexema lexema) {
        return priorities.isMapped(lexema.getLexemaClass()) || isConstant(lexema) || If == lexema.getLexemaClass() || DoWhileLoopStart == lexema.getLexemaClass();
    }

    private boolean isConstant(Lexema lexema) {
        return Arrays.asList(Identifier, ConstantInt, ConstantReal, True, False).contains(lexema.getLexemaClass());
    }

    private Integer ifThenExpressionEndIdx(List<Lexema> lexemas, int idx) {
        return specificExpressionEndIdx(lexemas, idx, If, Then, OpenBlockBracket, CloseBlockBracket);
    }
    private Integer doWhileExpressionEndIdx(List<Lexema> lexemas, int idx) {
        return specificExpressionEndIdx(lexemas, idx, DoWhileLoopStart, DoWhileLoopEnd, OpenBracket, CloseBracket);
    }

    private Integer specificExpressionEndIdx(List<Lexema> lexemas, int idx, LexemaClass firstPartOpen, LexemaClass firstPartClose, LexemaClass lastPartOpen, LexemaClass lastPartClose) {
        int mark = 0;
        int next = -1;

        for (int i = idx; i < lexemas.size(); i++) {
            if (lexemas.get(i).getLexemaClass() == firstPartOpen) {
                mark++;
            }
            if (lexemas.get(i).getLexemaClass() == firstPartClose) {
                if (--mark == 0) {
                    next = i + 1;
                    break;
                }
            }
        }
        for (int i = next; i < lexemas.size(); i++) {

            if (lexemas.get(i).getLexemaClass() == lastPartOpen) {
                mark++;
            }
            if (lexemas.get(i).getLexemaClass() == lastPartClose) {
                if (--mark == 0) {
                    return i;
                }
            }
        }

        throw new RuntimeException("Not found");
    }

}
