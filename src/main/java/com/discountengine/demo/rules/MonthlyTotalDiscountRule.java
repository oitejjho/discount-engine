package com.discountengine.demo.rules;

import com.discountengine.demo.model.DeliveryDiscountInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonthlyTotalDiscountRule implements IRule<DeliveryDiscountInfo, DeliveryDiscountInfo> {

    private final BigDecimal totalDiscount;
    private final Map<String, BigDecimal> monthlyDiscount;
    private final List<IRule<DeliveryDiscountInfo, DeliveryDiscountInfo>> dependencies;

    public MonthlyTotalDiscountRule() {
        this.totalDiscount = new BigDecimal(10);
        this.monthlyDiscount = new HashMap<>();
        this.dependencies = new ArrayList<>();
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

        if (this.monthlyDiscount.get(yearMonth) != null) {
            BigDecimal totalDiscountSoFar = this.monthlyDiscount.get(yearMonth);
            BigDecimal availableDiscount = this.totalDiscount.subtract(totalDiscountSoFar);
            if (availableDiscount.compareTo(discount) == 0 || availableDiscount.compareTo(discount) > 0) {
                calculatedDiscount = discount;
            } else {
                calculatedDiscount = availableDiscount;
            }

            this.monthlyDiscount.put(yearMonth, this.monthlyDiscount.get(yearMonth).add(calculatedDiscount));
        } else {

            if (this.totalDiscount.compareTo(discount) == 0 || this.totalDiscount.compareTo(discount) > 0)
                calculatedDiscount = discount;
            else
                calculatedDiscount = this.totalDiscount;

            this.monthlyDiscount.put(yearMonth, calculatedDiscount);
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
