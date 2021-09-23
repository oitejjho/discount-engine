package com.discountengine.demo;

import com.discountengine.demo.converter.StringToDeliveryDiscountInfo;
import com.discountengine.demo.model.DeliveryDiscountInfo;
import com.discountengine.demo.ruleengine.RuleEngine;
import com.discountengine.demo.rules.LowestSizeRule;
import com.discountengine.demo.rules.MonthlyLPFreeDelivery;
import com.discountengine.demo.rules.MonthlyTotalDiscountRule;
import com.discountengine.demo.util.FileOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

public class DiscountEngineApp {

    private static final Logger logger = LoggerFactory.getLogger(LowestSizeRule.class);

    public static void main(String[] args) throws IOException {

        RuleEngine ruleEngine = new RuleEngine();
        MonthlyLPFreeDelivery monthlyLPFreeDelivery = new MonthlyLPFreeDelivery();
        LowestSizeRule lowestSizeRule = new LowestSizeRule();
        MonthlyTotalDiscountRule monthlyTotalDiscountRule = new MonthlyTotalDiscountRule(monthlyLPFreeDelivery, lowestSizeRule);

        ruleEngine.registerRule(monthlyTotalDiscountRule);
        ruleEngine.registerRule(monthlyLPFreeDelivery);
        ruleEngine.registerRule(lowestSizeRule);

        Flux<String> deliveryInfoFlux = FileOperations.readLines(FileOperations.INPUT_FILE_PATH);
        Mono<List<DeliveryDiscountInfo>> listMono = deliveryInfoFlux
                .map(StringToDeliveryDiscountInfo::convert)
                .map(ruleEngine::rule)
                .doOnNext(s -> logger.info("{}", s))
                .onErrorContinue((throwable, s) -> logger.warn("{} ignored ", s))
                .collectList();
        Flux<DeliveryDiscountInfo> deliveryDiscountInfoFlux = listMono
                .flatMapMany(Flux::fromIterable);
        FileOperations.writeLines(FileOperations.OUTPUT_FILE_PATH, deliveryDiscountInfoFlux);


    }
}
