package com.yshi.algorithm;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Test
public class PerfectSquareTest {

  @Test
  public void test() {
    assertEquals(PerfectSquare.numSquares(12), 3);
    assertEquals(PerfectSquare.numSquares(13), 2);
  }
}
