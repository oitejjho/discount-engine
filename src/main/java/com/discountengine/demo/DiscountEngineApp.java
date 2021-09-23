package com.discountengine.demo;

import com.discountengine.demo.model.DeliveryDiscountInfo;
import com.discountengine.demo.ruleengine.RuleEngine;
import com.discountengine.demo.rules.LowestSizeRule;
import com.discountengine.demo.rules.MonthlyLPFreeDelivery;
import com.discountengine.demo.rules.MonthlyTotalDiscountRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DiscountEngineApp {

    private static final Logger logger = LoggerFactory.getLogger(LowestSizeRule.class);

    public static void main(String[] args) {
        /*System.out.println("hello");
        RuleLoader.loadRules("rules.yml");*/

        RuleEngine ruleEngine = new RuleEngine();
        MonthlyLPFreeDelivery monthlyLPFreeDelivery = new MonthlyLPFreeDelivery();
        LowestSizeRule lowestSizeRule = new LowestSizeRule();
        MonthlyTotalDiscountRule monthlyTotalDiscountRule = new MonthlyTotalDiscountRule(monthlyLPFreeDelivery, lowestSizeRule);


        ruleEngine.registerRule(monthlyTotalDiscountRule);
        ruleEngine.registerRule(monthlyLPFreeDelivery);
        ruleEngine.registerRule(lowestSizeRule);

        List<DeliveryDiscountInfo> deliveryDiscountInfoList = new ArrayList<>();
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-01"), "S", "MR", null, null, null, false));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-02"), "S", "MR", null, null, null, false));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-03"), "L", "LP", null, null, null, false));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-05"), "S", "LP", null, null, null, false));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-06"), "S", "MR", null, null, null, false));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-06"), "L", "LP", null, null, null, false));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-07"), "L", "MR", null, null, null, false));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-08"), "M", "MR", null, null, null, false));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-09"), "L", "LP", null, null, null, false));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-10"), "L", "LP", null, null, null, false));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-10"), "S", "MR", null, null, null, false));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-10"), "S", "MR", null, null, null, false));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-11"), "L", "LP", null, null, null, false));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-12"), "M", "MR", null, null, null, false));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-13"), "M", "LP", null, null, null, false));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-15"), "S", "MR", null, null, null, false));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-17"), "L", "LP", null, null, null, false));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-17"), "S", "MR", null, null, null, false));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-24"), "L", "LP", null, null, null, false));
//        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-02-29"), "CUPS", null, null, null, null));
        deliveryDiscountInfoList.add(new DeliveryDiscountInfo(LocalDate.parse("2015-03-01"), "S", "MR", null, null, null, false));

        for (DeliveryDiscountInfo deliveryDiscountInfo :
                deliveryDiscountInfoList) {
            DeliveryDiscountInfo result = ruleEngine.rule(deliveryDiscountInfo);
            logger.info("deliveryDiscountInfo : {}", deliveryDiscountInfo);
        }

    }

    private static void close(Closeable closeable) {
        try {
            closeable.close();
            System.out.println("Closed the resource");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static void write(BufferedWriter bw, String string) {
        try {
            bw.write(string);
            bw.newLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static DeliveryDiscountInfo convert(String line) {
        String[] tokens = line.split(" ");
        DeliveryDiscountInfo deliveryDiscountInfo = new DeliveryDiscountInfo();
        deliveryDiscountInfo.setBookingDate(LocalDate.parse(tokens[0]));
        deliveryDiscountInfo.setPackageSize(tokens[1]);
        deliveryDiscountInfo.setCarrierCode(tokens[2]);
        /*try {

        } catch (ArrayIndexOutOfBoundsException e) {
            int length = tokens.length;

            return deliveryDiscountInfo;
        } catch (DateTimeParseException e) {

        } catch (Exception e) {
            return deliveryDiscountInfo;
        }*/
        return deliveryDiscountInfo;
    }
}
