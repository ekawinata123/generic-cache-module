package org.gojek.base.strategy;

import org.gojek.cache.base.GenericCache;
import org.gojek.cache.strategy.impl.LRUCachingStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class GenericCacheTest {

    private GenericCache genericCache;

    @BeforeEach
    void initialize() {
        LRUCachingStrategy lruCachingStrategy = new LRUCachingStrategy();
        genericCache = new GenericCache(1, lruCachingStrategy);
    }

    @Test
    void get_GivenEntryExists_ShouldReturnValue() {
        var entry = Map.entry("val1", "test1");
        genericCache.put(entry.getKey() , entry.getValue());

        var result = genericCache.get(entry.getKey(), String.class);

        Assertions.assertEquals(result, entry.getValue());
    }

    @Test
    void get_GivenEntryNotExists_ShouldReturnNull() {
        var result = genericCache.get("randomkey", String.class);

        Assertions.assertNull(result);
    }

    @Test
    void remove_GivenEntryExists_ShouldReturnTrue() {
        var entry = Map.entry("key1", "val1");
        genericCache.put(entry.getKey(), entry.getValue());

        var result = genericCache.remove(entry.getKey());
        var updatedEntry = genericCache.get(entry.getKey(), String.class);

        Assertions.assertTrue(result);
        Assertions.assertNull(updatedEntry);
    }

    @Test
    void put_GivenCacheCapacityAvailable_ShouldRegisterToCache() {
        var entry = Map.entry("key1", "val1");

        genericCache.put(entry.getKey(), entry.getValue());
        var result = genericCache.get(entry.getKey(), String.class);

        Assertions.assertEquals(result, entry.getValue());
    }

    @Test
    void put_GivenCacheCapacityFull_ShouldEvictEntryAndRegisterValue() {
        var existingEntry = Map.entry("key1", "val1");
        var newEntry = Map.entry("key2", "val2");
        genericCache.put(existingEntry.getKey(), existingEntry.getValue());

        var result = genericCache.put(newEntry.getKey(), newEntry.getValue());

        Assertions.assertTrue(result);
        Assertions.assertNull(genericCache.get(existingEntry.getKey(), String.class));
    }
}
