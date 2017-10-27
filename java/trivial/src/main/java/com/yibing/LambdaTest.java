package com.yibing;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.Callable;

public class LambdaTest {

  public String toString() {
    return "a special toString implementation";
  }

  public Runnable innerClassRunnable = new Runnable() {
    @Override
    public void run() {
      System.out.println(this);
      System.out.println(toString());
    }
  };

  public Runnable lambdaRunnable = () -> {
    System.out.println(this);
    System.out.println(toString());
  };

  static class Person {
    final String firstName;
    final String secondName;
    final int yearBorn;

    public Person(String firstName, String secondName, int yearBorn) {
      this.firstName = firstName;
      this.secondName = secondName;
      this.yearBorn = yearBorn;
    }

    public String toString() {
      return firstName + '.' + secondName + '(' + yearBorn + ')';
    }

    public String getFirstName() {
      return firstName;
    }

    public String getSecondName() {
      return secondName;
    }

    public int getYearBorn() {
      return yearBorn;
    }
  }

  public static void main(String[] args) throws Exception {
    Runnable r = () -> System.out.println("Hello Lambda");
    r.run();

    // method reference
    Comparator<String> c = String::compareToIgnoreCase;
    System.out.println(c.compare("abcd", "CDEF"));

    Callable<Integer> callable = () -> 1;
    System.out.println(callable.call());

    // Keyword "this" in lambda body refers to the outer object, not lambda
    // function itself.
    LambdaTest test = new LambdaTest();
    test.innerClassRunnable.run();
    test.lambdaRunnable.run();

    Person[] persons = {
        new Person("Yibing", "Shi", 1977),
        new Person("Kun", "Xu", 1980)
    };

    Arrays.sort(persons, Comparator.comparing(Person::getFirstName));
  }
}
