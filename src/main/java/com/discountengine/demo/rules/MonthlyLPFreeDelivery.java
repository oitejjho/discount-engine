package com.discountengine.demo.rules;

import com.discountengine.demo.loader.PriceLoader;
import com.discountengine.demo.model.DeliveryDiscountInfo;
import com.discountengine.demo.model.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MonthlyLPFreeDelivery implements IRule<DeliveryDiscountInfo, DeliveryDiscountInfo> {

    private static final Logger logger = LoggerFactory.getLogger(MonthlyLPFreeDelivery.class);
    public static final Integer NTH_FREE_DELIVERY = 3;
    private static final String NAME = "LP";
    private static final String SIZE = "L";
    private static final BigDecimal DEFAULT_MIN = BigDecimal.valueOf(Double.MIN_VALUE);
    public static Map<String, Integer> monthlyCount;

    static {
        monthlyCount = new HashMap<>();
    }

    public void addMonthlyCount(LocalDate date) {
        String yearMonth = date.getYear() + "" + date.getMonthValue();
        monthlyCount.put(yearMonth, monthlyCount.getOrDefault(yearMonth, 0) + 1);
    }

    public boolean isFreeDelivery(LocalDate date) {
        String yearMonth = date.getYear() + "" + date.getMonthValue();
        return monthlyCount.get(yearMonth) == NTH_FREE_DELIVERY;
    }

    @Override
    public boolean matches(DeliveryDiscountInfo input) {

        Optional<Price> selectedPrice = PriceLoader.PRICE_LIST.stream()
                .filter(
                        deliveryInfo -> deliveryInfo.getProvider().equalsIgnoreCase(input.getCarrierCode()) &&
                                deliveryInfo.getPackageSize().equalsIgnoreCase(input.getPackageSize()))
                .findFirst();
        input.setOriginalPrice(selectedPrice.get().getPrice());
        input.setDiscountedPrice(selectedPrice.get().getPrice());
        input.setDiscount(null);

        if (input.getCarrierCode().equalsIgnoreCase(NAME) && input.getPackageSize().equalsIgnoreCase(SIZE)) {
            this.addMonthlyCount(input.getBookingDate());
            boolean isFreeDelivery = this.isFreeDelivery(input.getBookingDate());
            if (isFreeDelivery) {

                if (!selectedPrice.isPresent()) {
                    selectedPrice = Optional.of(new Price(null, null, DEFAULT_MIN));
                }

                //todo selectPrice isn't found throw exception
                input.setOriginalPrice(selectedPrice.get().getPrice());
                input.setDiscountedPrice(new BigDecimal(0));
                input.setDiscount(selectedPrice.get().getPrice());
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
