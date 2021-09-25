package com.discountengine.demo.ruleengine;

import com.discountengine.demo.exception.InvalidInputException;
import com.discountengine.demo.loader.PriceLoader;
import com.discountengine.demo.model.DeliveryDiscountInfo;
import com.discountengine.demo.rules.LowestSizeRule;
import com.discountengine.demo.rules.MonthlyLPFreeDeliveryRule;
import com.discountengine.demo.rules.MonthlyTotalDiscountRule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class RuleEngineTest {

    @InjectMocks
    private RuleEngine ruleEngine;

    @Test
    public void testRuleWithEmptyRule() {
        DeliveryDiscountInfo info = DeliveryDiscountInfo.builder()
                .bookingDate(LocalDate.parse("2021-02-01"))
                .carrierCode("MR")
                .packageSize("S")
                .build();
        DeliveryDiscountInfo result = this.ruleEngine.rule(info);
        Assertions.assertNull(result.getDiscount());
    }

    @Test
    public void testRuleWithLowestSizeRule() {
        PriceLoader priceLoader = new PriceLoader();
        LowestSizeRule lowestSizeRule = new LowestSizeRule(priceLoader);
        this.ruleEngine.registerRule(lowestSizeRule);
        DeliveryDiscountInfo info = DeliveryDiscountInfo.builder()
                .bookingDate(LocalDate.parse("2021-02-01"))
                .carrierCode("MR")
                .packageSize("L")
                .build();
        DeliveryDiscountInfo result = this.ruleEngine.rule(info);
        Assertions.assertNull(result.getDiscount());
    }

    @Test
    public void testRuleWithMonthlyLPFreeDeliveryRule() {
        PriceLoader priceLoader = new PriceLoader();
        MonthlyLPFreeDeliveryRule monthlyLPFreeDeliveryRule = new MonthlyLPFreeDeliveryRule(priceLoader);
        this.ruleEngine.registerRule(monthlyLPFreeDeliveryRule);
        DeliveryDiscountInfo info = DeliveryDiscountInfo.builder()
                .bookingDate(LocalDate.parse("2021-02-01"))
                .carrierCode("MR")
                .packageSize("L")
                .build();
        DeliveryDiscountInfo result = this.ruleEngine.rule(info);
        Assertions.assertNull(result.getDiscount());
    }

    @Test
    public void testRuleWithMonthlyTotalDiscountRule() {
        PriceLoader priceLoader = new PriceLoader();
        LowestSizeRule lowestSizeRule = new LowestSizeRule(priceLoader);
        MonthlyLPFreeDeliveryRule monthlyLPFreeDeliveryRule = new MonthlyLPFreeDeliveryRule(priceLoader);
        MonthlyTotalDiscountRule monthlyTotalDiscountRule = new MonthlyTotalDiscountRule();
        monthlyTotalDiscountRule.add(lowestSizeRule);
        monthlyTotalDiscountRule.add(monthlyLPFreeDeliveryRule);
        this.ruleEngine.registerRule(monthlyTotalDiscountRule);
        DeliveryDiscountInfo info = DeliveryDiscountInfo.builder()
                .bookingDate(LocalDate.parse("2021-02-01"))
                .carrierCode("MR")
                .packageSize("L")
                .build();
        DeliveryDiscountInfo result = this.ruleEngine.rule(info);
        Assertions.assertNull(result.getDiscount());
    }

    @Test
    public void testRuleWithMonthlyTotalDiscountRuleInvalidCarrierCode() {
        PriceLoader priceLoader = new PriceLoader();
        LowestSizeRule lowestSizeRule = new LowestSizeRule(priceLoader);
        MonthlyLPFreeDeliveryRule monthlyLPFreeDeliveryRule = new MonthlyLPFreeDeliveryRule(priceLoader);
        MonthlyTotalDiscountRule monthlyTotalDiscountRule = new MonthlyTotalDiscountRule();
        monthlyTotalDiscountRule.add(lowestSizeRule);
        monthlyTotalDiscountRule.add(monthlyLPFreeDeliveryRule);
        this.ruleEngine.registerRule(monthlyTotalDiscountRule);
        DeliveryDiscountInfo info = DeliveryDiscountInfo.builder()
                .bookingDate(LocalDate.parse("2021-02-21"))
                .carrierCode("MRR")
                .packageSize("S")
                .build();
        assertThrows(InvalidInputException.class, () -> {
            DeliveryDiscountInfo result = this.ruleEngine.rule(info);
        });
    }

    @Test
    public void testRuleWithMonthlyTotalDiscountRuleInvalidSize() {
        PriceLoader priceLoader = new PriceLoader();
        LowestSizeRule lowestSizeRule = new LowestSizeRule(priceLoader);
        MonthlyLPFreeDeliveryRule monthlyLPFreeDeliveryRule = new MonthlyLPFreeDeliveryRule(priceLoader);
        MonthlyTotalDiscountRule monthlyTotalDiscountRule = new MonthlyTotalDiscountRule();
        monthlyTotalDiscountRule.add(lowestSizeRule);
        monthlyTotalDiscountRule.add(monthlyLPFreeDeliveryRule);
        this.ruleEngine.registerRule(monthlyTotalDiscountRule);
        DeliveryDiscountInfo info = DeliveryDiscountInfo.builder()
                .bookingDate(LocalDate.parse("2021-02-21"))
                .carrierCode("MR")
                .packageSize("Invalid")
                .build();
        assertThrows(InvalidInputException.class, () -> {
            DeliveryDiscountInfo result = this.ruleEngine.rule(info);
        });
    }
}
