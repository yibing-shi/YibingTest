package com.yshi.hive.jdbc;

import org.apache.commons.dbutils.DbUtils;

import java.sql.*;

public class Hive2ClientPreparedStmt {

    private static final String DFT_URL = "jdbc:hive2://10.16.181.188:21050/default";
    private static final String DFT_USER = "";
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
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            db = DriverManager.getConnection(url, user, password);
            stmt = db.prepareStatement("SELECT code col1, count(*) FROM sample_07 where total_emp > ? GROUP BY col1");
            stmt.setInt(1, 10000);
            rs = stmt.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                System.out.print(rs.getString(i) + '\t');
            }
            System.out.println();

        } finally {
            DbUtils.closeQuietly(db, stmt, rs);
        }
    }
}
