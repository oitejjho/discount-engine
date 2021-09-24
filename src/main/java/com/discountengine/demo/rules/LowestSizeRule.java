package com.discountengine.demo.rules;

import com.discountengine.demo.loader.PriceLoader;
import com.discountengine.demo.model.DeliveryDiscountInfo;
import com.discountengine.demo.model.PriceInfo;

import java.math.BigDecimal;

public class LowestSizeRule implements IRule<DeliveryDiscountInfo, DeliveryDiscountInfo> {

    private final PriceLoader priceLoader;

    public LowestSizeRule(PriceLoader priceLoader) {
        this.priceLoader = priceLoader;
    }

    @Override
    public boolean matches(DeliveryDiscountInfo input) {
        if (input.getPackageSize().equalsIgnoreCase("S")) {
            PriceInfo actualPriceInfo = this.priceLoader.getPriceInfo(input.getCarrierCode(), input.getPackageSize());

            input.setOriginalPrice(actualPriceInfo.getPrice());
            input.setDiscountedPrice(actualPriceInfo.getPrice());
            input.setDiscount(null);

            BigDecimal lowestPrice = this.priceLoader.getLowestPriceBySize("S");

            if (actualPriceInfo.getPrice().compareTo(lowestPrice) > 0) {
                input.setDiscountedPrice(lowestPrice);
                input.setDiscount(input.getOriginalPrice().subtract(lowestPrice));
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
