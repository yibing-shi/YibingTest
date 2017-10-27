package com.yshi.algorithm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSort {
  public static <T extends Comparable<T> > void mergeSort(T[] input) {
    mergeSort(input, 0, input.length);
  }

  public static <T extends Comparable<T> > void mergeSort(T[] input, int begin, int end) {
    mergeSort(input, begin, end, new Comparator<T>() {
      public int compare(T o1, T o2) {
        return o1.compareTo(o2);
      }
    });
  }

  public static <T>
  void mergeSort(T[] input, Comparator<T> comparator) {
    mergeSort(input, 0, input.length, comparator);
  }

  public static <T>
  void mergeSort(T[] input, int begin, int end, Comparator<T> comparator) {
    if (input == null || end - begin <= 1) {
      return;
    }

    if (end - begin == 2) {
      if (comparator.compare(input[begin], input[end - 1]) > 0) {
        T temp = input[begin];
        input[begin] = input[end - 1];
        input[end - 1] = temp;
      }
      return;
    }

    merge(input, begin, (begin + end) / 2, end, comparator);
  }

  private static <T> void merge(T[] input, int begin, int split, int end, Comparator<T> comparator) {
    mergeSort(input, begin, split, comparator);
    mergeSort(input, split, end, comparator);

    List<T> result = new ArrayList<T>(input.length);

    int left = begin;
    int right = split;
    while (left < split && right < end) {
      if (comparator.compare(input[left], input[right]) > 0) {
        result.add(input[right]);
        right++;
      } else {
        result.add(input[left]);
        left++;
      }
    }

    if (left < split) {
      System.arraycopy(input, left, input, end - (split - left), split - left);
    }

    System.arraycopy(result.toArray(), 0, input, begin, result.size());
  }

  public static <T extends Comparable<T> > void bottomUpMergeSort(T[] input) {
    bottomUpMergeSort(input, 0, input.length);
  }

  public static <T extends Comparable<T> > void bottomUpMergeSort(T[] input, int begin, int end) {
    mergeSort(input, begin, end, new Comparator<T>() {
      public int compare(T o1, T o2) {
        return o1.compareTo(o2);
      }
    });
  }

  public static <T>
  void bottomUpMergeSort(T[] input, Comparator<T> comparator) {
    mergeSort(input, 0, input.length, comparator);
  }

  public static <T>
  void bottomUpMergeSort(T[] input, int begin, int end, Comparator<T> comparator) {
    int width = 1;
    int i = 0;
    while ((i + 1) * width < input.length) {
      merge(input, i * width, (i+1) * width, Math.min((i+2) * width, end), comparator);
      width = width * 2;
    }
  }
}
