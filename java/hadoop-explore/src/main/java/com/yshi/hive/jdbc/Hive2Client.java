package com.yshi.hive.jdbc;

import org.apache.commons.dbutils.DbUtils;

import java.sql.*;

public class Hive2Client {

    private static final String DFT_URL = "jdbc:hive2://nightly52-1.ent.cloudera.com:10000";
    private static final String DFT_USER = "hive";
    private static final String DFT_PSWD = "";

    public static void main (String args[]) throws SQLException, ClassNotFoundException {

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
            rs = stmt.executeQuery("show databases");

            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } finally {
            DbUtils.closeQuietly(db, stmt, rs);
        }
    }
}
