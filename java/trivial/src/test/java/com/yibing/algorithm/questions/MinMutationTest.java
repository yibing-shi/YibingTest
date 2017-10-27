package com.yibing.algorithm.questions;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by yshi on 20/2/17. Free to use.
 * Contact shi.yibing@gmail.com when necessary.
 */
public class MinMutationTest {

  @Test
  public void testMinMutation() {
    MinMutation mutation = new MinMutation("ACGT".toCharArray(), new String[] {"AACCGGTA"});
    Assert.assertEquals(mutation.numMutations("AACCGGTT", "AACCGGTA"), 1);

    mutation = new MinMutation("ACGT".toCharArray(), new String[] {"AACCGGTA", "AACCGCTA", "AAACGGTA"});
    Assert.assertEquals(mutation.numMutations("AACCGGTT", "AAACGGTA"), 2);
  }
}
