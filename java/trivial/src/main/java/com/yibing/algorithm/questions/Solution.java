package com.yibing.algorithm.questions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {
  public static void main(String args[] ) throws Exception {
    List<List<Integer>> input = Arrays.asList(
        Arrays.asList(1,1,1,1,1),
        Arrays.asList(1,1,1,1,1),
        Arrays.asList(1,1,1,1,1),
        Arrays.asList(1,1,1,1,1),
        Arrays.asList(1,1,0,1,1)
    );
    System.out.println("Output found: " + getLargestOrder(input));
    assert getLargestOrder(input) == 2;
  }

  private final List<List<Integer>> map;
  private final List<List<Integer>> northArmLengths = new ArrayList<>();
  private final List<List<Integer>> southArmLengths = new ArrayList<>();
  private final List<List<Integer>> eastArmLengths = new ArrayList<>();
  private final List<List<Integer>> westArmLengths = new ArrayList<>();

  public static int getLargestOrder(List<List<Integer>> map) {
    if (map.isEmpty()) { return 0; }
    assert map.size() == map.get(0).size();
    return new Solution(map).getHighestOrder();
  }

  public Solution(List<List<Integer>> map) {
    this.map = map;
    initializeEmptyMap(southArmLengths, map.size());
    initializeEmptyMap(northArmLengths, map.size());
    initializeEmptyMap(eastArmLengths, map.size());
    initializeEmptyMap(westArmLengths, map.size());
    populateEastArms();
    populateWestArms();
    populateNorthArms();
    populateSouthArms();
  }

  public int getHighestOrder() {
    int highestOrder = 0;
    for (int row = 0; row < map.size(); row++) {
      for (int col = 0; col < map.size(); col++) {
        int order = getOrder(row, col);
        if (order > highestOrder) {
          highestOrder = order;
        }
      }
    }
    return highestOrder;
  }

  public int getOrder(int row, int column) {
    int north = northArmLengths.get(row).get(column);
    int south = southArmLengths.get(row).get(column);
    int east = eastArmLengths.get(row).get(column);
    int west = westArmLengths.get(row).get(column);
    int order = Math.min(Math.min(east, west), Math.min(north, south));
    System.out.println("Row=" + row + ", Column=" + column + ", Order = " + order);
    return order;
  }

  private void initializeEmptyMap(List<List<Integer>> map, int width) {
    for (int row=0; row<width; row++) {
      List<Integer> curRow = new ArrayList<>();
      for (int col=0; col<width; col++) {
        curRow.add(0);
      }
      map.add(curRow);
    }
  }

  private void populateEastArms() {
    List<List<Integer>> myLengths = eastArmLengths;
    for (int row = 0; row < map.size(); row++) {
      int column = map.size() - 1;
      int length = map.get(row).get(column);
      myLengths.get(row).set(column, length);
    }
    for (int column = map.size() - 2; column >= 0; column--) {
      for (int row = 0; row < map.size(); row++) {
        int myLength = 0;
        if (map.get(row).get(column) == 1) {
          int neighborLength = myLengths.get(row).get(column+1);
          myLength = neighborLength + 1;
        }
        myLengths.get(row).set(column, myLength);
      }
    }
  }

  private void populateWestArms() {
    List<List<Integer>> myLengths = westArmLengths;
    for (int row = 0; row < map.size(); row++) {
      int column = 0;
      int length = map.get(row).get(column);
      myLengths.get(row).set(column, length);
    }
    for (int column = 1; column < map.size(); column++) {
      for (int row = 0; row < map.size(); row++) {
        int myLength = 0;
        if (map.get(row).get(column) == 1) {
          int neighborLength = myLengths.get(row).get(column-1);
          myLength = neighborLength + 1;
        }
        myLengths.get(row).set(column, myLength);
      }
    }
  }

  private void populateNorthArms() {
    List<List<Integer>> myLengths = northArmLengths;
    for (int column = 0; column < map.size(); column++) {
      int row = 0;
      int length = map.get(row).get(column);
      myLengths.get(row).set(column, length);
    }
    for (int row = 1; row < map.size(); row++) {
      for (int column = 0; column < map.size(); column++) {
        int myLength = 0;
        if (map.get(row).get(column) == 1) {
          int neighborLength = myLengths.get(row-1).get(column);
          myLength = neighborLength + 1;
        }
        myLengths.get(row).set(column, myLength);
      }
    }
  }

  private void populateSouthArms() {
    List<List<Integer>> myLengths = southArmLengths;
    for (int column = 0; column < map.size(); column++) {
      int row = map.size() - 1;
      int length = map.get(row).get(column);
      myLengths.get(row).set(column, length);
    }
    for (int row = map.size() - 2; row >= 0; row--) {
      for (int column = 0; column < map.size(); column++) {
        int myLength = 0;
        if (map.get(row).get(column) == 1) {
          int neighborLength = myLengths.get(row+1).get(column);
          myLength = neighborLength + 1;
        }
        myLengths.get(row).set(column, myLength);
      }
    }
  }
}
