package com.discountengine.demo.rules;

import com.discountengine.demo.model.DeliveryDiscountInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonthlyTotalDiscountRule implements IRule<DeliveryDiscountInfo, DeliveryDiscountInfo> {

    public static final BigDecimal TOTAL_DISCOUNT = new BigDecimal(10);
    public static Map<String, BigDecimal> monthlyDiscount = new HashMap<>();


    private final List<IRule<DeliveryDiscountInfo, DeliveryDiscountInfo>> dependencies;

    public MonthlyTotalDiscountRule() {
        dependencies = new ArrayList<>();
    }

    public MonthlyTotalDiscountRule add(IRule<DeliveryDiscountInfo, DeliveryDiscountInfo> dependency) {
        dependencies.add(dependency);
        return this;
    }

    public MonthlyTotalDiscountRule add(List<IRule<DeliveryDiscountInfo, DeliveryDiscountInfo>> dependencies) {
        this.dependencies.addAll(dependencies);
        return this;
    }

    public BigDecimal calculateDiscount(LocalDate date, BigDecimal discount) {

        BigDecimal calculatedDiscount;
        String yearMonth = date.getYear() + "" + date.getMonthValue();

        if (monthlyDiscount.get(yearMonth) != null) {
            BigDecimal totalDiscountSoFar = monthlyDiscount.get(yearMonth);
            BigDecimal availableDiscount = TOTAL_DISCOUNT.subtract(totalDiscountSoFar);
            if (availableDiscount.compareTo(discount) == 0 || availableDiscount.compareTo(discount) == 1) {
                calculatedDiscount = discount;
            } else {
                calculatedDiscount = availableDiscount;
            }

            monthlyDiscount.put(yearMonth, monthlyDiscount.get(yearMonth).add(calculatedDiscount));
        } else {

            if (TOTAL_DISCOUNT.compareTo(discount) == 0 || TOTAL_DISCOUNT.compareTo(discount) == 1)
                calculatedDiscount = discount;
            else
                calculatedDiscount = TOTAL_DISCOUNT;

            monthlyDiscount.put(yearMonth, calculatedDiscount);
        }

        return calculatedDiscount;
    }

    @Override
    public boolean matches(DeliveryDiscountInfo input) {

        for (IRule<DeliveryDiscountInfo, DeliveryDiscountInfo> rule :
                this.dependencies) {
            if (rule.matches(input)) {
                BigDecimal discount = this.calculateDiscount(input.getBookingDate(), input.getDiscount());
                input.setDiscountedPrice(input.getOriginalPrice().subtract(discount));
                input.setDiscount(discount);
                input.setMatched(true);
                return true;
            }
        }
        return false;

    }

    @Override
    public DeliveryDiscountInfo process(DeliveryDiscountInfo input) {
        return input;
    }
}
