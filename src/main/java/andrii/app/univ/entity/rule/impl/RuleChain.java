package andrii.app.univ.entity.rule.impl;

import andrii.app.univ.entity.rule.Rule;
import andrii.app.univ.entity.rule.RuleContext;
import andrii.app.univ.entity.rule.RuleResults;

import java.util.ArrayList;
import java.util.List;

public class RuleChain implements Rule {

    private List<Rule> rules = new ArrayList<>();

    public RuleChain(Rule rule) {
        rules.add(rule);
    }

    public RuleChain with(Rule... rules) {

        this.rules.add(context -> {
            RuleContext currCtx = context.clone();
            RuleResults rr = null;
            for (int i = 0; i < rules.length; i++) {
                rr = rules[i].isCorrect(currCtx);
                if (!rr.result()) {
                    context.addErrors(rr.errors(), currCtx.getIndex());
                    System.out.println("With (FALSE) :" + rr.errors());
                    return rr;
                }
            }
            if (rr.result()) {
                context.setIndex(currCtx.getIndex());
            }
            System.out.println("With :" + rr.errors());
            return rr;
        });

        return this;
    }

    public RuleChain anyOf(Rule... rules) {
        this.rules.add(context -> {
            RuleResults anyResults = null;
            for (Rule itm : rules) {
                RuleContext itmCtx = context.clone();
                RuleResults tmp = itm.isCorrect(itmCtx);
                if (!tmp.errors().isEmpty()) {
                    context.addErrors(tmp.errors(), itmCtx.getIndex());
                }
                if (tmp.result()) {
                    context.setIndex(itmCtx.getIndex());
                    System.out.println("Any Of (TRUE) :" + tmp.errors());
                    return tmp;
                }
                if (anyResults == null) {
                    anyResults = tmp;
                } else {
                    anyResults.addAllErrors(tmp.errors());
                }
            }
            System.out.println("Any Of :" + anyResults.errors());
            return anyResults;
        });

        return this;
    }

    @Override
    public RuleResults isCorrect(RuleContext context) {
        RuleResults rr = new RuleResults(true);
        for (Rule rule : rules) {
            RuleResults tmp = rule.isCorrect(context);
            rr.setResult(rr.result() && tmp.result());
            rr.addAllErrors(tmp.errors());
            if (!rr.result()) {
                context.addErrors(tmp.errors(), context.getIndex());
                System.out.println("Is Correct (FALSE)" + rr.errors());
                return rr;
            }
        }
        System.out.println("Is Correct " + rr.errors());
        return rr;
    }

}
