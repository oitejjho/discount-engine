package com.discountengine.demo.loader;

import com.discountengine.demo.model.PriceInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class PriceLoader {

    private String priceFileName = "prices.yml";
    private List<PriceInfo> priceInfos;
    private Map<String, List<PriceInfo>> providers;

    public PriceLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(classLoader.getResource(this.priceFileName).getFile());

        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

        try {
            this.priceInfos = objectMapper.readValue(file, new TypeReference<List<PriceInfo>>() {
            });
            this.providers = this.priceInfos.stream().collect(Collectors.groupingBy(PriceInfo::getProvider));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
