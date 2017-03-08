package com.app.innovationweek;

import android.app.Application;

import com.app.innovationweek.model.dao.DaoMaster;
import com.app.innovationweek.model.dao.DaoSession;
import com.google.firebase.database.FirebaseDatabase;

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
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
