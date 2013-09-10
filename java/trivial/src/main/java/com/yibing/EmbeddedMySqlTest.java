package com.yibing;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import com.mysql.management.driverlaunched.ServerLauncherSocketFactory;
import com.mysql.management.util.QueryUtil;

public class EmbeddedMySqlTest {
    public static String DRIVER = "com.mysql.jdbc.Driver";
    public static String JAVA_IO_TMPDIR = "java.io.tmpdir";
    public static void main(String[] args) throws Exception {
        File ourAppDir = new File(System.getProperty(JAVA_IO_TMPDIR));
        File databaseDir = File.createTempFile("test-mxj", "", ourAppDir);
        databaseDir.delete();
        databaseDir.mkdirs();
        int port = Integer.parseInt(System.getProperty("c-mxj_test_port", "3336"));
        String dbName = "our_test_app";
        String url = "jdbc:mysql:mxj://localhost:" + port + "/" + dbName //
                + "?" + "server.basedir=" + databaseDir //
                + "&" + "createDatabaseIfNotExist=true"//
                + "&" + "server.initialize-user=true" //
                ;
        System.out.println(url);
        String userName = "root";
        String password = "";
        Class.forName(DRIVER);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, userName, password);
            QueryUtil queryUtil = new QueryUtil(conn);
            queryUtil.execute("drop database if exists em_test");
            queryUtil.execute("create database em_test");
            queryUtil.execute("drop table if exists em_test.test");
            queryUtil.execute("create table em_test.test(a int, b int)");
            queryUtil.execute("insert into em_test.test values(1,2)");
            conn.close();

            conn = DriverManager.getConnection(url, userName, password);
            final String sql = "select * from test";
            String queryForString = new QueryUtil(conn).queryForString(sql);
            System.out.println("------------------------");
            System.out.println(sql);
            System.out.println("------------------------");
            System.out.println(queryForString);
            System.out.println("------------------------");
            System.out.flush();
            Thread.sleep(100); // wait for System.out to finish flush
        } finally {
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ServerLauncherSocketFactory.shutdown(databaseDir, null);
        }
    }
}