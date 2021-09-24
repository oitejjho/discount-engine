package com.discountengine.demo;

import com.discountengine.demo.manager.RuleEngineManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DiscountEngineApp {

    private static final Logger logger = LoggerFactory.getLogger(DiscountEngineApp.class);

    public static void main(String[] args) throws IOException {

        RuleEngineManager.applyRules();


    }
}
