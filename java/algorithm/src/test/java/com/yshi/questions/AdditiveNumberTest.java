package com.yshi.questions;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Test
public class AdditiveNumberTest {

  private AdditiveNumber additiveNumber;

  @BeforeMethod
  public void setUp() {
    additiveNumber = new AdditiveNumber();
  }

  @Test
  public void testAdditive() {
    assertFalse(additiveNumber.isAdditiveNumber(""));
    assertTrue(additiveNumber.isAdditiveNumber("11235813"));
    assertTrue(additiveNumber.isAdditiveNumber("121474836472147483648"));
    assertTrue(additiveNumber.isAdditiveNumber("1911192193385"));
  }
}
