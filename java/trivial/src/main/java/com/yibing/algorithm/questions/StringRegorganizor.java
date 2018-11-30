package com.yibing.algorithm.questions;

/*
Given a string S, check if the letters can be rearranged so that two characters that are adjacent to each other are not the same.

If possible, output any possible result.  If not possible, return the empty string.

Example 1:

Input: S = "aab"
Output: "aba"

Example 2:

Input: S = "aaab"
Output: ""

Note:

    S will consist of lowercase letters [a - z]

 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class StringRegorganizor {

  private static class CharCount {
    private final char ch;
    private int count;

    CharCount(char ch, int count) {
      this.ch = ch;
      this.count = count;
    }

  }

  public String reorg(String input) {
    int chCounts[] = new int['z' - 'a'];
    for (char ch: input.toCharArray()) {
      chCounts[ch - 'a']++;
    }

    PriorityQueue<CharCount> pq = new PriorityQueue<>(
        (cc1, cc2) -> cc1.count == cc2.count ? cc2.ch - cc1.ch : cc2.count - cc1.count
    );
    for (int i = 0; i < chCounts.length; ++i) {
      if (chCounts[i] > (input.length() + 1) / 2) {
        return "";
      }

      if (chCounts[i] == 0) {
        continue;
      }

      pq.add(new CharCount((char)('a' + i), chCounts[i]));
    }

    StringBuilder sb = new StringBuilder(input.length());
    while (pq.size() > 1) {
      CharCount cc1 = pq.poll();
      CharCount cc2 = pq.poll();
      sb.append(cc1.ch);
      sb.append(cc2.ch);
      if (cc1.count > 1) {
        pq.add(new CharCount(cc1.ch, cc1.count - 1));
      }
      if (cc2.count > 1) {
        pq.add(new CharCount(cc2.ch, cc2.count - 1));
      }
    }
    if (pq.size() > 0) {
      sb.append(pq.poll().ch);
    }
    return sb.toString();
  }

  String reorgNg(String input) {
    int chCounts[] = new int['z' - 'a'];
    for (char ch: input.toCharArray()) {
      chCounts[ch - 'a']++;
    }

    List<CharCount> countsList = new ArrayList<>();
    for (int i = 0; i < chCounts.length; ++i) {
      if (chCounts[i] == 0) {
        continue;
      }

      if (chCounts[i] > (input.length() + 1) / 2) {
        return "";
      }

      countsList.add(new CharCount((char)('a' + i), chCounts[i]));
    }
    countsList.sort((cc1, cc2) -> cc1.count == cc2.count ? cc2.ch - cc1.ch : cc2.count - cc1.count);

    StringBuilder sb = new StringBuilder(input.length());
    sb.setLength(input.length());
    int i = 0;
    while (!countsList.isEmpty()) {
      CharCount charCount = countsList.remove(0);
      for (int j = 0; j < charCount.count; j++) {
        sb.setCharAt(i, charCount.ch);
        i += 2;
        if (i >= input.length()) {
          i = 1;
        }
      }
    }

    return sb.toString();
  }
}
