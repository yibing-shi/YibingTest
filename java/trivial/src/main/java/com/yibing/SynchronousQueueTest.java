package com.yibing;

import java.util.concurrent.SynchronousQueue;

public class SynchronousQueueTest {

  public static void main(String[] args) throws InterruptedException {
    SynchronousQueue<String> tmpQue = new SynchronousQueue<String>();
    tmpQue.put(new String("aaaaa"));

    System.out.println(tmpQue.size());

  }
}
