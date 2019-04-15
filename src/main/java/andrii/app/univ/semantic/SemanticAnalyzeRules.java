package andrii.app.univ.semantic;

import andrii.app.univ.entity.error.TranslateError;
import andrii.app.univ.entity.error.SpecificError;
import andrii.app.univ.entity.error.SemanticError;
import andrii.app.univ.entity.lexema.Lexema;
import andrii.app.univ.entity.lexema.LexemaClass;
import andrii.app.univ.entity.rule.Rule;
import andrii.app.univ.entity.rule.RuleResults;
import andrii.app.univ.entity.rule.impl.RuleChain;
import andrii.app.univ.util.LexHelper;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static andrii.app.univ.entity.lexema.LexemaClass.*;

public class SemanticAnalyzeRules {

    private static TranslateError err(Lexema lex, LexemaClass... expected) {
        return new SemanticError(lex.getValue(), lex.getRowNumber(), Stream.of(expected).map(LexemaClass::toString).collect(Collectors.toList()));
    }

    public Rule specificClassesExpect(LexemaClass... lexemaClass) {
        return context -> {
            Boolean result;
            try {
                result = Arrays.asList(lexemaClass).contains(context.currentLexema().getLexemaClass());
            } catch (IndexOutOfBoundsException e) {
                return new RuleResults(false, new SpecificError("Expected: " + Arrays.asList(lexemaClass) + ", but lexema list is empty."));
            }
            if (result) {
                context.iterateIdx();
            }
            return result ? new RuleResults(true) : new RuleResults(false, err(context.currentLexema(), lexemaClass));
        };
    }

