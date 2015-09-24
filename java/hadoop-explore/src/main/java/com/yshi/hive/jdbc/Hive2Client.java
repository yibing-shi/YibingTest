package com.yshi.hive.jdbc;

import org.apache.commons.dbutils.DbUtils;

import java.sql.*;

public class Hive2Client {

  private static final String DFT_URL = "jdbc:hive2://host-10-17-80-41.coe.cloudera.com:10000";
  private static final String DFT_USER = "hive";
  private static final String DFT_PSWD = "";

  public static void main(String args[]) throws SQLException, ClassNotFoundException {

    Class.forName("org.apache.hive.jdbc.HiveDriver");

    String url = DFT_URL;
    if (args.length > 0) {
      url = args[0];
    }

    String user = DFT_USER;
    if (args.length > 1) {
      user = args[1];
    }

    String password = DFT_PSWD;
    if (args.length > 2) {
      password = args[2];
    }

    Connection db = null;
    Statement stmt = null;
    ResultSet rs = null;
    try {
      db = DriverManager.getConnection(url, user, password);
      stmt = db.createStatement();
      stmt.execute("set hive.query.result.fileformat=SequenceFile");

      rs = stmt.executeQuery("select * from hbase_test");
      final ResultSetMetaData meta = rs.getMetaData();
      final int numOfCols = meta.getColumnCount();

      while (rs.next()) {
        for (int i = 1; i <= numOfCols; ++i) {
          System.out.println(meta.getColumnName(i) + "[" + meta.getColumnTypeName(i) + "] ----> " + rs.getString(i));
          Object object = rs.getObject(i);
          System.out.println(object);
        }
      }
    } finally {
      DbUtils.closeQuietly(db, stmt, rs);
    }
  }
}
