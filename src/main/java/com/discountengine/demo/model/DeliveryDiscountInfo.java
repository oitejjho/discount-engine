package com.discountengine.demo.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class DeliveryDiscountInfo {

    private LocalDate bookingDate;

    private String packageSize;

    private String carrierCode;

    private BigDecimal originalPrice;

    private BigDecimal discountedPrice;

    private BigDecimal discount;
}
