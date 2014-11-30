package com.yshi.sqoop;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/** sqoop list-tables
 * -Dorg.apache.sqoop.credentials.loader.class=org.apache.sqoop.util.password.CryptoFileLoader
 * -Dorg.apache.sqoop.credentials.loader.crypto.passphrase=sqooppass
 * -Dorg.apache.sqoop.credentials.loader.crypto.alg="AES/ECB/PKCS5Padding"
 * --connect 'jdbc:mysql://10.16.8.127/cloudera'
 * --username cloudera
 * --password-file file:///tmp/new.pwd*/
public class GenEncryptedPasswordFile {

    private static byte[] encryptPassword(String password, String passPhrase, String alg, int iterations, int keySize) throws Exception {
        String algOnly = alg.split("/")[0];

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        SecretKey secKey =  factory.generateSecret(new PBEKeySpec(passPhrase.toCharArray(), "SALT".getBytes(), iterations, keySize));
        SecretKeySpec key = new SecretKeySpec(secKey.getEncoded(), algOnly);

        Cipher crypto = Cipher.getInstance(alg);
        crypto.init(Cipher.ENCRYPT_MODE, key);

        return crypto.doFinal(password.getBytes());
    }

    private static void writeToFile(String filePath, byte [] contents) throws IOException {
        File pwdFile = new File(filePath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pwdFile);
            fos.write(contents);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    private static final String DFT_ALG = "AES/ECB/PKCS5Padding";
    private static final int DFT_KEY_LENGTH = 128;

    public static void main(String[] args) throws Exception {
        if (args.length !=3 && args.length != 5) {
            System.err.println("Usage:\n" +
                    "java GenEncryptedPasswordFile <output-file-path> " +
                    "<password> <passphase> [<algorithm> <key-length>]\n" +
                    "Default algorithm: " + DFT_ALG + "\nDefault key length: " + DFT_KEY_LENGTH);
            return;
        }

        final String outputFilePath = args[0];
        final String password = args[1];
        final String passphrase = args[2];

        String alg = DFT_ALG;
        int keyLength = DFT_KEY_LENGTH;

        if (args.length == 5) {
            alg = args[3];
            keyLength = Integer.valueOf(args[5]);
        }

        writeToFile(outputFilePath,
                encryptPassword(password, passphrase, alg, 10000, keyLength));
    }
}
