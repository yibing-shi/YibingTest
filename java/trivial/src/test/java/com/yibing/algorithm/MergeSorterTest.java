package com.yibing.algorithm;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;

/**
 * Created by yshi on 17/2/17. Free to use.
 * Contact shi.yibing@gmail.com when necessary.
 */
public class MergeSorterTest {

  @DataProvider(name = "sortDataProvider")
  public Object[][] dataProvider() {
    return new Object[][] {
        {new int[] {1}, new int[] {1}},
        {new int[]{2, 1}, new int[]{1, 2}},
        {new int[]{1, 2}, new int[]{1, 2}},
        {new int[]{3, 2, 1}, new int[]{1, 2, 3}},
        {new int[]{1, 2, 3}, new int[]{1, 2, 3}},
        {new int[]{2, 3, 1}, new int[]{1, 2, 3}}
    };
  }

  @Test (dataProvider = "sortDataProvider")
  public void testMergeSort(int[] data, int[] expectedResult) {
    MergeSorter sorter = new MergeSorter(data);
    sorter.sort();
    Assert.assertEquals(data, expectedResult, "expected: " + Arrays.toString(expectedResult) +
    ", got: " + Arrays.toString(data));
  }

  @Test (dataProvider = "sortDataProvider")
  public void testQuickSort(int[] data, int[] expectedResult) {
    QuickSorter.sort(data);
    Assert.assertEquals(data, expectedResult, "expected: " + Arrays.toString(expectedResult) +
        ", got: " + Arrays.toString(data));
  }
}
