package com.intellischedule.report_service.cache;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ReportBinaryCache {

    private record CacheEntry(byte[] data, Instant expiresAt) {}

    private final Map<String, CacheEntry> store = new ConcurrentHashMap<>();
    private static final long DEFAULT_TTL_SECONDS = 300;

    public void put(String key, byte[] data) {
        store.put(key, new CacheEntry(data, Instant.now().plusSeconds(DEFAULT_TTL_SECONDS)));
    }

    public byte[] get(String key) {
        CacheEntry entry = store.get(key);
        if (entry != null && Instant.now().isBefore(entry.expiresAt())) {
            return entry.data();
        }
        store.remove(key);
        return null;
    }

    public void evict(String key) {
        store.remove(key);
    }

    public void clearAll() {
        store.clear();
    }
}
