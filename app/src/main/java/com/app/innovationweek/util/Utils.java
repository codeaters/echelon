package com.app.innovationweek.util;

import android.content.Context;
import android.content.SharedPreferences;
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
}
