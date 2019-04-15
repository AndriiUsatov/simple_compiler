package andrii.app.univ.entity.lexema;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static andrii.app.univ.entity.lexema.LexemaClass.*;

public class LexemaRouteMapping {

    private static final LexemaRouteMapping DEFAULT_MAPPING = new LexemaRouteMapping();
    private Map<LexemaClass, List<LexemaClass>> routeMap = new HashMap<>();

    private LexemaRouteMapping() {
        initDefaultMapping();
    }

    private void initDefaultMapping() {
        routeMap.put(Plus, Arrays.asList(OpenBracket, ConstantInt, ConstantReal, Identifier, Plus, Minus, SystemInInteger, SystemInReal));
        routeMap.put(Minus, Arrays.asList(OpenBracket, ConstantInt,ConstantReal, Identifier, Plus, Minus, SystemInInteger, SystemInReal));
        routeMap.put(Multiply, Arrays.asList(Multiply, OpenBracket, ConstantInt, ConstantReal, Identifier, Plus, Minus, SystemInInteger, SystemInReal));
        routeMap.put(Divide, Arrays.asList(OpenBracket, ConstantInt, ConstantReal, Identifier, Plus, Minus, SystemInInteger, SystemInReal));
        routeMap.put(OpenBracket, Arrays.asList(Plus, Minus, OpenBracket, CloseBracket, ConstantInt, Not, ConstantReal, Identifier, True, False, SystemInInteger, SystemInReal));
        routeMap.put(CloseBracket, Arrays.asList(Plus, Minus, Multiply, Divide, CloseBracket, EndLineDivider, Then, Equals, Appropriation, LessThen, MoreThen, LessOrEqual, MoreOrEqual, NotEquals,
                LogicalOr, LogicalAnd));
        routeMap.put(ConstantInt, Arrays.asList(CloseBracket, Plus, Minus, Multiply, Divide, EndLineDivider, Equals, LessThen, MoreThen, LessOrEqual, MoreOrEqual, NotEquals, LogicalAnd, LogicalOr));
        routeMap.put(EndLineDivider, Arrays.asList(OpenBlockBracket, CloseBlockBracket, IntegerType, OpenBracket, ConstantInt, EndLineDivider, DoWhileLoopStart, DoWhileLoopEnd, If, RealType, Not, ConstantReal,
                Identifier, True, False, BooleanType, SystemOut));
        routeMap.put(DoWhileLoopStart, Arrays.asList(OpenBlockBracket));
        routeMap.put(DoWhileLoopEnd, Arrays.asList(OpenBracket));
        routeMap.put(If, Arrays.asList(OpenBracket));
        routeMap.put(Then, Arrays.asList(OpenBlockBracket));
        routeMap.put(Equals, Arrays.asList(True, False, OpenBracket, ConstantInt, Not, ConstantReal, Identifier, SystemInInteger, SystemInReal));
        routeMap.put(IntegerType, Arrays.asList(Identifier));
        routeMap.put(Appropriation, Arrays.asList(Minus, Plus ,True, False, OpenBracket, ConstantInt, ConstantReal, Identifier, Not, SystemInInteger, SystemInReal));
        routeMap.put(LessThen, Arrays.asList(Plus, Minus, OpenBracket, ConstantInt, ConstantReal, Identifier, SystemInInteger, SystemInReal));
        routeMap.put(MoreThen, Arrays.asList(Plus, Minus, OpenBracket, ConstantInt, ConstantReal, Identifier, SystemInInteger, SystemInReal));
        routeMap.put(LessOrEqual, Arrays.asList(ConstantInt, Plus, Minus, OpenBracket, ConstantReal, Identifier, SystemInInteger, SystemInReal));
        routeMap.put(MoreOrEqual, Arrays.asList(Plus, Minus, OpenBracket, ConstantReal, ConstantInt, Identifier, SystemInInteger, SystemInReal));
        routeMap.put(RealType, Arrays.asList(Identifier));
        routeMap.put(NotEquals, Arrays.asList(True, False, OpenBracket, ConstantInt, Not, ConstantReal, Identifier, SystemInInteger, SystemInReal));
        routeMap.put(Not, Arrays.asList(True, False, OpenBracket, Identifier));
        routeMap.put(LogicalOr, Arrays.asList(True, False, OpenBracket, ConstantInt, Minus, Plus, Not, ConstantReal, Identifier, SystemInInteger, SystemInReal));
        routeMap.put(LogicalAnd, Arrays.asList(True, False, OpenBracket, ConstantInt, Minus, Plus, Not, ConstantReal, Identifier, SystemInInteger, SystemInReal));
        routeMap.put(BooleanType, Arrays.asList(Identifier));
        routeMap.put(True, Arrays.asList(EndLineDivider, CloseBracket, Equals, NotEquals, LogicalOr, LogicalAnd, True, False, Identifier));
        routeMap.put(False, Arrays.asList(EndLineDivider, CloseBracket, Equals, NotEquals, LogicalOr, LogicalAnd, True, False, Identifier));
        routeMap.put(ConstantReal, Arrays.asList(Plus, Minus, Multiply, Divide, EndLineDivider, Equals, LessThen, MoreThen, LessOrEqual, MoreOrEqual, NotEquals, CloseBracket, LogicalAnd, LogicalOr));
        routeMap.put(Identifier, Arrays.asList(Plus, Minus, Multiply, Divide, EndLineDivider, Equals, Appropriation, LessThen, MoreThen, LessOrEqual, MoreOrEqual, NotEquals, CloseBracket, LogicalAnd, LogicalOr));
        routeMap.put(OpenBlockBracket, Arrays.asList(OpenBracket, ConstantInt, EndLineDivider, DoWhileLoopStart, DoWhileLoopEnd, If, IntegerType, RealType, Not, BooleanType,
                True, False, OpenBlockBracket, CloseBlockBracket, ConstantReal, Identifier, SystemOut));
        routeMap.put(CloseBlockBracket, Arrays.asList(OpenBracket, ConstantInt, EndLineDivider, DoWhileLoopStart, DoWhileLoopEnd, If, IntegerType, RealType, Not, BooleanType,
                True, False, OpenBlockBracket, CloseBlockBracket, ConstantReal, Identifier, SystemOut));
        routeMap.put(SystemOut, Arrays.asList(OpenBracket));
        routeMap.put(SystemInInteger, Arrays.asList(Plus, Minus, Multiply, Divide, CloseBracket, EndLineDivider, Equals, LessThen, MoreThen, LessOrEqual, MoreOrEqual, NotEquals));
        routeMap.put(SystemInReal, Arrays.asList(Plus, Minus, Multiply, Divide, CloseBracket, EndLineDivider, Equals, LessThen, MoreThen, LessOrEqual, MoreOrEqual, NotEquals));

    }


    public Map<LexemaClass, List<LexemaClass>> getRouteMap() {
        return routeMap;
    }

    public static LexemaRouteMapping getDefaultMapping() {
        return DEFAULT_MAPPING;
    }
}
