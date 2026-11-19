package com.intellischedule.report_service.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportBinaryCacheTest {

    private ReportBinaryCache cache;

    @BeforeEach
    void setUp() {
        cache = new ReportBinaryCache();
    }

    @Test
    void shouldStoreAndRetrieveData() {
        byte[] payload = "test-bytes".getBytes();
        cache.put("key1", payload);

        byte[] retrieved = cache.get("key1");
        assertNotNull(retrieved);
        assertArrayEquals(payload, retrieved);
    }

    @Test
    void shouldEvictDataExplicitly() {
        cache.put("key2", "data".getBytes());
        cache.evict("key2");

        assertNull(cache.get("key2"));
    }
}
