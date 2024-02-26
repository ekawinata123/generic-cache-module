package org.practice.cache.base.strategy.impl;

import org.practice.cache.strategy.impl.LFUCachingStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class LFUCachingStrategyTest {

    private LFUCachingStrategy lfuCachingStrategy;

    @BeforeEach
    void initialize() {
        this.lfuCachingStrategy = new LFUCachingStrategy();
    }

    @Test
    void put_GivenEntryNotExists_ShouldStoreToCache() {
        var storedObjects = List.of(Map.entry("key1", "value1"), Map.entry("key2", "value2"));

        storedObjects.forEach(entry -> {
            lfuCachingStrategy.put(entry.getKey(), entry.getValue());
        });

        storedObjects.forEach(entry -> {
            Assertions.assertEquals(lfuCachingStrategy.get(entry.getKey()), entry.getValue());
        });
    }

    @Test
    void put_GivenEntryWithKeyExists_ShouldUpdateTheValue() {
        var keyValuePair = Map.entry("key1", "value1");
        var updatedKeyValuePair = Map.entry("key1", "value1_rev1");
        lfuCachingStrategy.put(keyValuePair.getKey(), keyValuePair.getValue());

        lfuCachingStrategy.put(updatedKeyValuePair.getKey(), updatedKeyValuePair.getValue());

        Assertions.assertEquals(lfuCachingStrategy.get(keyValuePair.getKey()), updatedKeyValuePair.getValue());
    }

    @Test
    void remove_GivenEntryExists_ShouldRemoveFromCache() {
        var keyValuePair = Map.entry("key1", "value1");
        lfuCachingStrategy.put(keyValuePair.getKey(), keyValuePair.getValue());

        lfuCachingStrategy.remove(keyValuePair.getKey());

        Assertions.assertNull(lfuCachingStrategy.get(keyValuePair.getKey()));
    }

    @Test
    void evict_GivenEntryWithLeastFrequencyCount_ShouldEvicted() {
        var keyValuePair1 = Map.entry("key1", "value1");
        var keyValuePair2 = Map.entry("key2", "value2");
        lfuCachingStrategy.put(keyValuePair1.getKey(), keyValuePair1.getValue());
        lfuCachingStrategy.put(keyValuePair2.getKey(), keyValuePair2.getValue());
        lfuCachingStrategy.get(keyValuePair2.getKey());
        lfuCachingStrategy.get(keyValuePair2.getKey());
        lfuCachingStrategy.get(keyValuePair1.getKey());

        lfuCachingStrategy.evict();
        var evictedEntry = lfuCachingStrategy.get(keyValuePair1.getKey());
        var nonEvictedEntry = lfuCachingStrategy.get(keyValuePair2.getKey());

        Assertions.assertNull(evictedEntry);
        Assertions.assertEquals(nonEvictedEntry, keyValuePair2.getValue());
    }

    @Test
    void evict_GivenEntryIsEmpty_ShouldReturnFalse() {
        var result = lfuCachingStrategy.evict();

        Assertions.assertFalse(result);
    }
}
