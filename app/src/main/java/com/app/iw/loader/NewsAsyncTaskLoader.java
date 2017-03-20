package com.app.iw.loader;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.app.iw.EchelonApplication;
import com.app.iw.model.News;
import com.app.iw.model.dao.NewsDao;

import java.util.List;

/**
 * Created by Madeyedexter on 11-03-2017.
 */

public class NewsAsyncTaskLoader extends AsyncTaskLoader<List<News>> {
    private List<News> news;

    public NewsAsyncTaskLoader(Context applicationContext) {
        super(applicationContext);
    }

    @Override
    public List<News> loadInBackground() {
        List<News> news = ((EchelonApplication) getContext().getApplicationContext()).getDaoSession().getNewsDao().queryBuilder().orderDesc(NewsDao.Properties.Timestamp).list();
        return news;
    }

    @Override
    protected void onStartLoading() {
        if (news != null) {
            deliverResult(news);
        } else {
            // We have no data, so kick off loading it
            forceLoad();
        }
    }
}
