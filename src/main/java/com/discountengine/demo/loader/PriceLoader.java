package com.discountengine.demo.loader;

import com.discountengine.demo.model.Price;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PriceLoader {

    public static final String PRICE_FILE = "prices.yml";
    public static List<Price> PRICE_LIST = new ArrayList<>();

    static {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(classLoader.getResource(PRICE_FILE).getFile());

        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());


        try {
            PRICE_LIST = objectMapper.readValue(file, new TypeReference<List<Price>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
