package com.yshi.sqoop;

import java.sql.*;

public class JdbcDriverVerifier {

  public static void main(String[] args) throws ClassNotFoundException, SQLException {
    if (args.length != 3) {
      System.err.println("Usage: Java JDBCDriverVerifier <driver-class-name> <connection-string> " +
          "<username> <password>");
      return;
    }

    final String className = args[0];
    Class.forName(className);

    final String connectionStr = args[1];
    final String username = args[2];
    final String password = args[3];

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      conn = DriverManager.getConnection(connectionStr, username, password);
      stmt = conn.createStatement();
      rs = stmt.executeQuery("select 1");
      while (rs.next()) {
        System.out.println("Record:" + rs.getString(1));
      }
    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException e) {
          //Ignore
        }
      }
    }
  }
}
