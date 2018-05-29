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

    S will consist of lowercase letters and have length in range [1, 500].

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

    public CharCount(char ch, int count) {
      this.ch = ch;
      this.count = count;
    }

    public char getCh() {
      return ch;
    }

    public int getCount() {
      return count;
    }
  }

  public String reorg(String input) {
    int chCounts[] = new int['z' - 'a'];
    for (char ch: input.toCharArray()) {
      chCounts[ch - 'a']++;
    }

    PriorityQueue<CharCount> pq = new PriorityQueue<>(
        (cc1, cc2) -> cc1.getCount() == cc2.getCount() ? cc2.getCh() - cc1.getCh() : cc2.getCount() - cc1.getCount()
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
      sb.append(cc1.getCh());
      sb.append(cc2.getCh());
      if (cc1.getCount() > 1) {
        pq.add(new CharCount(cc1.getCh(), cc1.getCount() - 1));
      }
      if (cc2.getCount() > 1) {
        pq.add(new CharCount(cc2.getCh(), cc2.getCount() - 1));
      }
    }
    if (pq.size() > 0) {
      sb.append(pq.poll().getCh());
    }
    return sb.toString();
  }

  public String reorgNg(String input) {
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
    countsList.sort((cc1, cc2) -> cc1.getCount() == cc2.getCount() ? cc2.getCh() - cc1.getCh() : cc2.getCount() - cc1.getCount());

    StringBuilder sb = new StringBuilder(input.length());
    sb.setLength(input.length());
    int i = 0;
    while (!countsList.isEmpty()) {
      CharCount charCount = countsList.remove(0);
      for (int j = 0; j < charCount.getCount(); j++) {
        sb.setCharAt(i, charCount.getCh());
        i += 2;
        if (i >= input.length()) {
          i = 1;
        }
      }
    }

    return sb.toString();
  }
}
