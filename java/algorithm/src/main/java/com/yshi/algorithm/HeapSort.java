package com.yshi.algorithm;

import java.util.Comparator;

public class HeapSort {

  public static <T extends Comparable<T>> void sort(T[] input) {
    sort(input, new Comparator<T>() {
      public int compare(T o1, T o2) {
        return o1.compareTo(o2);
      }
    });
  }

  public static <T> void sort(T[] input, Comparator<T> comparator) {
    heapify(input, comparator);
    swap(input, 0, input.length - 1);

    int sorted = 1;
    while (sorted < input.length) {
      siftDown(input, 0, input.length - sorted, comparator);
      swap(input, 0, input.length - sorted - 1);
      sorted ++;
    }
  }

  private static <T> void heapify (T[] input, Comparator<T> comparator) {
    int start = (input.length - 1 - 2) / 2;
    while (start >= 0) {
      siftDown(input, start, input.length, comparator);
      start --;
    }
  }

  private static <T> void siftDown (T[] input, int root, int end, Comparator<T> comparator) {
    int curPos = root;
    while (curPos * 2 + 1 < end) {
      // Has at least one child
      int left = curPos * 2 + 1;
      int right = left + 1;
      if (right < end) {
        //has right child as well
        if (comparator.compare(input[curPos], input[left]) > 0 &&
            comparator.compare(input[curPos], input[right]) > 0) {
          break;
        }
        if (comparator.compare(input[left], input[right]) > 0) {
          //left child is bigger
          swap(input, curPos, left);
          curPos = left;
        } else {
          // right child is bigger
          swap(input, curPos, right);
          curPos = right;
        }
      } else {
        // has only left child
        if (comparator.compare(input[curPos], input[left]) > 0) {
          break;
        }
        swap(input, curPos, left);
        curPos = left;
      }
    }
  }

  private static <T> void swap(T[] input, int p1, int p2) {
    T temp = input[p1];
    input[p1] = input[p2];
    input[p2] = temp;
  }
}
