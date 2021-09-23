package com.discountengine.demo;

import com.discountengine.demo.model.DeliveryDiscountInfo;
import com.discountengine.demo.ruleengine.RuleEngine;
import com.discountengine.demo.rules.LowestSizeRule;
import com.discountengine.demo.rules.MonthlyLPFreeDelivery;
import com.discountengine.demo.rules.MonthlyTotalDiscountRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiscountEngineApp {

    private static final Logger logger = LoggerFactory.getLogger(LowestSizeRule.class);

    public static void main(String[] args) {
        /*System.out.println("hello");
        RuleLoader.loadRules("rules.yml");*/

        RuleEngine ruleEngine = new RuleEngine();
        MonthlyLPFreeDelivery monthlyLPFreeDelivery = new MonthlyLPFreeDelivery();
        LowestSizeRule lowestSizeRule = new LowestSizeRule();
        MonthlyTotalDiscountRule monthlyTotalDiscountRule = new MonthlyTotalDiscountRule(monthlyLPFreeDelivery , lowestSizeRule);


        ruleEngine.registerRule(monthlyTotalDiscountRule);
        ruleEngine.registerRule(monthlyLPFreeDelivery);
        ruleEngine.registerRule(lowestSizeRule);
//        ruleEngine.registerRule();

        List<DeliveryDiscountInfo> deliveryDiscountInfoList = new ArrayList<>();
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-01"), "S", "MR", null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-02"), "S", "MR", null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-03"), "L", "LP", null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-05"), "S", "LP", null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-06"), "S", "MR", null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-06"), "L", "LP", null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-07"), "L", "MR", null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-08"), "M", "MR", null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-09"), "L", "LP", null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-10"), "L", "LP", null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-10"), "S", "MR", null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-10"), "S", "MR", null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-11"), "L", "LP", null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-12"), "M", "MR", null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-13"), "M", "LP", null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-15"), "S", "MR", null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-17"), "L", "LP", null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-17"), "S", "MR", null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-24"), "L", "LP", null, null, null));
//        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-28"), "CUPS", null, null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-03-01"), "S", "MR", null, null, null));

        for (DeliveryDiscountInfo deliveryDiscountInfo :
                deliveryDiscountInfoList) {
            DeliveryDiscountInfo result = ruleEngine.rule(deliveryDiscountInfo);
            logger.info("deliveryDiscountInfo : {}", deliveryDiscountInfo);
        }
        /*List<DeliveryDiscountInfo> result = deliveryDiscountInfoList.stream()
                .map(deliveryDiscountInfo -> ruleEngine.rule(deliveryDiscountInfo))
                .collect(Collectors.toList());

        result.forEach(deliveryDiscountInfo -> logger.info("deliveryDiscountInfo : {}", deliveryDiscountInfo));*/


        /*System.out.println("Load prices");
        List<Price> priceList = PriceLoader.loadRules("prices.yml");
        System.out.println("Accessing first element: " + priceList.size());

        DeliveryInfo info = DeliveryInfo.builder()
                .carrierCode("MR")
                .build();
        List<DeliveryInfo> infos = new ArrayList<>();
        infos.add(info);

        Map<String, List<DeliveryInfo>> stringListMap = infos.stream().collect(Collectors.groupingBy(DeliveryInfo::getCarrierCode));
        Map<String, List<Price>> listMap = priceList.stream().collect(Collectors.groupingBy(Price::getProvider));
        System.out.println(stringListMap.size());*/

    }
}
