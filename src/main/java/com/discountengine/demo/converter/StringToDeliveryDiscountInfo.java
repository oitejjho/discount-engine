package com.discountengine.demo.converter;

import com.discountengine.demo.model.DeliveryDiscountInfo;

import java.time.LocalDate;

public class StringToDeliveryDiscountInfo {

    public static final String SPLITTER = " ";

    public DeliveryDiscountInfo convert(String deliveryDiscountInfoString) {
        String[] tokens = deliveryDiscountInfoString.split(SPLITTER);
        DeliveryDiscountInfo deliveryDiscountInfo = new DeliveryDiscountInfo();
        deliveryDiscountInfo.setBookingDate(LocalDate.parse(tokens[0]));
        deliveryDiscountInfo.setPackageSize(tokens[1]);
        deliveryDiscountInfo.setCarrierCode(tokens[2]);
        return deliveryDiscountInfo;
    }
}
