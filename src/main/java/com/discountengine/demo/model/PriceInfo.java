package com.discountengine.demo.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PriceInfo {

    private String provider;

    private String packageSize;

    private BigDecimal price;

}
