package com.discountengine.demo.ruleengine;

import com.discountengine.demo.model.DeliveryDiscountInfo;
import com.discountengine.demo.model.DeliveryInfo;
import com.discountengine.demo.rules.IRule;
import com.discountengine.demo.rules.LowestSizeRule;

import java.util.ArrayList;
import java.util.List;

public class RuleEngine {

    List<IRule<DeliveryDiscountInfo, DeliveryDiscountInfo>> rules;

    public RuleEngine() {
        rules = new ArrayList<>();
    }

    public DeliveryDiscountInfo rule(DeliveryDiscountInfo info) {
        return rules.stream()
                .filter(rule -> rule.matches(info))
                .map(rule -> rule.process(info))
                .findFirst()
//                .filter(rule -> rule.getPackageSize().equalsIgnoreCase("S"))
                .orElse(info);
//                .orElseThrow(() -> new RuntimeException("No Matching rule found"));
    }

    public RuleEngine registerRule(IRule rule) {
        rules.add(rule);
        return this;
    }
}
