package org.practice.cache.strategy;

public interface CachingStrategy {

    Object get(String key);

    boolean put(String key, Object object);

    boolean remove(String key);

    boolean evict();

}
