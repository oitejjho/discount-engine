package com.discountengine.demo.model;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDiscountInfo {

    private LocalDate bookingDate;

    private String packageSize;

    private String carrierCode;

    private BigDecimal originalPrice;

    private BigDecimal discountedPrice;

    private BigDecimal discount;

    private boolean matched = false;

    @Override
    public String toString() {
        return
                bookingDate +
                        " " + packageSize +
                        " " + carrierCode +
                        " " + (discountedPrice != null ? discountedPrice.setScale(2, RoundingMode.CEILING) : "-") +
                        " " + (discount != null ? discount.setScale(2, RoundingMode.CEILING) : "-");
    }
}
