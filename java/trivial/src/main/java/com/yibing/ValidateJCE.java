package com.yibing;

import javax.crypto.Cipher;
import java.security.NoSuchAlgorithmException;

public class ValidateJCE {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
        System.out.println(maxKeyLen);
    }
}