    public Rule specificClassesNotRequired(LexemaClass... lexemaClass) {
        return context -> {
            try {
                if (Arrays.asList(lexemaClass).contains(context.currentLexema().getLexemaClass())) {
                    context.iterateIdx();
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            return new RuleResults(true);
        };
    }

    private Rule alwaysTrue() {
        return context -> new RuleResults(true);
    }

    public Rule booleanType() {
        return specificClassesExpect(BooleanType);
    }

    public Rule integerType() {
        return specificClassesExpect(IntegerType);
    }

    public Rule realType() {
        return specificClassesExpect(RealType);
    }

    public Rule endl() {
        return specificClassesExpect(EndLineDivider);
    }

    public Rule identifier() {
        return specificClassesExpect(Identifier);
    }

    public Rule appropriation() {
        return specificClassesExpect(Appropriation);
    }

    public Rule trueVal() {
        return specificClassesExpect(True);
    }

    public Rule falseVal() {
        return specificClassesExpect(False);
    }

    public Rule logicValue() {
        return Rule.chainOf(alwaysTrue()).anyOf(trueVal(), falseVal());
    }

    public Rule expressionOpenBracket() {
        return specificClassesExpect(OpenBracket);
    }

    public Rule expressionCloseBracket() {
        return specificClassesExpect(CloseBracket);
    }

    public Rule blockOpenBracket() {
        return specificClassesExpect(OpenBlockBracket);
    }

    public Rule blockCloseBracket() {
        return specificClassesExpect(CloseBlockBracket);
    }

    public Rule numberExpression() {
        return Rule.chainOf(alwaysTrue()).anyOf(integerExpression(), realExpression());
    }

    public Rule logicalOperation() {
        return specificClassesExpect(LogicalAnd, LogicalOr);
    }

    public Rule integerExpression() {
        RuleChain chain = Rule.chainOf(alwaysTrue());
        return chain.anyOf(
                Rule.chainOf(expressionOpenBracket()).with(/*specificClassesNotRequired(Plus, Minus)*/repeatablePlusMinusNonRequired(), Rule.chainOf(alwaysTrue()).anyOf(unsignedInt(), integerTypeIdentifier()),
                        Rule.chainOf(alwaysTrue()).anyOf(Rule.chainOf(specificClassesExpect(Plus, Minus, Multiply, Divide)).with(chain), alwaysTrue()), expressionCloseBracket()),
                Rule.chainOf(alwaysTrue()).with(/*specificClassesNotRequired(Plus, Minus)*/repeatablePlusMinusNonRequired(), Rule.chainOf(alwaysTrue()).anyOf(unsignedInt(), integerTypeIdentifier()),
                        Rule.chainOf(alwaysTrue()).anyOf(Rule.chainOf(specificClassesExpect(Plus, Minus, Multiply, Divide)).with(chain), alwaysTrue()))
        );
    }

    public Rule repeatablePlusMinusNonRequired() {
        return new RuleChain(alwaysTrue()) {
            {
                anyOf(Rule.chainOf(specificClassesExpect(Plus, Minus)).with(this), alwaysTrue());
            }
        };
    }

    public Rule realExpression() {
        RuleChain chain = Rule.chainOf(alwaysTrue());
        return chain.anyOf(
                Rule.chainOf(alwaysTrue()).with(expressionOpenBracket(), /*specificClassesNotRequired(Plus, Minus)*/ repeatablePlusMinusNonRequired(), Rule.chainOf(alwaysTrue()).anyOf(constNumber(), numberIdentifier()),
                        Rule.chainOf(alwaysTrue()).anyOf(Rule.chainOf(specificClassesExpect(Plus, Minus, Multiply, Divide)).with(chain), alwaysTrue()), expressionCloseBracket()),
                Rule.chainOf(alwaysTrue()).with(/*specificClassesNotRequired(Plus, Minus)*/ repeatablePlusMinusNonRequired(), Rule.chainOf(alwaysTrue()).anyOf(constNumber(), numberIdentifier()),
                        Rule.chainOf(alwaysTrue()).anyOf(Rule.chainOf(specificClassesExpect(Plus, Minus, Multiply, Divide)).with(chain), alwaysTrue()))
        );
    }

    public Rule booleanExpression() {
        RuleChain chain = Rule.chainOf(alwaysTrue());
        return chain.anyOf(
                Rule.chainOf(expressionOpenBracket()).with(specificClassesNotRequired(Not), Rule.chainOf(alwaysTrue()).anyOf(logicValue(), booleanTypeIdentifier(),
                        Rule.chainOf(numberResultItem()).with(equalsOperations(), numberResultItem())),
                        Rule.chainOf(alwaysTrue()).anyOf(Rule.chainOf(alwaysTrue()).with(logicalOperation(), chain), alwaysTrue()), expressionCloseBracket()),
                Rule.chainOf(alwaysTrue()).with(specificClassesNotRequired(Not), Rule.chainOf(alwaysTrue()).anyOf(logicValue(), booleanTypeIdentifier(),
                        Rule.chainOf(numberResultItem()).with(equalsOperations(), numberResultItem())),
                        Rule.chainOf(alwaysTrue()).anyOf(Rule.chainOf(alwaysTrue()).with(logicalOperation(), chain), alwaysTrue()))
        );
    }

    public Rule ifThen(Rule blockContentRule) {
        return Rule.chainOf(specificClassesExpect(If)).with(expressionOpenBracket(), booleanExpression(),
                expressionCloseBracket(), specificClassesExpect(Then), blockOpenBracket(), blockContentRule, blockCloseBracket());
    }

    public Rule blockContent() {
        return new RuleChain(alwaysTrue()) {
            {
                anyOf(block(this), statements(), declarations(), consoleOut(), doWhileLoop(this), ifThen(this)).anyOf(this, alwaysTrue());
            }
        };
    }

    public Rule consoleOut() {
        return Rule.chainOf(specificClassesExpect(SystemOut)).with(expressionOpenBracket(),
                Rule.chainOf(alwaysTrue()).anyOf(booleanExpression(), realExpression(), integerExpression()), expressionCloseBracket(), endl());
    }

    public Rule integerResultItem() {
        return Rule.chainOf(alwaysTrue()).anyOf(integerExpression(), integerTypeIdentifier());
    }

    public Rule realResultItem() {
        return Rule.chainOf(alwaysTrue()).anyOf(realExpression(), realTypeIdentifier());
    }

    public Rule numberResultItem() {
        return Rule.chainOf(alwaysTrue()).anyOf(integerResultItem(), realResultItem());
    }

    public Rule constNumber() {
        return Rule.chainOf(alwaysTrue()).anyOf(unsignedInt(), specificClassesExpect(ConstantReal), specificClassesExpect(SystemInReal));
    }

    public Rule unsignedInt() {
        return Rule.chainOf(alwaysTrue()).anyOf(specificClassesExpect(ConstantInt), specificClassesExpect(SystemInInteger));
    }

    public Rule numberIdentifier() {
        return Rule.chainOf(alwaysTrue()).anyOf(integerTypeIdentifier(), realTypeIdentifier());
    }

    public Rule equalsOperations() {
        return Rule.chainOf(alwaysTrue()).anyOf(specificClassesExpect(Equals), specificClassesExpect(NotEquals), specificClassesExpect(LessThen),
                specificClassesExpect(MoreThen), specificClassesExpect(LessOrEqual), specificClassesExpect(MoreOrEqual));
    }

    public Rule booleanTypeIdentifier() {
        return context -> {
            try {
                Boolean result = context.currentLexema().getLexemaClass() == Identifier
                        && "BOOLEAN".equals(LexHelper.identifierStringType(context.currentLexema(), context.getLexemas()));
                if (result) {
                    context.iterateIdx();
                }
                return result ? new RuleResults(true)
                        : new RuleResults(false, new SemanticError(context.currentLexema().getValue(), context.getIndex(), "Boolean Type Identifier"));
            } catch (IndexOutOfBoundsException e) {
                return new RuleResults(false, new SpecificError("Expected: " + BooleanType + ", but lexema list is empty."));
            }
        };
    }

    public Rule integerTypeIdentifier() {
        return context -> {
            try {
                Boolean result = context.currentLexema().getLexemaClass() == Identifier
                        && "INTEGER".equals(LexHelper.identifierStringType(context.currentLexema(), context.getLexemas()));
                if (result) {
                    context.iterateIdx();
                }
                return result ? new RuleResults(true)
                        : new RuleResults(false, new SemanticError(context.currentLexema().getValue(), context.getIndex(), "Integer Type Identifier"));
            } catch (IndexOutOfBoundsException e) {
                return new RuleResults(false, new SpecificError("Expected: " + IntegerType + ", but lexema list is empty."));
            }
        };
    }

    public Rule realTypeIdentifier() {
        return context -> {
            try {
                Boolean result = context.currentLexema().getLexemaClass() == Identifier
                        && "REAL".equals(LexHelper.identifierStringType(context.currentLexema(), context.getLexemas()));

                System.out.println("=========REAL " + context.currentLexema() + ", res: " + result);
                if (result) {
                    context.iterateIdx();
                }
                return result ? new RuleResults(true)
                        : new RuleResults(false, new SemanticError(context.currentLexema().getValue(), context.getIndex(), "Real Type Identifier"));
            } catch (IndexOutOfBoundsException e) {
                return new RuleResults(false, new SpecificError("Expected: " + RealType + ", but lexema list is empty."));
            }
        };
    }

    public Rule declarations() {
        RuleChain chain = Rule.chainOf(declaration());
        return chain.anyOf(
                chain, alwaysTrue()
        );
    }

    public Rule declaration() {
        return Rule.chainOf(alwaysTrue())
                .anyOf(
                        Rule.chainOf(booleanType()).with(identifier(), Rule.chainOf(alwaysTrue()).anyOf(Rule.chainOf(appropriation()).with(booleanExpression()), alwaysTrue())),
                        Rule.chainOf(integerType()).with(identifier(), Rule.chainOf(alwaysTrue()).anyOf(Rule.chainOf(appropriation()).with(integerExpression()), alwaysTrue())),
                        Rule.chainOf(realType()).with(identifier(), Rule.chainOf(alwaysTrue()).anyOf(Rule.chainOf(appropriation()).with(numberExpression()), alwaysTrue()))
                ).with(endl());
    }

    public Rule assignment() {
        return Rule.chainOf(alwaysTrue()).anyOf(
                Rule.chainOf(integerTypeIdentifier()).with(appropriation(), integerExpression()),
                Rule.chainOf(realTypeIdentifier()).with(appropriation(), numberExpression()),
                Rule.chainOf(booleanTypeIdentifier()).with(appropriation(), booleanExpression())
        );
    }

    public Rule doWhileLoop(Rule blockContentRule) {
        return Rule.chainOf(specificClassesExpect(DoWhileLoopStart)).with(blockOpenBracket(), blockContentRule, blockCloseBracket(),
                specificClassesExpect(DoWhileLoopEnd), expressionOpenBracket(), booleanExpression(), expressionCloseBracket(), endl());
    }

    public Rule statements() {
        RuleChain chain = Rule.chainOf(assignment());
        return chain.with(endl()).anyOf(chain, alwaysTrue());
    }

    public Rule block(Rule blockContent) {
        return Rule.chainOf(blockOpenBracket()).anyOf(blockContent, alwaysTrue()).with(blockCloseBracket());
    }

    public Rule program() {
        return new RuleChain(alwaysTrue()) {
            {
                anyOf(blockContent()).anyOf(this, alwaysTrue());
            }
        };
    }

}
