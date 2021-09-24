package com.discountengine.demo.rules;

import com.discountengine.demo.exception.InvalidInputException;
import com.discountengine.demo.loader.PriceLoader;
import com.discountengine.demo.model.DeliveryDiscountInfo;
import com.discountengine.demo.model.PriceInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MonthlyLPFreeDelivery implements IRule<DeliveryDiscountInfo, DeliveryDiscountInfo> {

    public final Integer nthFreeDelivery;
    private final String name;
    private final String size;
    private final PriceLoader priceLoader;
    private final Map<String, Integer> monthlyCount;

    public MonthlyLPFreeDelivery(PriceLoader priceLoader) {
        this.nthFreeDelivery = 3;
        this.name = "LP";
        this.size  = "L";
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
        Optional<PriceInfo> actualPrice = this.priceLoader.getPriceInfos().stream()
                .filter(
                        deliveryInfo -> deliveryInfo.getProvider().equalsIgnoreCase(input.getCarrierCode()) &&
                                deliveryInfo.getPackageSize().equalsIgnoreCase(input.getPackageSize()))
                .findFirst();

        if (!actualPrice.isPresent()) {
            throw new InvalidInputException("Invalid input found");
        }

        input.setOriginalPrice(actualPrice.get().getPrice());
        input.setDiscountedPrice(actualPrice.get().getPrice());
        input.setDiscount(null);

        if (input.getCarrierCode().equalsIgnoreCase(this.name) && input.getPackageSize().equalsIgnoreCase(this.size)) {
            input.setMatched(true);
            this.addMonthlySizeCount(input.getBookingDate(), input.getPackageSize());
            boolean isFreeDelivery = this.isFreeDelivery(input.getBookingDate(), input.getPackageSize());
            if (isFreeDelivery) {

                input.setOriginalPrice(actualPrice.get().getPrice());
                input.setDiscountedPrice(new BigDecimal(0));
                input.setDiscount(actualPrice.get().getPrice());
                return true;
            }
            return false;

        }
        return false;

    }

    @Override
    public DeliveryDiscountInfo process(DeliveryDiscountInfo input) {
        return input;
    }
}
