package org.gojek.cache.base;

import lombok.Setter;
import org.gojek.cache.strategy.CachingStrategy;

@Setter
public class GenericCache {

    private CachingStrategy cachingStrategy;
    private int capacity;
    private int size;

    public GenericCache(int capacity, CachingStrategy cachingStrategy) {
        this.cachingStrategy = cachingStrategy;
        this.capacity = capacity;
    }

    public <T> T get(String key, Class<T> clazz) {
        return clazz.cast(cachingStrategy.get(key));
    }

    public boolean remove(String key) {
        this.size--;
        return cachingStrategy.remove(key);
    }

    public boolean put(String key, Object object) {
        if (this.size == capacity) {
            this.cachingStrategy.evict();
            this.size--;
        }
        this.size++;
        return cachingStrategy.put(key, object);
    }

}
