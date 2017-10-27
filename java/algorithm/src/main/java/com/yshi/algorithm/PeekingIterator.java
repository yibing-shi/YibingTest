package com.yshi.algorithm;


import java.util.Iterator;

// Java Iterator interface reference:
// https://docs.oracle.com/javase/8/docs/api/java/util/Iterator.html
class PeekingIterator implements Iterator<Integer> {
  Iterator<Integer> underlying;
  boolean hasCache;
  Integer cacheVal;

  public PeekingIterator(Iterator<Integer> iterator) {
    // initialize any member here.
    this.underlying = iterator;
    this.hasCache = false;
    this.cacheVal = null;
  }

  // Returns the next element in the iteration without advancing the iterator.
  public Integer peek() {
    if (!hasCache) {
      cacheVal = underlying.next();
      hasCache = true;
    }

    return cacheVal;
  }

  // hasNext() and next() should behave the same as in the Iterator interface.
  // Override them if needed.
  public Integer next() {
    if (hasCache) {
      hasCache = false;
      return cacheVal;
    } else {
      return underlying.next();
    }
  }

  public boolean hasNext() {
    return hasCache || underlying.hasNext();
  }

  public void remove() {
    throw new UnsupportedOperationException("PeekingIterator doesn't support remove operation");
  }

}