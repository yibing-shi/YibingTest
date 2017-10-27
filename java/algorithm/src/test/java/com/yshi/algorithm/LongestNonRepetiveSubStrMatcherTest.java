package com.yshi.algorithm;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Test
public class LongestNonRepetiveSubStrMatcherTest {
  private LongestNonRepetiveSubStrMatcher matcher;

  @BeforeMethod
  public void setup() {
    matcher = new LongestNonRepetiveSubStrMatcher();
  }

  @DataProvider
  public Object[][] testData() {
    return new Object[][] {
        {null, false, 0, -1},
        {"", false, 0, -1},
        {"aa", true, 1, 0},
        {"ab", true, 2, 0},
        {"babcdea", true, 5, 1},
        {"aabcde", true, 5, 1},
        {"aabcdee", true, 5, 1}
    };
  }

  @Test (dataProvider = "testData")
  public void test(String str, boolean successful, int length, int offset) {
    assertEquals(matcher.match(str), successful);
    if (successful) {
      assertEquals(matcher.getLongestSubStrLen(), length);
      assertEquals(matcher.getLongestSubStrOffset(), offset);
    }
  }

}
