package com.yshi.hive.jdbc;

import org.apache.commons.dbutils.DbUtils;

import java.sql.*;

public class Hive1Client {

    public static void main (String[] args) throws SQLException, ClassNotFoundException {
        Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver");

        Connection conn = null;
        Statement stmt1 = null;
        Statement stmt2 = null;
        Statement stmt3 = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection("jdbc:hive://10.16.181.170:11050/default", "hive", "hive");
            stmt1 = conn.createStatement();
            stmt1.execute("add jar /tmp/hadoop-explore-1.0-SNAPSHOT.jar");

            stmt2 = conn.createStatement();
            stmt2.execute("create temporary function dummy as 'com.yshi.hive.udf.DummyUDF'");

            stmt3 = conn.createStatement();
            rs = stmt3.executeQuery("select dummy(code) from sample_07");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } finally {
            DbUtils.closeQuietly(stmt1);
            DbUtils.closeQuietly(stmt2);
            DbUtils.closeQuietly(conn, stmt3, rs);
        }
    }
}
