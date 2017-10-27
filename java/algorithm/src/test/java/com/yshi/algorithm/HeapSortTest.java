package com.yshi.algorithm;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test
public class HeapSortTest {
  @Test
  void testSort() {
    Integer[] input = {5, 4, 1, 2, 3, 9, 12, 8, 23};
    HeapSort.sort(input);
    assertEquals(input, new Integer[] {1, 2, 3, 4, 5, 8, 9, 12, 23}, "xx");
  }

}
