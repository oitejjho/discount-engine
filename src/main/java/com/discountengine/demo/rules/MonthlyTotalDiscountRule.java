/*
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

public class MonthlyTotalDiscountRule implements IRule<DeliveryDiscountInfo, DeliveryDiscountInfo> {

    private static final Logger logger = LoggerFactory.getLogger(MonthlyTotalDiscountRule.class);
    public static final BigDecimal TOTAL_DISCOUNT = new BigDecimal(10);
    public static Map<String, BigDecimal> monthlyDiscount;

    static {
        monthlyDiscount = new HashMap<>();
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
                        deliveryInfo -> deliveryInfo.getProvider().equalsIgnoreCase(NAME) &&
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
//                logger.info("original price {}", selectedPrice.get().getPrice());

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
*/
