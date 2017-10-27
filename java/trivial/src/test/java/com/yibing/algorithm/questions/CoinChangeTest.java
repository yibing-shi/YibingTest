package com.yibing.algorithm.questions;

import com.yibing.algorithm.questions.CoinChange;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by yshi on 19/2/17. Free to use.
 * Contact shi.yibing@gmail.com when necessary.
 */
public class CoinChangeTest {

  @Test
  public void testCoinChange() {
    Assert.assertEquals(new CoinChange(5, new int[] {1, 2, 5}).getNumberOfGroups(), 4);
    Assert.assertEquals(new CoinChange(3, new int[] {2}).getNumberOfGroups(), 0);
    Assert.assertEquals(new CoinChange(10, new int[] {10}).getNumberOfGroups(), 1);
  }
}
