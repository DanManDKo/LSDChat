package com.example.lsdchat.util;

/**
 * Created by User on 30.01.2017.
 */

public class Email {
    public static boolean checkEmail(String email) {
        return email.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    }
}
