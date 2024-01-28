package org.gojek.cache.base.strategy.impl;

import org.gojek.cache.strategy.impl.LRUCachingStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class LRUCachingStrategyTest {
    private LRUCachingStrategy lruCachingStrategy;

    @BeforeEach
    void initialize() {
        this.lruCachingStrategy = new LRUCachingStrategy();
    }

    @Test
    void put_GivenNoEntryExists_ShouldAddToCache() {
        var entry = Map.entry("key1", "val1");

        lruCachingStrategy.put(entry.getKey(), entry.getValue());
        var result = lruCachingStrategy.get(entry.getKey());

        Assertions.assertEquals(result, entry.getValue());
    }

    @Test
    void put_GivenEntryExists_ShouldUpdateCache() {
        var entry = Map.entry("key1", "val1");
        var updatedEntry = Map.entry("key1", "val1updated");

        lruCachingStrategy.put(entry.getKey(), entry.getValue());
        lruCachingStrategy.put(updatedEntry.getKey(), updatedEntry.getValue());
        var result = lruCachingStrategy.get(updatedEntry.getKey());

        Assertions.assertEquals(result, updatedEntry.getValue());
    }

    @Test
    void get_GivenEntryNotExists_ShouldReturnNull() {
        var result = lruCachingStrategy.get("randomkey");

        Assertions.assertNull(result);
    }

    @Test
    void get_GivenEntryExists_ShouldReturnEntry() {
        var entry = Map.entry("key1", "val1");

        lruCachingStrategy.put(entry.getKey(), entry.getValue());
        var result = lruCachingStrategy.get(entry.getKey());

        Assertions.assertEquals(result, entry.getValue());
    }

    @Test
    void remove_GivenEntryNotExists_ShouldReturnFalse() {
        var result = lruCachingStrategy.remove("randomkey");

        Assertions.assertFalse(result);
    }

    @Test
    void remove_GivenEntryExists_ShouldRemoveEntryFroMCache() {
        var entry = Map.entry("key1", "val1");
        lruCachingStrategy.put(entry.getKey(), entry.getValue());

        var result = lruCachingStrategy.remove(entry.getKey());
        var removalResult = lruCachingStrategy.get(entry.getKey());

        Assertions.assertTrue(result);
        Assertions.assertNull(removalResult);
    }

    @Test
    void evict_GivenEntriesExist_ShouldRemoveTheLeastRecentAccessed() {
        var expectedEvictedKey = "key3";
        var entries = List.of(Map.entry("key1", "val1"),
                Map.entry("key2", "val2"),
                Map.entry("key3", "val3"));
        entries.forEach(entry -> lruCachingStrategy.put(entry.getKey(), entry.getValue()));

        lruCachingStrategy.get("key1");
        lruCachingStrategy.get("key2");
        lruCachingStrategy.get("key3");
        lruCachingStrategy.get("key2");
        lruCachingStrategy.get("key1");
        lruCachingStrategy.evict();

        Assertions.assertNull(lruCachingStrategy.get(expectedEvictedKey));
        Assertions.assertEquals(lruCachingStrategy.get("key2"), "val2");
        Assertions.assertEquals(lruCachingStrategy.get("key1"), "val1");
    }

    @Test
    void evict_GivenEntriesEmpty_ShouldReturnFalse() {
        var result = lruCachingStrategy.evict();

        Assertions.assertFalse(result);
    }
}
