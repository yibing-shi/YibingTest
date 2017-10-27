package com.yibing.algorithm.questions;

/**
 * Created by yshi on 20/2/17. Free to use.
 * Contact shi.yibing@gmail.com when necessary.
 */


import java.util.*;

/**
 *
 A gene string can be represented by an 8-character long string, with choices from "A", "C", "G", "T".

 Suppose we need to investigate about a mutation (mutation from "start" to "end"), where ONE mutation is defined as ONE single character changed in the gene string.

 For example, "AACCGGTT" -> "AACCGGTA" is 1 mutation.

 Also, there is a given gene "bank", which records all the valid gene mutations. A gene must be in the bank to make it a valid gene string.

 Now, given 3 things - start, end, bank, your task is to determine what is the minimum number of mutations needed to mutate from "start" to "end". If there is no such a mutation, return -1.

 Note:

 Starting point is assumed to be valid, so it might not be included in the bank.
 If multiple mutations are needed, all mutations during in the sequence must be valid.
 You may assume start and end string is not the same.

 Example 1:

 start: "AACCGGTT"
 end:   "AACCGGTA"
 bank: ["AACCGGTA"]

 return: 1

 Example 2:

 start: "AACCGGTT"
 end:   "AAACGGTA"
 bank: ["AACCGGTA", "AACCGCTA", "AAACGGTA"]

 return: 2

 Example 3:

 start: "AAAAACCC"
 end:   "AACCCCCC"
 bank: ["AAAACCCC", "AAACCCCC", "AACCCCCC"]

 return: 3

 */
public class MinMutation {
  final char[] charSet;
  final Set<String> bank;

  public MinMutation(char[] charSet, String[] bank) {
    this.charSet = new String(charSet).toCharArray();
    this.bank = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(bank)));
  }

  public int numMutations(final String start, final String end) {
    if (start == null || end == null || start.length() != end.length()) {
      return 0;
    }

    if (start.isEmpty()) return 0;

    LinkedList<String> que = new LinkedList<String>();
    que.addLast(start);
    int depth = 0;
    Set<String> visited = new HashSet<String>();
    while (!que.isEmpty()) {
      int size = que.size();
      for (; size > 0; size --) {
        String cur = que.removeFirst();
        if (cur.equals(end))
          return depth;

        visited.add(cur);
        for(int i = 0; i < cur.length(); ++i) {
          for (char c : charSet) {
            if (c == cur.charAt(i))
              continue;
            StringBuilder b = new StringBuilder(cur);
            b.setCharAt(i, c);
            String child = b.toString();
            if (!visited.contains(child) && bank.contains(child)) {
              que.addLast(child);
            }
          }
        }
      }
      depth++;
    }
    return depth;
  }
}
