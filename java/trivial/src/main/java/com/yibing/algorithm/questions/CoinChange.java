package com.yibing.algorithm.questions;

import java.util.Arrays;

/**
 * Created by yshi on 19/2/17. Free to use.
 * Contact shi.yibing@gmail.com when necessary.
 */
public class CoinChange {
  /*
   *  You are given coins of different denominations and a total amount of money. Write a function to compute the number of combinations that make up that amount. You may assume that you have infinite number of each kind of coin.

   Note: You can assume that

   0 <= amount <= 5000
   1 <= coin <= 5000
   the number of coins is less than 500
   the answer is guaranteed to fit into signed 32-bit integer

   Example 1:

   Input: amount = 5, coins = [1, 2, 5]
   Output: 4
   Explanation: there are four ways to make up the amount:
   5=5
   5=2+2+1
   5=2+1+1+1
   5=1+1+1+1+1

   Example 2:

   Input: amount = 3, coins = [2]
   Output: 0
   Explanation: the amount of 3 cannot be made up just with coins of 2.

   Example 3:

   Input: amount = 10, coins = [10]
   Output: 1
   */

  private final int amount;
  private final int[] coins; //assume coins is sorted

  public CoinChange(int amount, int[] coins) {
    this.amount = amount;
    this.coins = coins;
    Arrays.sort(coins);
  }

  public int getNumberOfGroups() {
    if (amount < coins[0]) {
      return 0;
    }

    int[][] results = new int[coins.length][amount];
    for (int i = 0; i < coins.length; ++i) {
      for (int j = 0; j < amount; ++j) {
        if ((j + 1) % coins[i] == 0) {
          results[i][j] = 1;
        }
        for (int k = j; k >= 0; k -= coins[i]) {
          results[i][j] += i > 0 ? results[i-1][k] : 0;
        }
      }
    }
    return results[coins.length - 1][amount - 1];
  }
}
