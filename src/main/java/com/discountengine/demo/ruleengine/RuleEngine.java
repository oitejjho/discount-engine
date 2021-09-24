package com.discountengine.demo.ruleengine;

import com.discountengine.demo.model.DeliveryDiscountInfo;
import com.discountengine.demo.rules.IRule;

import java.util.ArrayList;
import java.util.List;

public class RuleEngine {

    private final List<IRule<DeliveryDiscountInfo, DeliveryDiscountInfo>> rules;

    public RuleEngine() {
        rules = new ArrayList<>();
    }

    public DeliveryDiscountInfo rule(DeliveryDiscountInfo info) {
        return rules.stream()
                .filter(rule -> !info.isMatched() && rule.matches(info))
                .map(rule -> rule.process(info))
                .findFirst()
                .orElse(info);
    }

    public RuleEngine registerRule(IRule<DeliveryDiscountInfo, DeliveryDiscountInfo> rule) {
        rules.add(rule);
        return this;
    }
}
