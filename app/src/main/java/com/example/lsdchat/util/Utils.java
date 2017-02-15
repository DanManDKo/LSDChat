package com.example.lsdchat.util;

import java.util.Random;



public class Utils {
    public static String generateFileName() {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        sb.append(".jpg");
        return sb.toString();
    }
}
