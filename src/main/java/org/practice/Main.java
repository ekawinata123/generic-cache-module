package org.practice;

import org.practice.cache.base.GenericCache;
import org.practice.cache.strategy.CachingStrategy;
import org.practice.cache.strategy.impl.LFUCachingStrategy;

public class Main {
    public static void main(String[] args) {
        CachingStrategy cachingStrategy = new LFUCachingStrategy();
        GenericCache<Object> genericCache = new GenericCache<>(
                3,
                cachingStrategy
        );

        genericCache.put("test1", "val1");
        genericCache.put("test2", "val2");
        genericCache.put("test3", "val3");
        genericCache.get("test2", Object.class);
        genericCache.get("test2", Object.class);
        genericCache.get("test2", Object.class);
        genericCache.get("test2", Object.class);
        genericCache.get("test3", Object.class);
        genericCache.get("test3", Object.class);
        genericCache.put("test4", "val4");
        System.out.println("test");
    }
}       