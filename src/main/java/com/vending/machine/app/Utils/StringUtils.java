package com.vending.machine.app.Utils;

public class StringUtils {

    public static String convertToHumanlyReadable(String str) {
        str = str.replace('_', ' ');
        str = str.substring(0,1).toUpperCase() + str.substring(1).toLowerCase();
        return str;
    }
}
