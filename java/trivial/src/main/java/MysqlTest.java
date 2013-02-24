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
            long totalTime = 0;
            long totalAmount = 0;
            for (int i = 0; i < TEST_COUNT; ++i) {
                long numberOfResults = 0;
                long startTime = System.currentTimeMillis();
                Integer timeIndex = timeIndexes.get(randomGenerator.nextInt(timeIndexes.size()));
                ResultSet resultSet = statement.executeQuery(getQuery(timeIndex));
                while (resultSet.next()) {
                    numberOfResults ++;
                    totalAmount += resultSet.getLong(4);
                }
                long endTime = System.currentTimeMillis();
                System.out.println(numberOfResults + " records returned for " + timeIndex +
                        " in " + (endTime - startTime) + " milli seconds");
                totalTime += endTime - startTime;
            }
            System.out.println("Average time: " + totalTime/TEST_COUNT + " milli seconds");
            System.out.println("Average amount: " + totalAmount/TEST_COUNT);
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

    private String getQuery(Integer timeIndex) {
        return " select country_code, entity_id, partner_id, sum(m1) as amount " +
                " from publication where country_code in (360,682) and time_index=" + timeIndex +
                " group by country_code, entity_id, partner_id " +
                " order by amount desc";
    }

    public static void main(String[] args) {
        final String dbUrl = "jdbc:mysql://labrat-00.eng.inf.effectivemeasure.net/em_data";
        final String username = "dbtest";
        final String password = "emr0cks";
        MysqlTest mysqlTest = new MysqlTest(dbUrl, username, password);
        mysqlTest.test();
    }
}
