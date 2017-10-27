package com.yshi.algorithm;

public class LongestNonRepetiveSubStrMatcher {
  private int longestSubStrLen;
  private int longestSubStrOffset;

  public LongestNonRepetiveSubStrMatcher () {
    longestSubStrLen = 0;
    longestSubStrOffset = -1;
  }

  public boolean match (final String str) {
    if (str == null || str.isEmpty()) {
      return false;
    }

    int begin = 0;
    int end = begin + 1;
    while (end < str.length() &&
        str.length() - begin >= longestSubStrLen) {
      boolean duplicate = false;
      for (int i = begin; i < end; ++i) {
        if (str.charAt(i) == str.charAt(end)) {
          duplicate = true;
          break;
        }
      }
      if (duplicate) {
        //find duplication
        if (end - begin > longestSubStrLen) {
          longestSubStrLen = end - begin;
          longestSubStrOffset = begin;
        }
        begin++;
        end++;
      } else {
        end++;
      }
    }

    if (end - begin > longestSubStrLen) {
      longestSubStrLen = end - begin;
      longestSubStrOffset = begin;
    }

    return longestSubStrLen > 0;
  }

  public int getLongestSubStrLen() {
    return longestSubStrLen;
  }

  public int getLongestSubStrOffset() {
    return longestSubStrOffset;
  }
}
