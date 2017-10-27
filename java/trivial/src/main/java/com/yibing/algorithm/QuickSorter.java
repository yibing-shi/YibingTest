package com.yibing.algorithm;


public class QuickSorter {
  public static void sort(int[] data) {
    sort(data, 0, data.length);
  }

  private static void sort(int[] data, int s, int e) {
    if (s >= e - 1) {
      return;
    }

    int i = s + 1;
    int j = e - 1;
    while (i <= j) {
      while (i < e && data[i] <= data[s]) {
        i++;
      }
      while (j > s && data[j] > data[s]) {
        j--;
      }
     if (i < j && i < e && j > s) {
        swap(data, i, j);
        i++;
        j--;
      }
    }
    swap(data, s, i - 1);
    sort(data, s, j);
    sort(data, i, e);
  }

  private static void swap (int[] data, int i, int j) {
    int t = data[i];
    data[i] = data[j];
    data[j] = t;
  }

}

