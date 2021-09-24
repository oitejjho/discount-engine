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

    public static final Integer NTH_FREE_DELIVERY = 3;
    private static final String NAME = "LP";
    private static final String SIZE = "L";
    private Map<String, Integer> monthlyCount;

    public MonthlyLPFreeDelivery() {
        this.monthlyCount = new HashMap<>();
    }

    public void addMonthlySizeCount(LocalDate date, String size) {
        String yearMonthSize = date.getYear() + "" + date.getMonthValue() + size;
        monthlyCount.put(yearMonthSize, monthlyCount.getOrDefault(yearMonthSize, 0) + 1);
    }

    public boolean isFreeDelivery(LocalDate date, String size) {
        String yearMonthSize = date.getYear() + "" + date.getMonthValue() + size;
        return monthlyCount.get(yearMonthSize).equals(NTH_FREE_DELIVERY);
    }

    @Override
    public boolean matches(DeliveryDiscountInfo input) {
        PriceLoader priceLoader = new PriceLoader();
        Optional<PriceInfo> actualPrice = priceLoader.getPriceInfos().stream()
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

        if (input.getCarrierCode().equalsIgnoreCase(NAME) && input.getPackageSize().equalsIgnoreCase(SIZE)) {
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
