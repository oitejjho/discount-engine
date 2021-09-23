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
    private static BigDecimal lowestPrice = BigDecimal.valueOf(Double.MAX_VALUE);

    public void setLowestPrice() {
        Optional<BigDecimal> lowestPriceOptional = PriceLoader.PRICE_LIST.stream()
                .filter(size -> size.getPackageSize().equalsIgnoreCase("S"))
                .map(price -> price.getPrice())
                .min(Comparator.naturalOrder());

        lowestPrice = lowestPriceOptional.get();
        //todo minPrice isn't found throw exception
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

        if (input.getPackageSize().equalsIgnoreCase("S")) {
            this.setLowestPrice();
            if (!selectedPrice.isPresent()) {
                selectedPrice = Optional.of(new Price(null, null, DEFAULT_MIN));
            }

//            logger.info("original price {}, lowest price {}", selectedPrice.get().getPrice(), lowestPrice);

            //todo selectPrice isn't found throw exception
            input.setOriginalPrice(selectedPrice.get().getPrice());
            return selectedPrice.get().getPrice().compareTo(lowestPrice) == 1;
        }

        return false;

    }

    @Override
    public DeliveryDiscountInfo process(DeliveryDiscountInfo input) {
        input.setDiscountedPrice(lowestPrice.compareTo(DEFAULT_MIN) == 0 ? null : lowestPrice);
        input.setDiscount(input.getOriginalPrice().subtract(lowestPrice));
        return input;
    }
}
