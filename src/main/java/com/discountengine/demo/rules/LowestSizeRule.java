package com.discountengine.demo.rules;

import com.discountengine.demo.loader.PriceLoader;
import com.discountengine.demo.model.DeliveryDiscountInfo;
import com.discountengine.demo.model.PriceInfo;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Optional;

public class LowestSizeRule implements IRule<DeliveryDiscountInfo, DeliveryDiscountInfo> {

    private final PriceLoader priceLoader;
    private final BigDecimal lowestPrice;

    public LowestSizeRule(PriceLoader priceLoader) {
        this.priceLoader = priceLoader;
        Optional<BigDecimal> lowestPriceOptional = priceLoader.getPriceInfos().stream()
                .filter(size -> size.getPackageSize().equalsIgnoreCase("S"))
                .map(PriceInfo::getPrice)
                .min(Comparator.naturalOrder());
        lowestPrice = lowestPriceOptional.isPresent() ? lowestPriceOptional.get() : BigDecimal.valueOf(Double.MAX_VALUE);
    }

    @Override
    public boolean matches(DeliveryDiscountInfo input) {
        if (input.getPackageSize().equalsIgnoreCase("S")) {
            PriceInfo actualPriceInfo = this.priceLoader.getPriceInfo(input.getCarrierCode(), input.getPackageSize());

            input.setOriginalPrice(actualPriceInfo.getPrice());
            input.setDiscountedPrice(actualPriceInfo.getPrice());
            input.setDiscount(null);

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
