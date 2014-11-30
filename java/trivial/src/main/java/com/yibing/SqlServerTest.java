package com.yibing;

import java.sql.*;

public class SqlServerTest {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        Connection conn = DriverManager.getConnection("jdbc:sqlserver://10.0.2.21;instanceName=yshi;database=yshi", "yshi", "cloudera");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select * from test");
        ResultSetMetaData metaData = rs.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); ++i) {
            System.out.println(metaData.getColumnName(i) + " ----> " + metaData.getColumnTypeName(i)
                + " [" + metaData.getColumnType(i) + "]");
        }
    }
}
