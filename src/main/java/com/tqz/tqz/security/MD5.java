package com.tqz.tqz.security;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String getMd5(String val) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        md5.update(val.getBytes());
        byte[] m = md5.digest();//加密
        // md5.digest(val.getBytes());
        return getString(m);
    }

    private static String getString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length;
             i++) {
            sb.append(b[i]);
        }
        return sb.toString();
    }
}
