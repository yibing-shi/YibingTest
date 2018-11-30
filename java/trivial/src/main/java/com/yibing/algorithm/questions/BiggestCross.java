package com.yibing.algorithm.questions;

import com.yibing.GenericVarArgs;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 In a 2D grid from (0, 0) to (N-1, N-1), every cell contains a 1 or a 0.
 What is the largest axis-aligned plus sign of 1s contained in the grid?
 Return the order of the plus sign. If there is none, return 0.

 An "axis-aligned plus sign of 1s of order k" has some center grid[x][y] = 1 along with 4 arms of
 length k-1 going up, down, left, and right, and made of 1s. This is demonstrated in the diagrams below.
 Note that there could be 0s or 1s beyond the arms of the plus sign, only the relevant area of the plus
 sign is checked for 1s.

 Examples of Axis-Aligned Plus Signs of Order k:

 Order 1:
 000
 010
 000

 Order 2:
 00000
 00100
 01110
 00100
 00000

 Order 3:
 0000000
 0001000
 0001000
 0111110
 0001000
 0001000
 0000000



 ====================
 Example 1:

 Input: N = 5, mines = [[4, 2]]
 Output: 2
 Explanation:
 11111
 11111
 11111
 11111
 11011
 In the above grid, the largest plus sign can only be order 2.  One of them is marked in bold.

 Example 2:

 Input: N = 2, mines = []
 Output: 1
 Explanation:
 There is no plus sign of order 2, but there is of order 1.

 Example 3:

 Input: N = 1, mines = [[0, 0]]
 Output: 0
 Explanation:
 There is no plus sign, so return 0.

 Note:

 N will be an integer in the range [1, 500].
 mines will have length at most 5000.
 mines[i] will be length 2 and consist of integers in the range [0, N-1].
 (Additionally, programs submitted in C, C++, or C# will be judged with a slightly smaller time limit.)

 */

public class BiggestCross {
  static class Coordinator {
    final private int row;
    final private int column;

    Coordinator(int row, int column) {
      this.row = row;
      this.column = column;
    }

    public int getRow() {
      return row;
    }

    public int getColumn() {
      return column;
    }
  }

  private static abstract class GridRowColumnIterator implements Iterator<Coordinator> {
    final int matrixSize;
    Coordinator coord;

    GridRowColumnIterator(int matrixSize, Coordinator coord) {
      this.matrixSize = matrixSize;
      this.coord = coord;
    }
  }

  private static class LeftArmIterator extends GridRowColumnIterator {
    LeftArmIterator(int matrixSize, Coordinator coord) {
      super(matrixSize, new Coordinator(coord.getRow(), -1));
    }

    @Override
    public boolean hasNext() {
      return this.coord.getColumn() < matrixSize - 1;
    }

    @Override
    public Coordinator next() {
      this.coord = new Coordinator(coord.getRow(), coord.getColumn() + 1);
      return this.coord;
    }
  }

  private static class RightArmIterator extends GridRowColumnIterator {
    RightArmIterator(int matrixSize, Coordinator coord) {
      super(matrixSize, new Coordinator(coord.getRow(), matrixSize));
    }

    @Override
    public boolean hasNext() {
      return this.coord.getColumn() > 0;
    }

    @Override
    public Coordinator next() {
      this.coord = new Coordinator(coord.getRow(), coord.getColumn() - 1);
      return this.coord;
    }
  }

  private static class UpArmIterator extends GridRowColumnIterator {
    UpArmIterator(int matrixSize, Coordinator coord) {
      super(matrixSize, new Coordinator(-1, coord.getColumn()));
    }

    @Override
    public boolean hasNext() {
      return this.coord.getRow() < matrixSize - 1;
    }

    @Override
    public Coordinator next() {
      this.coord = new Coordinator(coord.getRow() + 1, coord.getColumn());
      return this.coord;
    }
  }

  private static class DownArmIterator extends GridRowColumnIterator {
    DownArmIterator(int matrixSize, Coordinator coord) {
      super(matrixSize, new Coordinator(matrixSize, coord.getColumn()));
    }

    @Override
    public boolean hasNext() {
      return this.coord.getRow() > 0;
    }

    @Override
    public Coordinator next() {
      this.coord = new Coordinator(coord.getRow() - 1, coord.getColumn());
      return this.coord;
    }
  }


  public int getBiggestCrossOrder(int[][] grid) {
    int n = grid.length;
    if (n == 0) {
      return 0;
    }

    if (n != grid[0].length) {
      throw new InvalidParameterException("The number of rows and columns should be the same.");
    }

    // 2D array dp stores the min length of all the 4 arms of one item
    int[][] dp = genDPArray(n);

    // Check left and right arms
    for (int r = 0; r < n; r++) {
      dp[r][0] = grid[r][0] == 0 ? 0 : 1;
      for (int c = 1; c < n; c++) {
        if (grid[r][c] == 0) {
          dp[r][c] = 0;
        } else {
          dp[r][c] = Math.min(dp[r][c], dp[r][c-1] + 1);
        }
      }
      dp[r][n-1] = grid[r][n-1] == 0 ? 0 : 1;
      for (int c = n - 2; c >= 0; c--) {
        if (grid[r][c] == 0) {
          dp[r][c] = 0;
        } else {
          dp[r][c] = Math.min(dp[r][c], dp[r][c+1] + 1);
        }
      }
    }

    // Check up and down arms
    for (int c = 0; c < n; c++) {
      dp[0][c] = grid[0][c] == 0 ? 0 : 1;
      for (int r = 1; r < n; r++) {
        if (grid[r][c] == 0) {
          dp[r][c] = 0;
        } else {
          dp[r][c] = Math.min(dp[r][c], dp[r - 1][c] + 1);
        }
      }
      dp[n - 1][c] = grid[n - 1][c] == 0 ? 0 : 1;
      for (int r = n - 2; r >= 0; r--) {
        if (grid[r][c] == 0) {
          dp[r][c] = 0;
        } else {
          dp[r][c] = Math.min(dp[r][c], dp[r + 1][c] + 1);
        }
      }
    }

    return findMaxOrder(n, dp);
  }

  public int getBiggestCrossOrderNG(int[][] grid) {
    int n = grid.length;
    if (n == 0) {
      return 0;
    }

    if (n != grid[0].length) {
      throw new InvalidParameterException("The number of rows and columns should be the same.");
    }

    // 2D array dp stores the min length of all the 4 arms of one item
    int[][] dp = genDPArray(n);

    List<Iterator<Coordinator>> iterators = new ArrayList<>(n * 4);
    for (int i = 0; i < n; i++) {
      iterators.add(new LeftArmIterator(n, new Coordinator(i, i)));
      iterators.add(new RightArmIterator(n, new Coordinator(i, i)));
      iterators.add(new UpArmIterator(n, new Coordinator(i, i)));
      iterators.add(new DownArmIterator(n, new Coordinator(i, i)));
    }

    for (Iterator<Coordinator> iter: iterators) {
      assert (iter.hasNext());
      Coordinator first = iter.next(); // Must have at least one item because it has been
      dp[first.getRow()][first.getColumn()] = grid[first.getRow()][first.getColumn()] == 1 ? 1 : 0;
      Coordinator prev = first;
      while (iter.hasNext()) {
        Coordinator current = iter.next();
        if (grid[first.getRow()][first.getColumn()] == 0) {
          dp[current.getRow()][current.getColumn()] = 0;
        } else {
          dp[current.getRow()][current.getColumn()] = Math.min(
              dp[current.getRow()][current.getColumn()], dp[prev.getRow()][prev.getColumn()] + 1);
        }
        prev = current;
      }
    }

    return findMaxOrder(n, dp);
  }

  private int findMaxOrder(int n, int[][] dp) {
    int maxOrder = 0;
    for (int r = 0; r < n; r++) {
      for (int c = 0; c < n; c++) {
        maxOrder = Math.max(maxOrder, dp[r][c]);
      }
    }
    return maxOrder;
  }

  private int[][] genDPArray(int n) {
    int[][] dp = new int[n][n];
    for (int r = 0; r < n; r++) {
      for (int c = 0; c < n; c++) {
        dp[r][c] = Integer.MAX_VALUE;
      }
    }
    return dp;
  }
}
