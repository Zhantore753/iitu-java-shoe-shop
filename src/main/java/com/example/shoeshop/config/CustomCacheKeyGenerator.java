package com.example.shoeshop.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Custom key generator for caching to avoid using complex entity objects as keys
 */
@Component
public class CustomCacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return target.getClass().getSimpleName() + "_" +
                method.getName() + "_" +
                Arrays.toString(params);
    }
}