package com.example.lsdchat.util;

public class Email {
    public static boolean checkEmail(CharSequence email) {
        return email.toString().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    }
}
