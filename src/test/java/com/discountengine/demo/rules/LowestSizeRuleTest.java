package com.discountengine.demo.rules;

import com.discountengine.demo.exception.InvalidInputException;
import com.discountengine.demo.loader.PriceLoader;
import com.discountengine.demo.model.DeliveryDiscountInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class LowestSizeRuleTest {

    private LowestSizeRule lowestSizeRule;

    @BeforeEach
    public void setUp() {
        PriceLoader priceLoader = new PriceLoader();
        this.lowestSizeRule = new LowestSizeRule(priceLoader);
    }

    @Test
    public void testMatchesSuccess() {
        DeliveryDiscountInfo info = DeliveryDiscountInfo.builder()
                .bookingDate(LocalDate.parse("2021-02-01"))
                .carrierCode("MR")
                .packageSize("S")
                .build();
        Assertions.assertTrue(this.lowestSizeRule.matches(info));
    }

    @Test
    public void testMatchesSuccessInvalidCarrierCode() {
        DeliveryDiscountInfo info = DeliveryDiscountInfo.builder()
                .bookingDate(LocalDate.parse("2021-02-01"))
                .carrierCode("MRR")
                .packageSize("S")
                .build();
        assertThrows(InvalidInputException.class, () -> {
            Assertions.assertFalse(this.lowestSizeRule.matches(info));
        });
    }

    @Test
    public void testMatchesSuccessInvalidSize() {
        DeliveryDiscountInfo info = DeliveryDiscountInfo.builder()
                .bookingDate(LocalDate.parse("2021-02-01"))
                .carrierCode("MR")
                .packageSize("SL")
                .build();
        Assertions.assertFalse(this.lowestSizeRule.matches(info));
    }
}
