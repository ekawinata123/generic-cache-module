package org.practice.cache.strategy.impl;

import org.practice.cache.strategy.CachingStrategy;

import java.util.*;

public class LFUCachingStrategy implements CachingStrategy {
    private final Map<String, Object> keyToValueMap;
    private final Map<String, Integer> keyToFrequencyMap;
    private final TreeMap<Integer, LinkedHashSet<String>> frequencyToKeysMap;

    public LFUCachingStrategy() {
        keyToValueMap = new HashMap<>();
        keyToFrequencyMap = new HashMap<>();
        frequencyToKeysMap = new TreeMap<>();
    }


    @Override
    public Object get(String key) {
        if (keyToValueMap.containsKey(key)) {
            recomputeAccessFrequency(key);
            return keyToValueMap.get(key);
        }
        return null;
    }

    @Override
    public boolean put(String key, Object object) {
        if (keyToValueMap.containsKey(key)) {
            keyToValueMap.put(key, object);
            return true;
        }
        recomputeAccessFrequency(key);
        keyToValueMap.put(key, object);
        return true;
    }

    @Override
    public boolean remove(String key) {
        if (!keyToValueMap.containsKey(key)) {
            return false;
        }
        keyToValueMap.remove(key);
        int frequency = keyToFrequencyMap.remove(key);
        frequencyToKeysMap.computeIfPresent(frequency, (k, v) -> {
            v.remove(key);
            return v;
        });
        return true;
    }

    @Override
    public boolean evict() {
        var keyToEvict = frequencyToKeysMap.entrySet()
                .stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .findFirst()
                .map(Map.Entry::getValue)
                .map(linkedHasSet -> linkedHasSet.stream()
                        .findFirst()
                        .orElse(null))
                .orElse(null);

        if (Objects.isNull(keyToEvict)) {
            return false;
        }
        keyToValueMap.remove(keyToEvict);
        int frequency = keyToFrequencyMap.remove(keyToEvict);
        frequencyToKeysMap.computeIfPresent(frequency, (k,v) -> {
            v.remove(keyToEvict);
            return v;
        });
        return true;
    }

    private void recomputeAccessFrequency(String key) {
        int currentFrequency = keyToFrequencyMap.getOrDefault(key, 0);
        int updatedFrequency = currentFrequency + 1;
        keyToFrequencyMap.computeIfPresent(key, (k, v) -> updatedFrequency);
        keyToFrequencyMap.putIfAbsent(key, updatedFrequency);
        frequencyToKeysMap.computeIfPresent(currentFrequency, (k, v) -> {
            v.remove(key);
            return v;
        });
        frequencyToKeysMap.computeIfPresent(updatedFrequency, (k, v) -> {
            v.add(key);
            return v;
        });
        frequencyToKeysMap.putIfAbsent(updatedFrequency, new LinkedHashSet<>(List.of(key)));
    }
}
