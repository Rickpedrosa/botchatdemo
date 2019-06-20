package com.example.ricardopedrosarecupandroid.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyTimeUtils {

    public static String getCurrentTime() {
        return new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(new Date().getTime());
    }

    private MyTimeUtils() {
    }
}
