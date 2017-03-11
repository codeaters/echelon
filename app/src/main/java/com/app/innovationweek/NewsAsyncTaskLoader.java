package com.app.innovationweek;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.app.innovationweek.model.News;

import java.util.List;

/**
 * Created by Madeyedexter on 11-03-2017.
 */

public class NewsAsyncTaskLoader extends AsyncTaskLoader<List<News>> {
    public NewsAsyncTaskLoader(Context applicationContext) {
        super(applicationContext);
    }

    @Override
    public List<News> loadInBackground() {
        return ((EchelonApplication) getContext().getApplicationContext()).getDaoSession()
                .getNewsDao().loadAll();
    }
}
