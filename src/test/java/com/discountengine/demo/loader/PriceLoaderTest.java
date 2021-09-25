package com.discountengine.demo.loader;

import com.discountengine.demo.exception.InvalidInputException;
import com.discountengine.demo.model.PriceInfo;
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
        Assertions.assertNotNull(lowestPrice);
    }

    @Test
    public void testGetLowestPriceBySizeExceptionWithInvalidSize() {
        assertThrows(InvalidInputException.class, () -> {
            BigDecimal lowestPrice = priceLoader.getLowestPriceBySize("invalid size");
        });
    }

    @Test
    public void testGetPriceInfo(){
        PriceInfo priceInfo = priceLoader.getPriceInfo("MR","S");
        Assertions.assertNotNull(priceInfo);
    }

    @Test
    public void testGetPriceInfoExceptionWithInvalidSize() {
        assertThrows(InvalidInputException.class, () -> {
            PriceInfo priceInfo = priceLoader.getPriceInfo("MRR","S");
        });
    }
}
