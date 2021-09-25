package com.discountengine.demo.loader;

import com.discountengine.demo.exception.InvalidInputException;
import com.discountengine.demo.model.PriceInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class PriceLoader {

    private final String priceFileName;
    private List<PriceInfo> priceInfo;
    private Map<String, List<PriceInfo>> providers;

    public PriceLoader() {
        this.priceFileName = "prices.yml";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(classLoader.getResource(this.priceFileName).getFile());

        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

        try {
            this.priceInfo = objectMapper.readValue(file, new TypeReference<>() {
            });
            this.providers = this.priceInfo.stream().collect(Collectors.groupingBy(PriceInfo::getProvider));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PriceInfo getPriceInfo(String providerName, String size) {
        if (this.providers.get(providerName) == null || this.providers.get(providerName).isEmpty())
            throw new InvalidInputException("Invalid provider found");

        return this.providers.get(providerName).stream()
                .filter(priceInfo -> priceInfo.getPackageSize().equalsIgnoreCase(size))
                .findFirst()
                .orElseThrow(() -> new InvalidInputException("Invalid size found"));
    }

    public BigDecimal getLowestPriceBySize(String size) {
        return this.priceInfo.stream()
                .filter(priceInfo -> priceInfo.getPackageSize().equalsIgnoreCase(size))
                .map(PriceInfo::getPrice)
                .min(Comparator.naturalOrder())
                .orElseThrow(() -> new InvalidInputException("Invalid size found"));
    }

}
