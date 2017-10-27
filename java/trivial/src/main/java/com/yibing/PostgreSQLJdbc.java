package com.yibing;

import org.apache.commons.dbutils.DbUtils;

import java.sql.*;

/**
 * Created by yshi on 20/12/16. Free to use.
 * Contact shi.yibing@gmail.com when necessary.
 */
public class PostgreSQLJdbc {
  public static void main(String[] args) throws ClassNotFoundException {
    Class.forName("org.postgresql.Driver");

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      conn = DriverManager.getConnection(
          "jdbc:postgresql://main/sql_ascii_test",
          "sqoop",
          "cloudera"
      );

      stmt = conn.createStatement();
      rs = stmt.executeQuery(
          "select convert_from(\n" +
              "      convert(\n" +
              "        convert_to(name, 'WIN1252'), 'WIN1252', 'UTF8'\n" +
              "      ), 'UTF8'\n" +
              "    )\n" +
              "FROM test_intl_char;"
      );

      while (rs.next()) {
        System.out.println(rs.getString(1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DbUtils.closeQuietly(conn, stmt, rs);
    }
  }
}
