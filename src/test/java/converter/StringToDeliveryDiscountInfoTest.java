package converter;

import com.discountengine.demo.converter.StringToDeliveryDiscountInfo;
import com.discountengine.demo.model.DeliveryDiscountInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class StringToDeliveryDiscountInfoTest {

    @InjectMocks
    private StringToDeliveryDiscountInfo stringToDeliveryDiscountInfo;

    @Test
    public void testConvertSuccess() {
        String deliverDiscountInfoString = "2021-02-01 S MR";
        DeliveryDiscountInfo info = stringToDeliveryDiscountInfo.convert(deliverDiscountInfoString);
        Assertions.assertEquals(LocalDate.parse("2021-02-01"), info.getBookingDate());
        Assertions.assertEquals("S", info.getPackageSize());
        Assertions.assertEquals("MR", info.getCarrierCode());
    }

    @Test
    public void testConvertWithInvalidDate() {
        String deliverDiscountInfoString = "2021-02-29 S MR";
        assertThrows(DateTimeParseException.class, () -> {
            DeliveryDiscountInfo info = stringToDeliveryDiscountInfo.convert(deliverDiscountInfoString);
        });
    }

    @Test
    public void testConvertWithInvalidInput() {
        String deliverDiscountInfoString = "2021-02-28 S";
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            DeliveryDiscountInfo info = stringToDeliveryDiscountInfo.convert(deliverDiscountInfoString);
        });
    }
}
