package com.yhsi.hive.jdbc;

import com.yshi.hive.jdbc.Hive2ClientKerberos;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.SQLException;

public class Hive2ClientKerberosTest {

    @Test
    public void testConnection() throws ClassNotFoundException, SQLException, InterruptedException, IOException {
        Hive2ClientKerberos.main(new String[] {});
    }
}
