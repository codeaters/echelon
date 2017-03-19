package com.app.innovationweek.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

/**
 * Created by zeeshan on 3/7/2017.
 */

public class Utils {
    public static boolean isLoggedIn(Context context) {
        if (context == null)
            return false;
        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(context);
        return spf.getBoolean("is_logged_in", false);
    }

    public static String getUid(Context context) {
        if (context == null)
            return null;
        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(context);
        return spf.getString("uid", "");
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

    }

    public static boolean isUsersFetched(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("is_users_fetched",
                false);
    }

    public static boolean isSpecialUser(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString("special_message",
                "").isEmpty();
    }

    public static String getSpecialMessage(Context context) {
        String uid = PreferenceManager.getDefaultSharedPreferences(context).getString
                ("uid", "");
        switch (uid) {
            case "1003473":
                return "Hi Khushbooo!!";
            case "1037647":
                return "Navodaya wale launde bade harami hote hai :D";
            case "1028696":
                return "Ami banglaaaa !!";
            case "983348":
                return "Hi mental ;)";
            case "883789":
                return "Bro Abd \\m/";
            case "1036870":
                return "m freaking awesome!!";
            default:
                return null;
        }
    }
}
