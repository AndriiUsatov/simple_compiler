package andrii.app.univ.entity.lexema;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum LexemaClass {

    Plus(1, "+"),                       //OpenBracket, ConstantInt, Increment, Decrement, ConstantReal, Indentificator
    Minus(2, "-"),                      //OpenBracket, ConstantInt, Increment, Decrement, ConstantReal, Indentificator
    Multiply(3, "*"),                   //OpenBracket, ConstantInt, Increment, Decrement, ConstantReal, Indentificator
    Divide(4, "/"),                     //OpenBracket, ConstantInt, Increment, Decrement, ConstantReal, Indentificator
    BooleanType(5, "Boolean"),         //Identifier
    OpenBracket(6, "("),                //Plus, Minus, OpenBracket, CloseBracket, ConstantInt, Increment, Decrement, Not, ConstantReal, Identifier, True, False
    CloseBracket(7, ")"),               //Plus, Minus, Multiply, Divide, DivideLeft, CloseBracket, EndLineDivider, Then, Equals, Appropriation, LessThen, MoreThen, LessOrEqual, MoreOrEqual, Increment, Decrement,NotEquals, LogicalOr, LogicalAnd
    ConstantInt(8),                                  //Plus, Minus, Multiply, Divide, DivideLeft, EndLineDivider, Equals, LessThen, MoreThen, LessOrEqual, MoreOrEqual, Increment, Decrement, NotEquals
    EndLineDivider(9, ";"),             //Plus, Minus, OpenBracket, ConstantInt, EndLineDivider, DoWhileLoopStart, DoWhileLoopEnd, If, Increment, Decrement, RealType, Not, ConstantReal, Identifier, True, False, BooleanType
    DoWhileLoopStart(10, "do"),         //True, False, BooleanType, Plus, Minus, OpenBracket, ConstantInt, EndLineDivider, DoWhileLoopStart, DoWhileLoopEnd, If, IntegerType, Increment, Decrement, RealType, Not, ConstantReal, Identifier
    DoWhileLoopEnd(11, "while"),        //True, False, Plus, Minus, OpenBracket, ConstantInt, Increment, Decrement, Not, ConstantReal, Identifier
    If(12, "if"),                       //True, False, Plus, Minus, OpenBracket, ConstantInt, Increment, Decrement, Not, ConstantReal, Identifier
    Then(13, "then"),                   //True, False, BooleanType, Plus, Minus, OpenBracket, ConstantInt, EndLineDivider, DoWhileLoopStart, If, IntegerType, Increment, Decrement, RealType, Not, ConstantReal, Identifier
    Equals(14, "=="),                   //True, False, OpenBracket, ConstantInt, Increment, Decrement, Not, ConstantReal, Identifier
    IntegerType(15, "Integer"),         //Identifier
    Appropriation(16, "="),             //True, False, Plus, Minus, OpenBracket, ConstantInt, Increment, Decrement, ConstantReal, Identifier
    LessThen(17, "<"),                  //Plus, Minus, OpenBracket, Increment, Decrement, ConstantReal, Identifier
    MoreThen(18, ">"),                  //Plus, Minus, OpenBracket, Increment, Decrement, ConstantReal, Identifier
    LessOrEqual(19, "<="),              //Plus, Minus, OpenBracket, Increment, Decrement, ConstantReal, Identifier
    MoreOrEqual(20, ">="),              //Plus, Minus, OpenBracket, Increment, Decrement, ConstantReal, Identifier
    RealType(21, "Real"),               //Identifier
    NotEquals(22, "!="),                //True, False, OpenBracket, ConstantInt, Increment, Decrement, Not, ConstantReal, Identifier
    Not(23, "!"),                       //True, False, OpenBracket, Identifier
    LogicalOr(24, "||"),                //True, False, OpenBracket, ConstantInt, Minus, Plus, Increment, Decrement, Not, ConstantReal, Identifier
    LogicalAnd(25, "&&"),               //True, False, OpenBracket, ConstantInt, Minus, Plus, Increment, Decrement, Not, ConstantReal, Identifier
    True(26, "true"),                   //EndLineDivider, CloseBracket, Equals, NotEquals, LogicalOr, LogicalAnd, True, False, Identifier
    False(27, "false"),                 //EndLineDivider, CloseBracket, Equals, NotEquals, LogicalOr, LogicalAnd, True, False, Identifier
    OpenBlockBracket(28, "{"),          //Plus, Minus, OpenBracket, ConstantInt, EndLineDivider, DoWhileLoopStart, DoWhileLoopEnd, If, IntegerType, Increment, Decrement, RealType, Not, BooleanType, True, False, OpenBlockBracket, CloseBlockBracket, ConstantReal, Identifier
    CloseBlockBracket(29, "}"),         //Plus, Minus, OpenBracket, ConstantInt, EndLineDivider, DoWhileLoopStart, DoWhileLoopEnd, If, IntegerType, Increment, Decrement, RealType, Not, BooleanType, True, False, OpenBlockBracket, CloseBlockBracket, ConstantReal, Identifier
    ConstantReal(30),                                //Plus, Minus, Multiply, Divide, EndLineDivider, Equals, LessThen, MoreThen, LessOrEqual, MoreOrEqual, NotEquals
    NUMBER_NEGATION(31, "%"),
    SystemOut(32, "consoleWrite"),
    SystemInInteger(33, "consoleReadInt()"),
    SystemInReal(34, "consoleReadReal()"),
    Identifier(35);                      //Plus, Minus, Multiply, Divide, DivideLeft, EndLineDivider, Equals, Appropriation, LessThen, MoreThen, LessOrEqual, MoreOrEqual, Increment, Decrement, NotEquals

    int code;
    String representaion;
    static List<LexemaClass> operatorList = Stream.of(1,2,3,4,14,16,17,18,19,20,22,23,24,25,31,32,33,34)
            .map(LexemaClass::getLexemaByCode)
            .collect(Collectors.toList());
    LexemaClass(int code) {
        this.code = code;
    }

    LexemaClass(int code, String representaion) {
        this.code = code;
        this.representaion = representaion;
    }

    public int getCode() {
        return code;
    }

    public String getRepresentaion() {
        return representaion;
    }

    public static LexemaClass getLexemaByCode(int code) {
        return Stream.of(LexemaClass.class.getEnumConstants())
                .filter(lex -> lex.getCode() == code)
                .findFirst().get();
    }

    public static List<LexemaClass> operators() {
        return operatorList;
    }


    @Override
    public String toString() {
        return representaion == null ? super.toString() : representaion;
    }

}
