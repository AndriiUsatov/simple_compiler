package andrii.app.univ.entity.rule;

import andrii.app.univ.entity.rule.impl.RuleChain;

public interface Rule {

    RuleResults isCorrect(RuleContext context);

    static RuleChain chainOf(Rule rootRule) {
        return new RuleChain(rootRule);
    }
}
