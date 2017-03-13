package com.example.lsdchat.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DateUtils {

    public static String millisecondsToDate(String mill) {
        if (mill == null) {
            return "";
        } else {
            long milliseconds = Long.parseLong(mill) * 1000;

            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat formatterTime = new SimpleDateFormat("HH:mm");

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliseconds);

            Calendar dataNow = Calendar.getInstance();
            dataNow.setTimeInMillis(System.currentTimeMillis());

            if (formatter.format(calendar.getTime()).equals(formatter.format(dataNow.getTime()))) {
                return formatterTime.format(calendar.getTime());
            } else {
                return formatter.format(calendar.getTime());
            }

        }
    }
}
