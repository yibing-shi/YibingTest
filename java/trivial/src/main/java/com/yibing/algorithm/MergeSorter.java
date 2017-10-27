package com.yibing.algorithm;

/**
 * Created by yshi on 16/2/17. Free to use.
 * Contact shi.yibing@gmail.com when necessary.
 */
public class MergeSorter {
  int[] data;
  int[] helper;

  public MergeSorter(int[] data) {
    this.data = data;
    this.helper = new int[data.length];
  }

  public void sort() {
    mergeSort(0, data.length);
  }

  private void mergeSort(int s, int e) {
    if (s >= e - 1) {
      return;
    }

    int m = (s + e) / 2;
    mergeSort(s, m);
    mergeSort(m, e);

    int i = s;
    int j = m;
    int k = s;

    while (i < m && j < e) {
      if (data[i] > data[j]) {
        helper[k++] = data[j++];
      } else {
        helper[k++] = data[i++];
      }
    }

    if (i < m) {
      System.arraycopy(data, i, data, e - (m - i), m - i);
    }
    System.arraycopy(helper, s, data, s, k - s);
  }
}
