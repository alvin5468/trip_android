package com.example.mlj.mylocaljourney2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by alvin on 2016/5/14.
 */
/*
 * Utils class with toggling log messages that return the calling
 * method, file name, and line number
 */
public class Utils {
    public static Calendar dateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String dateToString(Date date) {
        Calendar calendar = dateToCalendar(date);
        return calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1) +"-" + calendar.get(Calendar.DAY_OF_MONTH);
    }

    /*
     * debug variable enables/disables all log messages to logcat
     * Useful to disable prior to app store submission
     */
    public static final boolean debug = true;

    /*
     * l method used to log passed string and returns the
     * calling file as the tag, method and line number prior
     * to the string's message
     */
    public static void l(String s) {
        if (debug) {
            String[] msg = trace(Thread.currentThread().getStackTrace(), 3);
            Log.i(msg[0], msg[1] + s);
        } else {
            return;
        }
    }

    /*
     * l (tag, string)
     * used to pass logging messages as normal but can be disabled
     * when debug == false
     */
    public static void l(String t, String s) {
        if (debug) {
            Log.i(t, s);
        } else {
            return;
        }
    }

    /*
     * trace
     * Gathers the calling file, method, and line from the stack
     * returns a string array with element 0 as file name and
     * element 1 as method[line]
     */
    public static String[] trace(final StackTraceElement e[], final int level) {
        if (e != null && e.length >= level) {
            final StackTraceElement s = e[level];
            if (s != null) { return new String[] {
                    e[level].getFileName(), e[level].getMethodName() + "[" + e[level].getLineNumber() + "]"
            };}
        }
        return null;
    }
}
