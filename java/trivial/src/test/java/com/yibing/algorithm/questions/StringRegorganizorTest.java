package com.yibing.algorithm.questions;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class StringRegorganizorTest {
  private StringRegorganizor reorganizer;

  @BeforeMethod
  public void setup() {
    reorganizer = new StringRegorganizor();
  }

  private boolean checkValidity(String str) {
    for (int i = 1; i < str.length(); i++) {
      if (str.charAt(i) == str.charAt(i - 1)) {
        return false;
      }
    }
    return true;
  }

  @DataProvider(name = "validCases")
  public Object[][] validCases() {
    return new Object[][] {
        {""},
        {"a"},
        {"ab"},
        {"abc"},
        {"ababc"},
        {"aaabc"},
        {"aaabbc"},
        {"aaabbbc"},
        {"aaabbbcc"},
    };
  }

  @Test (dataProvider = "validCases")
  public void testValidStrings(String input) {
    String strAfterReorg = reorganizer.reorg(input);
    assertTrue(checkValidity(strAfterReorg), "String \"" + input + "\" should be valid");
    assertEquals(strAfterReorg.length(), input.length(), "String length should be the same after reorder");
  }

  @Test (dataProvider = "validCases")
  public void testValidStringsNg(String input) {
    String strAfterReorg = reorganizer.reorgNg(input);
    assertTrue(checkValidity(strAfterReorg), "String \"" + input + "\" should be valid");
    assertEquals(strAfterReorg.length(), input.length(), "String length should be the same after reorder");
  }

  @DataProvider(name = "invalidCases")
  public Object[][] invalidCases() {
    return new Object[][] {
        {"aa"},
        {"aaa"},
        {"aaab"},
        {"aaaabb"},
    };
  }

  @Test(dataProvider = "invalidCases")
  public void testInvalidStrings(String input) {
    assertEquals(reorganizer.reorg(input), "", "String \"" + input + "\" should be invalid");
  }

  @Test(dataProvider = "invalidCases")
  public void testInvalidStringsNg(String input) {
    assertEquals(reorganizer.reorgNg(input), "", "String \"" + input + "\" should be invalid");
  }
}
