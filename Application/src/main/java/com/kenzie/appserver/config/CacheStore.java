package com.kenzie.appserver.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kenzie.appserver.repositories.EventRepository;
import com.kenzie.appserver.repositories.model.EventRecord;
import com.kenzie.appserver.service.model.Event;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class CacheStore {
    private Cache<String, Optional<EventRecord>> cache;
    private EventRepository eventRepository;

    public CacheStore(int expiry, TimeUnit timeUnit) {
        // initialize the cache
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();    
    }

    public Optional<EventRecord> get(String key) {

        return cache.getIfPresent(key);
    }

    public void evict(String key) {

        cache.invalidate(key);
    }

    public void add(String key, Optional<EventRecord> value) {

        cache.put(key, value);
    }
}
