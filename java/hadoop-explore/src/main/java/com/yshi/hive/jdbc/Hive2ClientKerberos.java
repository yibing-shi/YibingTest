package com.yshi.hive.jdbc;

import org.apache.commons.dbutils.DbUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.security.PrivilegedExceptionAction;
import java.sql.*;

public class Hive2ClientKerberos {

    private static final String DFT_URL = "jdbc:hive2://nightly52-1.ent.cloudera.com:10000";
    private static final String DFT_USER = "hive";
    private static final String DFT_PSWD = "";

    public static void main (String args[]) throws SQLException, ClassNotFoundException, IOException, InterruptedException {

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

        Configuration conf = new Configuration();
        conf.set("hadoop.security.authentication", "kerberos");
        conf.set("java.security.krb5.conf","/tmp/krb5.conf");
        UserGroupInformation.setConfiguration(conf);
        UserGroupInformation.loginUserFromKeytab("hive/host-10-16-8-127.openstacklocal@YSHI.COM",
                "/tmp/hive.keytab");

        Connection db = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            db = getConnectionWithLogin(UserGroupInformation.getLoginUser(), url, user, password);
            stmt = db.createStatement();
            rs = stmt.executeQuery("show databases");

            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } finally {
            DbUtils.closeQuietly(db, stmt, rs);
        }
    }

    private static Connection getConnectionWithLogin(final UserGroupInformation ugi, final String url,
                                                     final String user, final String password)
            throws IOException, InterruptedException {
        return ugi.doAs(new PrivilegedExceptionAction<Connection>() {
            @Override
            public Connection run() throws Exception {
                return DriverManager.getConnection(url, user, password);
            }
        });
    }
}
