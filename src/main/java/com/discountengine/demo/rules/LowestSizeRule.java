package com.discountengine.demo.rules;

import com.discountengine.demo.loader.PriceLoader;
import com.discountengine.demo.model.DeliveryDiscountInfo;
import com.discountengine.demo.model.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Optional;

public class LowestSizeRule implements IRule<DeliveryDiscountInfo, DeliveryDiscountInfo> {

    private static final Logger logger = LoggerFactory.getLogger(LowestSizeRule.class);
    private static final BigDecimal DEFAULT_MIN = BigDecimal.valueOf(Double.MIN_VALUE);
    private static final BigDecimal lowestPrice;

    static {
        Optional<BigDecimal> lowestPriceOptional = PriceLoader.PRICE_LIST.stream()
                .filter(size -> size.getPackageSize().equalsIgnoreCase("S"))
                .map(price -> price.getPrice())
                .min(Comparator.naturalOrder());

        lowestPrice = lowestPriceOptional.isPresent() ? lowestPriceOptional.get() : BigDecimal.valueOf(Double.MAX_VALUE);
    }

    @Override
    public boolean matches(DeliveryDiscountInfo input) {

        Optional<Price> actualPrice = PriceLoader.PRICE_LIST.stream()
                .filter(
                        deliveryInfo -> deliveryInfo.getProvider().equalsIgnoreCase(input.getCarrierCode()) &&
                                deliveryInfo.getPackageSize().equalsIgnoreCase(input.getPackageSize()))
                .findFirst();
        input.setOriginalPrice(actualPrice.get().getPrice());
        input.setDiscountedPrice(actualPrice.get().getPrice());
        input.setDiscount(null);

        if(input.getPackageSize().equalsIgnoreCase("S") && actualPrice.get().getPrice().compareTo(lowestPrice) == 1){
            input.setDiscountedPrice(lowestPrice.compareTo(DEFAULT_MIN) == 0 ? null : lowestPrice);
            input.setDiscount(input.getOriginalPrice().subtract(lowestPrice));
            return true;
        }

        return false;

    }

    @Override
    public DeliveryDiscountInfo process(DeliveryDiscountInfo input) {
        return input;
    }
}
