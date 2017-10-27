package com.yshi.questions;

import java.math.BigInteger;

public class AdditiveNumber {
  public boolean isAdditiveNumber(String num) {
    if (num == null || num.length() < 3) {
      return false;
    }

    boolean isAdditive = false;
    for (int firstNumLen = 1; firstNumLen <= num.length() / 2 && !isAdditive; ++firstNumLen) {
      for (int secNumLen = 1; secNumLen <= num.length() / 2 && !isAdditive; ++secNumLen) {
        BigInteger firstNum = strToBigInteger(num, 0, firstNumLen);
        BigInteger secNum = strToBigInteger(num, firstNumLen, firstNumLen + secNumLen);
        int compareIndex = firstNumLen + secNumLen;
        while (compareIndex < num.length()) {
          BigInteger sum = firstNum.add(secNum);
          String sumStr = sum.toString();
          if (compareIndex + sumStr.length() > num.length()) {
            break;
          }

          if (sumStr.equals(num.substring(compareIndex))) {
            isAdditive = true;
            break;
          }

          firstNum = secNum;
          secNum = sum;
          compareIndex += sumStr.length();
        }
      }
    }
    return isAdditive;
  }

  private BigInteger strToBigInteger(String num, int start, int end) {
    BigInteger result = BigInteger.ZERO;
    for (int i = start; i < end; ++i) {
      result = result.multiply(BigInteger.TEN).add(BigInteger.valueOf(Character.getNumericValue(num.charAt(i))));
    }
    return result;
  }
}
