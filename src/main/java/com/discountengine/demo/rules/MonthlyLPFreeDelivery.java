package com.discountengine.demo.rules;

import com.discountengine.demo.loader.PriceLoader;
import com.discountengine.demo.model.DeliveryDiscountInfo;
import com.discountengine.demo.model.PriceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MonthlyLPFreeDelivery implements IRule<DeliveryDiscountInfo, DeliveryDiscountInfo> {

    public static final Integer NTH_FREE_DELIVERY = 3;
    private static final Logger logger = LoggerFactory.getLogger(MonthlyLPFreeDelivery.class);
    private static final String NAME = "LP";
    private static final String SIZE = "L";
    private static final BigDecimal DEFAULT_MIN = BigDecimal.valueOf(Double.MIN_VALUE);
    public static Map<String, Integer> monthlyCount;

    static {
        monthlyCount = new HashMap<>();
    }

    public void addMonthlySizeCount(LocalDate date, String size) {
        String yearMonthSize = date.getYear() + "" + date.getMonthValue() + size;
        monthlyCount.put(yearMonthSize, monthlyCount.getOrDefault(yearMonthSize, 0) + 1);
    }

    public boolean isFreeDelivery(LocalDate date, String size) {
        String yearMonthSize = date.getYear() + "" + date.getMonthValue() + size;
        return monthlyCount.get(yearMonthSize) == NTH_FREE_DELIVERY;
    }

    @Override
    public boolean matches(DeliveryDiscountInfo input) {
        PriceLoader priceLoader = new PriceLoader();
        Optional<PriceInfo> selectedPrice = priceLoader.getPriceInfos().stream()
                .filter(
                        deliveryInfo -> deliveryInfo.getProvider().equalsIgnoreCase(input.getCarrierCode()) &&
                                deliveryInfo.getPackageSize().equalsIgnoreCase(input.getPackageSize()))
                .findFirst();
        input.setOriginalPrice(selectedPrice.get().getPrice());
        input.setDiscountedPrice(selectedPrice.get().getPrice());
        input.setDiscount(null);

        if (input.getCarrierCode().equalsIgnoreCase(NAME) && input.getPackageSize().equalsIgnoreCase(SIZE)) {
            input.setMatched(true);
            this.addMonthlySizeCount(input.getBookingDate(), input.getPackageSize());
            boolean isFreeDelivery = this.isFreeDelivery(input.getBookingDate(), input.getPackageSize());
            if (isFreeDelivery) {

                if (!selectedPrice.isPresent()) {
                    selectedPrice = Optional.of(new PriceInfo(null, null, DEFAULT_MIN));
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
