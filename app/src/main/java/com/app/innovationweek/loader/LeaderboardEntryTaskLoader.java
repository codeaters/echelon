package com.app.innovationweek.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.app.innovationweek.EchelonApplication;
import com.app.innovationweek.model.LeaderboardEntry;
import com.app.innovationweek.model.dao.LeaderboardEntryDao;

import java.util.List;

/**
 * Created by zeeshan on 3/14/2017.
 */

public class LeaderboardEntryTaskLoader extends AsyncTaskLoader<List<LeaderboardEntry>> {
    private List<LeaderboardEntry> leaderboardEntries;
    private String quizId;

    public LeaderboardEntryTaskLoader(Context context, String quizId) {
        super(context);
        this.quizId = quizId;
    }

    @Override
    public List<LeaderboardEntry> loadInBackground() {
        leaderboardEntries = ((EchelonApplication) getContext().getApplicationContext()).getDaoSession()
                .getLeaderboardEntryDao().queryBuilder().where(LeaderboardEntryDao.Properties
                        .LeaderboardId.eq(quizId)).orderDesc(LeaderboardEntryDao.Properties
                        .Score).orderAsc(LeaderboardEntryDao.Properties.TotalTime).list();
        return leaderboardEntries;
    }

    @Override
    protected void onStartLoading() {
        if (leaderboardEntries != null) {
            deliverResult(leaderboardEntries);
        } else {
            // We have no data, so kick off loading it
            forceLoad();
        }
    }

}
