package com.yshi.sqoop;

import org.apache.sqoop.Sqoop;

import java.util.TimeZone;

public class InlineSqoop {
  public static void main(String[] args) {
    String[] str = { "import",
        "-Doracle.sessionTimeZone=Australia/Melbourne",
        "--connect", "jdbc:oracle:thin:@host-10-17-81-20.coe.cloudera.com:1521:xe",
        "--username", "sqoop", "--password", "cloudera",
        "--table", "sqoop_test", "--target-dir", "/tmp/sqoop_test", "--delete-target-dir", "--direct"};

    System.out.println("Default timezone is: " + TimeZone.getDefault());

    try {
      Sqoop.runTool(str);
    } finally {
      System.out.println("Default timezone is: " + TimeZone.getDefault());
    }
  }
}
