package com.yshi.hive.jdbc;

import org.apache.commons.dbutils.DbUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.security.PrivilegedExceptionAction;
import java.sql.*;

public class Hive2ClientKerberos {

    public static void main (String args[]) throws SQLException, ClassNotFoundException, IOException, InterruptedException {
        if (args.length != 4) {
            System.err.println("Usage: java Hive2ClientKerberos <jdbc-url> <krb5.conf-path> <principal> <keytab-file-path>");
            return;
        }

        final String jdbcUrl = args[0];
        final String krb5ConfFile = args[1];
        final String principal = args[2];
        final String keytabFilePath = args[3];

        Class.forName("org.apache.hive.jdbc.HiveDriver");

        Configuration conf = new Configuration();
        conf.set("hadoop.security.authentication", "kerberos");
        conf.set("java.security.krb5.conf", krb5ConfFile);
        UserGroupInformation.setConfiguration(conf);
        UserGroupInformation.loginUserFromKeytab(principal, keytabFilePath);

        Connection db = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            db = getConnectionWithLogin(UserGroupInformation.getLoginUser(),
                    getWholeJdbcUrl(jdbcUrl, principal), "hive", "");
            stmt = db.createStatement();
            rs = stmt.executeQuery("show databases");

            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } finally {
            DbUtils.closeQuietly(db, stmt, rs);
        }
    }

    private static String getWholeJdbcUrl (final String jdbcUrl, final String principal) {
        if (!jdbcUrl.contains(";pricipal=")) {
            return jdbcUrl + ";principal=" + principal;
        } else {
            return jdbcUrl;
        }
    }

    private static Connection getConnectionWithLogin(final UserGroupInformation ugi, final String url,
                                                     final String user, final String password)
            throws IOException, InterruptedException {
        return ugi.doAs(new PrivilegedExceptionAction<Connection>() {
            public Connection run() throws Exception {
                return DriverManager.getConnection(url, user, password);
            }
        });
    }
}
