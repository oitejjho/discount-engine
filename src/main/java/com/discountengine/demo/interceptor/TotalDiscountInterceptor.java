package com.discountengine.demo.interceptor;

import com.discountengine.demo.rules.IRule;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TotalDiscountInterceptor implements InvocationHandler {

    private IRule rule;

    public TotalDiscountInterceptor (IRule rule) {
        this.rule = rule;
    }

    @Override
    public Object invoke (Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before method call : " + method.getName());
        Object result = method.invoke(rule, args);
        System.out.println("after method call : " + method.getName());
        return result;
    }
}
