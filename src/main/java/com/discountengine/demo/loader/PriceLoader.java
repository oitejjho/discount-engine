package com.discountengine.demo.loader;

import com.discountengine.demo.model.Price;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PriceLoader {

    public static final String PRICE_FILE = "prices.yml";
    public static List<Price> PRICE_LIST;
    public static Map<String, List<Price>> PROVIDERS;

    static {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(classLoader.getResource(PRICE_FILE).getFile());

        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());


        try {
            PRICE_LIST = objectMapper.readValue(file, new TypeReference<List<Price>>() {
            });
            Map<String, List<Price>> PROVIDERS = PRICE_LIST.stream().collect(Collectors.groupingBy(Price::getProvider));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
