package com.yshi.algorithm;

import java.util.Random;

public class QuickSort {

  private Random random = new Random();

  public void quickSort(Integer[] arr) {
    quickSort(arr, 0, arr.length);
  }

  private void quickSort(Integer[] arr, int start, int end) {
    if (start >= end) {
      return;
    }

    int p = partition(arr, start, end);
    quickSort(arr, start, p);
    quickSort(arr, p + 1, end);
  }

  private int partition(Integer[] arr, int start, int end) {
    int randomPivot = start + random.nextInt(end - start);
    swap(arr, randomPivot, end - 1);

    int x = arr[end - 1];
    int i = start;
    for (int j = start; j < end - 1; j++) {
      if (arr[j] <= x) {
        swap(arr, i, j);
        ++i;
      }
    }
    swap(arr, i, end - 1);
    return i;
  }

  private void swap(Integer[] arr, int i, int j) {
    if (i == j) {
      return;
    }
    int temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
  }

}

