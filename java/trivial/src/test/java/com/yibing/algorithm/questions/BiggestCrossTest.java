package com.yibing.algorithm.questions;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class BiggestCrossTest {
  private BiggestCross orderFinder;

  @BeforeMethod
  public void setup() {
    this.orderFinder = new BiggestCross();
  }

  @DataProvider(name = "provider")
  public Object[][] provider() {
    return new Object[][] {
        {
            new int[][] {
                {1,1,1,1,1},
                {1,1,1,1,1},
                {1,1,1,1,1},
                {1,1,1,1,1},
                {1,1,0,1,1},
            },
            2, // expected order
        },
        {
            new int[][] {
                {1,1,},
                {1,0,},
            },
            1, // expected order
        },
        {
            new int[][] {
                {1,},
            },
            1, // expected order
        },
        {
            new int[][] {
                {0},
            },
            0, // expected order
        },
    };
  }

  @Test(dataProvider = "provider")
  public void test1(int[][] input, int expectedOrder) {
    assertEquals(this.orderFinder.getBiggestCrossOrder(input), expectedOrder);
  }

  @Test(dataProvider = "provider")
  public void testGetOrderNg(int[][] input, int expectedOrder) {
    assertEquals(this.orderFinder.getBiggestCrossOrderNG(input), expectedOrder);
  }
}
