package com.yibing;

/**
 * Created by yshi on 23/12/16. Free to use.
 * Contact shi.yibing@gmail.com when necessary.
 */
public class ExceptionTest {

  static class MyException extends Exception {
    public MyException () {
      super();
    }

    public MyException(String message) {
      super(message);
    }

    public MyException(String message, Exception cause) {
      super(message, cause);
    }
  }

  private int exFunc() throws MyException {
    throw new RuntimeException("For testing");
  }

  private int genExceptionInWeirdWay () {
    try {
      return exFunc();
    } catch (MyException e) {
      e.printStackTrace();
    } finally {
      return 1;
    }
  }

  public static void main(String[] args) {
    ExceptionTest test = new ExceptionTest();

  }
}
