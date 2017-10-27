package com.yshi.algorithm;

import org.testng.annotations.Test;

import java.util.Comparator;

import static org.testng.Assert.*;

@Test
public class MergeSortTest {
  @Test
  void testMergeSort() {
    Integer[] input = {5, 4, 1, 2, 3, 9, 12, 8, 23};
    MergeSort.mergeSort(input);
    assertEquals(input, new Integer[] {1, 2, 3, 4, 5, 8, 9, 12, 23}, "xx");
  }

  @Test
  void testReverseMergeSort() {
    Integer[] input = {5, 4, 1, 2, 3, 9, 12, 8, 23};
    MergeSort.mergeSort(input, new Comparator<Integer>() {
      public int compare(Integer o1, Integer o2) {
        return o2 - o1;
      }
    });
    assertEquals(input, new Integer[] {23, 12, 9, 8, 5, 4, 3, 2, 1}, "xx");
  }

  @Test
  void testBottomUpMergeSort() {
    Integer[] input = {5, 4, 1, 2, 3, 9, 12, 8, 23};
    MergeSort.bottomUpMergeSort(input);
    assertEquals(input, new Integer[] {1, 2, 3, 4, 5, 8, 9, 12, 23}, "xx");
  }
}
