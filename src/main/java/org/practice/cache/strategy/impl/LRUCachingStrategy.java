package org.practice.cache.strategy.impl;

import org.practice.cache.strategy.CachingStrategy;

import java.util.*;

public class LRUCachingStrategy implements CachingStrategy {

    private final Map<String, Object> keyToValueMap;
    private final List<LRUWrapper> valueIndexWrappers;

    public LRUCachingStrategy() {
        this.keyToValueMap = new HashMap<>();
        this.valueIndexWrappers = new ArrayList<>();
    }

    @Override
    public Object get(String key) {
        if (!keyToValueMap.containsKey(key)) {
            return null;
        }
        var entry = keyToValueMap.get(key);
        var wrappedEntry = new LRUWrapper(key, entry);
        valueIndexWrappers.remove(wrappedEntry);
        valueIndexWrappers.add(wrappedEntry);
        return keyToValueMap.put(key, entry);
    }

    @Override
    public boolean put(String key, Object object) {
        var wrappedValue = new LRUWrapper(key, object);
        if (keyToValueMap.containsKey(key)) {
            valueIndexWrappers.remove(wrappedValue);
        }
        valueIndexWrappers.add(wrappedValue);
        keyToValueMap.put(key, object);
        return true;
    }

    @Override
    public boolean remove(String key) {
        if (!keyToValueMap.containsKey(key)) {
            return false;
        }
        var entry = keyToValueMap.get(key);
        valueIndexWrappers.remove(new LRUWrapper(key, entry));
        keyToValueMap.remove(key);
        return true;
    }

    @Override
    public boolean evict() {
        var wrappedEntry = valueIndexWrappers.stream()
                .findFirst()
                .orElse(null);
        if (Objects.isNull(wrappedEntry)) {
            return false;
        }
        valueIndexWrappers.remove(wrappedEntry);
        keyToValueMap.remove(wrappedEntry.key);
        return true;
    }

    private record LRUWrapper(String key, Object value){

    }
}
