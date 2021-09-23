package com.discountengine.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class FreeDelivery {

    private String provider;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer nThFreeDelivery;

}
