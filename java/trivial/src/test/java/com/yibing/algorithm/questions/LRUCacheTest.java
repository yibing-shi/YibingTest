package com.yibing.algorithm.questions;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class LRUCacheTest {
  private static final int TEST_CACHE_CAPACITY = 3;
  private LRUCache<String, String> cache;

  @BeforeMethod
  public void setupCache() {
    this.cache = new LRUCache<>(TEST_CACHE_CAPACITY);
  }

  @Test
  public void testCacheInit() {
    this.cache.put("key1", "val1");
    this.cache.put("key2", "val2");
    this.cache.put("key3", "val3");

    assertEquals(this.cache.get("key1"), "val1");
    assertEquals(this.cache.get("key2"), "val2");
    assertEquals(this.cache.get("key3"), "val3");

    this.cache.put("key4", "val4"); // this would evict key1
    assertNull(this.cache.get("key1"));
    assertEquals(this.cache.get("key4"), "val4");
  }
}
