package com.app.innovationweek.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.app.innovationweek.model.Rule;
import com.app.innovationweek.model.dao.DaoSession;
import com.app.innovationweek.model.dao.PhaseDao;
import com.app.innovationweek.model.firebase.Event;
import com.app.innovationweek.model.firebase.Phase;
import com.google.firebase.database.DataSnapshot;

import java.lang.ref.WeakReference;
import java.util.Map;

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
}
