package com.discountengine.demo.rules;

import com.discountengine.demo.loader.PriceLoader;
import com.discountengine.demo.model.DeliveryDiscountInfo;
import com.discountengine.demo.model.PriceInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class MonthlyLPFreeDeliveryRule implements IRule<DeliveryDiscountInfo, DeliveryDiscountInfo> {

    public final Integer nthFreeDelivery;
    private final String name;
    private final String size;
    private final PriceLoader priceLoader;
    private final Map<String, Integer> monthlyCount;

    public MonthlyLPFreeDeliveryRule(PriceLoader priceLoader) {
        this.nthFreeDelivery = 3;
        this.name = "LP";
        this.size = "L";
        this.monthlyCount = new HashMap<>();
        this.priceLoader = priceLoader;
    }

    public void addMonthlySizeCount(LocalDate date, String size) {
        String yearMonthSize = date.getYear() + "" + date.getMonthValue() + size;
        this.monthlyCount.put(yearMonthSize, this.monthlyCount.getOrDefault(yearMonthSize, 0) + 1);
    }

    public boolean isFreeDelivery(LocalDate date, String size) {
        String yearMonthSize = date.getYear() + "" + date.getMonthValue() + size;
        return this.monthlyCount.get(yearMonthSize).equals(this.nthFreeDelivery);
    }

    @Override
    public boolean matches(DeliveryDiscountInfo input) {
        PriceInfo actualPriceInfo = this.priceLoader.getPriceInfo(input.getCarrierCode(), input.getPackageSize());

        input.setOriginalPrice(actualPriceInfo.getPrice());
        input.setDiscountedPrice(actualPriceInfo.getPrice());
        input.setDiscount(null);

        if (input.getCarrierCode().equalsIgnoreCase(this.name) && input.getPackageSize().equalsIgnoreCase(this.size)) {
            input.setMatched(true);
            this.addMonthlySizeCount(input.getBookingDate(), input.getPackageSize());
            boolean isFreeDelivery = this.isFreeDelivery(input.getBookingDate(), input.getPackageSize());
            if (isFreeDelivery) {
                input.setOriginalPrice(actualPriceInfo.getPrice());
                input.setDiscountedPrice(new BigDecimal(0));
                input.setDiscount(actualPriceInfo.getPrice());
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
