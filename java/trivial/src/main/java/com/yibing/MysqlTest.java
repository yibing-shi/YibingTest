package com.yibing;

import org.joda.time.DateTime;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MysqlTest {

    private static final Random randomGenerator = new Random();

    private final String dbUrl;
    private final String username;
    private final String password;
    private final List<Integer> timeIndexes;

    public MysqlTest(String dbUrl, String username, String password) {
        this.dbUrl = dbUrl;
        this.username = username;
        this.password = password;
        this.timeIndexes = Collections.unmodifiableList(initTimeIndexArray(2008, 10, 2012, 8));
    }

    private ArrayList<Integer> initTimeIndexArray(int startYear, int startMonth, int endYear, int endMonth) {
        DateTime startDate = new DateTime(startYear, startMonth, 1, 0, 0);
        DateTime endDate = new DateTime(endYear, endMonth, 1, 0, 0);
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (DateTime date = startDate;
             date.toInstant().compareTo(endDate.toInstant()) <= 0;
             date = date.plusMonths(1)) {
            result.add(date.getYear() * 1000000 + date.getMonthOfYear() * 10000 + 100);
        }
        return result;
    }

    private static final int TEST_COUNT = 100;

    public void test() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from test2");
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                for (int i = 0; i < meta.getColumnCount(); i++) {
                    System.out.print(rs.getString(i + 1) + "\t");
                }
                Timestamp ts = rs.getTimestamp("update_at");
                System.out.print(ts.getTimezoneOffset());
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }

    public static void main(String[] args) {
        final String dbUrl = "jdbc:mysql://host-10-17-81-144.coe.cloudera.com:3306/sqoop1?" +
            "useTimezone=true&serverTimezone=America/Los_Angeles";
        final String username = "sqoop";
        final String password = "cloudera";
        MysqlTest mysqlTest = new MysqlTest(dbUrl, username, password);
        mysqlTest.test();
    }
}
