package com.app.iw;

import android.app.Application;

import com.app.iw.model.dao.DaoMaster;
import com.app.iw.model.dao.DaoSession;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.greenrobot.greendao.database.Database;

/**
 * Created by zeeshan on 3/7/2017.
 */

public class EchelonApplication extends Application {
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic("defaultTopic");
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "echelon-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
