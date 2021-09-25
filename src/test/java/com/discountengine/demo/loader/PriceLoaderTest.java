package com.discountengine.demo.loader;

import com.discountengine.demo.exception.InvalidInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PriceLoaderTest {

    @InjectMocks
    private PriceLoader priceLoader;

    @Test
    public void testGetLowestPriceBySizeSuccessWithSizeS() {
        BigDecimal lowestPrice = priceLoader.getLowestPriceBySize("S");
        Assertions.assertEquals(new BigDecimal(2), lowestPrice);
    }

    @Test
    public void testGetLowestPriceBySizeSuccessWithSizeM() {
        BigDecimal lowestPrice = priceLoader.getLowestPriceBySize("M");
        Assertions.assertEquals(new BigDecimal(3), lowestPrice);
    }

    @Test
    public void testGetLowestPriceBySizeSuccessWithSizeL() {
        BigDecimal lowestPrice = priceLoader.getLowestPriceBySize("L");
        Assertions.assertEquals(new BigDecimal(4), lowestPrice);
    }

    @Test
    public void testGetLowestPriceBySizeExceptionWithInvalidSize() {
        assertThrows(InvalidInputException.class, () -> {
            BigDecimal lowestPrice = priceLoader.getLowestPriceBySize("invalid size");
        });
    }
}
