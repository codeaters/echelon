package com.app.innovationweek.util;

import android.os.AsyncTask;

import com.app.innovationweek.callbacks.DaoOperationComplete;
import com.app.innovationweek.model.News;
import com.app.innovationweek.model.dao.DaoSession;
import com.google.firebase.database.DataSnapshot;

import java.lang.ref.WeakReference;

/**
 * Created by 1036870 on 3/16/2017.
 */

public class NewsInsertTask extends AsyncTask<DataSnapshot, Void, News> {
    private WeakReference<DaoOperationComplete> daoOperationCompleteWeakReference;
    private WeakReference<DaoSession> daoSessionWeakReference;

    public NewsInsertTask(DaoSession daoSession, DaoOperationComplete daoOperationComplete) {
        daoOperationCompleteWeakReference = new WeakReference<>(daoOperationComplete);
        daoSessionWeakReference = new WeakReference<>(daoSession);
    }

    @Override
    protected News doInBackground(DataSnapshot... dataSnapshots) {
        News news;
        for (DataSnapshot dataSnapshot : dataSnapshots) {
            if (dataSnapshot.getValue() != null) {
                news = dataSnapshot.getValue(News.class);
                news.setNewsId(dataSnapshot.getKey());
                if (daoSessionWeakReference.get() != null)
                    daoSessionWeakReference.get().getNewsDao().insertOrReplace(news);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(News news) {
        if (daoOperationCompleteWeakReference.get() != null)
            daoOperationCompleteWeakReference.get().onDaoOperationComplete(news);
        super.onPostExecute(news);
    }
}
