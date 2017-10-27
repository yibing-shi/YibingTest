package com.yshi.algorithm;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertEqualsNoOrder;
import static org.testng.Assert.assertTrue;

@Test
public class QuickSortTest {

  private QuickSort sorter;

  @DataProvider(name = "testData")
  public Object[][] genTestData() {
    return new Object[][] {
        {Arrays.asList(5, 4, 1, 2, 3, 9, 12, 8, 23), Arrays.asList(1, 2, 3, 4, 5, 8, 9, 12, 23)}
    };
  }

  @BeforeMethod
  public void setup() {
    sorter = new QuickSort();
  }

  @Test
  void testNormalSort() {
    Integer[] input = {5, 4, 1, 2, 3, 9, 12, 8, 23};
    Integer[] expected = {1, 2, 3, 4, 5, 8, 9, 12, 23};
    sorter.quickSort(input);
    assertEquals(input, expected);
  }

}
