package com.minecraft.ultikits.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    static final char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String encrypt(String plainText, String saltValue) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(plainText.getBytes());
            md.update(saltValue.getBytes());

            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encrypt(String plainText) {
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");

            mdTemp.update(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hex[byte0 >>> 4 & 0xf];
                str[k++] = hex[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean valid(String text, String md5) {
        return md5.equals(encrypt(text));
    }

}
