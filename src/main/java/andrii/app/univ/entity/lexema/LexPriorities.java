package andrii.app.univ.entity.lexema;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static andrii.app.univ.entity.lexema.LexemaClass.*;

public class LexPriorities {

    private static final LexPriorities DEFAULT_PRIORITIES_MAPPING = initNewDefaultMapping();
    private Map<LexemaClass, Integer> prioritiesMapping;

    public LexPriorities(Map<LexemaClass, Integer> prioritiesMapping) {
        this.prioritiesMapping = prioritiesMapping;
    }

    public static LexPriorities getDefaultPrioritiesMapping() {
        return DEFAULT_PRIORITIES_MAPPING;
    }

    private static LexPriorities initDefaultMapping() {
        Map<LexemaClass, Integer> mapping = new HashMap<>();
        Stream.of(OpenBracket, CloseBracket).forEach(itm -> mapping.put(itm, 1));;
        Stream.of(Not).forEach(itm -> mapping.put(itm, 3));
        Stream.of(Multiply, Divide).forEach(itm -> mapping.put(itm, 4));
        Stream.of(Plus, Minus).forEach(itm -> mapping.put(itm, 5));
        Stream.of(LessThen, LessOrEqual, MoreThen, MoreOrEqual).forEach(itm -> mapping.put(itm, 6));

        Stream.of(Equals, NotEquals).forEach(itm -> mapping.put(itm, 7));
        Stream.of(LogicalAnd).forEach(itm -> mapping.put(itm, 8));
        Stream.of(LogicalOr).forEach(itm -> mapping.put(itm, 9));
        Stream.of(Appropriation).forEach(itm -> mapping.put(itm, 10));
        return new LexPriorities(mapping);
    }

    private static LexPriorities initNewDefaultMapping() {
        Map<LexemaClass, Integer> mapping = new HashMap<>();
        Stream.of(SystemOut).forEach(itm -> mapping.put(itm, -2));
        Stream.of(SystemInInteger, SystemInReal).forEach(itm -> mapping.put(itm, 10));
        Stream.of(OpenBracket, CloseBracket).forEach(itm -> mapping.put(itm, 0));
        Stream.of(Not).forEach(itm -> mapping.put(itm, 9));
        Stream.of(NUMBER_NEGATION).forEach(itm -> mapping.put(itm, 8));
        Stream.of(Multiply, Divide).forEach(itm -> mapping.put(itm, 7));
        Stream.of(Plus, Minus).forEach(itm -> mapping.put(itm, 6));
        Stream.of(LessThen, LessOrEqual, MoreThen, MoreOrEqual).forEach(itm -> mapping.put(itm, 5));
        Stream.of(Equals, NotEquals).forEach(itm -> mapping.put(itm, 4));
        Stream.of(LogicalAnd).forEach(itm -> mapping.put(itm, 3));
        Stream.of(LogicalOr).forEach(itm -> mapping.put(itm, 2));
        Stream.of(Appropriation).forEach(itm -> mapping.put(itm, -3));
        return new LexPriorities(mapping);
    }

    public Integer priority(LexemaClass cls) {
        return prioritiesMapping.get(cls);
    }

    public boolean isMapped(LexemaClass cls) {
        return prioritiesMapping.keySet().contains(cls);
    }

}
