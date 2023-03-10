package com.haduc.beshop.util;

import java.util.Random;

public class FunctionCommon {
    public static String getRandomNumber(int len) {// tao mot chuoi ngau nhien tu 0 den 9
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

}
