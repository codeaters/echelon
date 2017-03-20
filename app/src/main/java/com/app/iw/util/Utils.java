package com.app.iw.util;

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
        long lastFetchedOn = PreferenceManager.getDefaultSharedPreferences(context).getLong
                ("users_fetched_on", -1);
        return lastFetchedOn != -1;
    }
    public static long usersFetchedOn(Context context) {
        return  PreferenceManager.getDefaultSharedPreferences(context).getLong
                ("users_fetched_on", 0);
    }


    public static boolean isSpecialUser(Context context) {
        return getSpecialMessage(context) != null;
    }

    public static String getSpecialMessage(Context context) {
        String uid = PreferenceManager.getDefaultSharedPreferences(context).getString
                ("uid", "");
        switch (uid) {
            case "1003473":
                return "Hi Khushbooo!! enjoy your vacation in Pune :)";//khushboo
            case "1037647":
                return "Navodaya wale launde bade harami hote hai. dile pe le liye kya ?";//ambuj
            case "1028696":
                return "Ami banglaaaa !! macchi khaboo!!";//somnath
            case "983348":
                return "crying makes ppl strong! but you don't want to be Khali right?";//ojha
            case "883789":
                return "rule 1: don't give a f**k \\m/";//abdullah
            case "834619":
                return "You are freaking awesome!!";//danish
            case "1003563":
                return "who is 'butter cup cumber snatch'?";//divya
            case "974920":
                return "Shaadi mubarak !!";//hera
            case "1003546":
                return "Keep calm! and eat carrots :p";//shivika
            default:
                return null;
        }
    }
}
