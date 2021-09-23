package com.discountengine.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class DeliveryInfo {

    private LocalDate bookingDate;

    private String packageSize;

    private String carrierCode;

    private BigDecimal originalPrice;
}
