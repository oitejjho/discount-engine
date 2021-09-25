package com.discountengine.demo.manager;

import com.discountengine.demo.converter.StringToDeliveryDiscountInfo;
import com.discountengine.demo.loader.PriceLoader;
import com.discountengine.demo.model.DeliveryDiscountInfo;
import com.discountengine.demo.ruleengine.RuleEngine;
import com.discountengine.demo.rules.LowestSizeRule;
import com.discountengine.demo.rules.MonthlyLPFreeDeliveryRule;
import com.discountengine.demo.rules.MonthlyTotalDiscountRule;
import com.discountengine.demo.util.FileOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

public class RuleEngineManager {

    private static final Logger logger = LoggerFactory.getLogger(RuleEngineManager.class);

    /*private static List<IRule<DeliveryDiscountInfo, DeliveryDiscountInfo>> getAllRules(){
        PriceLoader priceLoader = priceLoader();
        List<IRule<DeliveryDiscountInfo, DeliveryDiscountInfo>> rules = new ArrayList<>();

        LowestSizeRule lowestSizeRule = new LowestSizeRule(priceLoader);
        rules.add(lowestSizeRule);
        MonthlyLPFreeDelivery monthlyLPFreeDelivery = new MonthlyLPFreeDelivery(priceLoader);
        rules.add(monthlyLPFreeDelivery);
        MonthlyTotalDiscountRule monthlyTotalDiscountRule = new MonthlyTotalDiscountRule();
        monthlyTotalDiscountRule.add(lowestSizeRule);
        monthlyTotalDiscountRule.add(monthlyLPFreeDelivery);
        rules.add(monthlyTotalDiscountRule);

        return rules;
    }

    private static PriceLoader priceLoader(){
        return new PriceLoader();
    }

    private static RuleEngine ruleEngine(){
        return new RuleEngine();
    }*/


    public static void applyRules() {
        RuleEngine ruleEngine = new RuleEngine();
        PriceLoader priceLoader = new PriceLoader();
        MonthlyLPFreeDeliveryRule monthlyLPFreeDeliveryRule = new MonthlyLPFreeDeliveryRule(priceLoader);
        LowestSizeRule lowestSizeRule = new LowestSizeRule(priceLoader);
        MonthlyTotalDiscountRule monthlyTotalDiscountRule = new MonthlyTotalDiscountRule();
        monthlyTotalDiscountRule.add(lowestSizeRule);
        monthlyTotalDiscountRule.add(monthlyLPFreeDeliveryRule);

        ruleEngine.registerRule(monthlyTotalDiscountRule);
        ruleEngine.registerRule(monthlyLPFreeDeliveryRule);
        ruleEngine.registerRule(lowestSizeRule);

        Flux<String> deliveryInfoFlux = FileOperations.readLines("input.txt");
        Mono<List<DeliveryDiscountInfo>> listMono = deliveryInfoFlux
                .map(new StringToDeliveryDiscountInfo()::convert)
                .map(ruleEngine::rule)
                .doOnNext(s -> logger.info("{}", s))
                .onErrorContinue((throwable, s) -> logger.warn("{} ignored ", s))
                .collectList();
        Flux<DeliveryDiscountInfo> deliveryDiscountInfoFlux = listMono
                .flatMapMany(Flux::fromIterable);
        try {
            FileOperations.writeLines("output.txt", deliveryDiscountInfoFlux);
        } catch (IOException e) {
            logger.error("error happened while writing {}, {}", e.getMessage(), e);
        }
    }


}
