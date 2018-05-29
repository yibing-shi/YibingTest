package com.yibing;

import java.util.Arrays;
import java.util.List;

public class GenericVarArgs {

  static void func(List<String> ... lists) {
    System.out.println(lists.length);
  }

  public static void main(String[] args) {
    List<String> l1 = Arrays.asList("a", "b");
    GenericVarArgs.func(l1);
  }
}
