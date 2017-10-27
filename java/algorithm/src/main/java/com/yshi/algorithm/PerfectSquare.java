package com.yshi.algorithm;

public class PerfectSquare {
  public static int numSquares(int n) {
    int[] results = new int[n + 1];
    results[0] = 0;
    for (int i = 1; i < n + 1; ++i) {
      results[i] = Integer.MAX_VALUE;
    }

    int[] elements = genElements(n);
    for (int i = 1; i < n + 1; ++i) {
      for (int j = 0; j < elements.length; ++j) {
        if (elements[j] > i) {
          break;
        }

        int potentialResult = results[i - elements[j]] + 1;
        if (potentialResult < results[i]) {
          results[i] = potentialResult;
        }
      }
    }
    return results[n];
  }

  private static int[] genElements(final int n) {
    int maxSquareRoot = (int) Math.floor(Math.sqrt(n));
    int[] result = new int[maxSquareRoot];
    for (int i = 1; i <= maxSquareRoot; ++i) {
      result[i] = i * i;
    }
    return result;
  }
}
