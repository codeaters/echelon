package com.app.iw.util;

import android.os.AsyncTask;
import android.util.Log;

import com.app.iw.callbacks.DaoOperationComplete;
import com.app.iw.callbacks.OnNewUserFoundClbk;
import com.app.iw.model.Leaderboard;
import com.app.iw.model.LeaderboardEntry;
import com.app.iw.model.User;
import com.app.iw.model.dao.DaoSession;
import com.app.iw.model.dao.LeaderboardDao;
import com.app.iw.model.dao.LeaderboardEntryDao;
import com.app.iw.model.dao.UserDao;
import com.google.firebase.database.DataSnapshot;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by zeeshan on 3/14/2017.
 */

public class LeaderboardEntryInsertTask extends AsyncTask<DataSnapshot, String, LeaderboardEntry> {
    public static final String TAG = LeaderboardEntryInsertTask.class.getSimpleName();
    private WeakReference<DaoOperationComplete> daoOperationCompleteWeakReference;
    private WeakReference<DaoSession> daoSessionWeakReference;
    private WeakReference<OnNewUserFoundClbk> newUserFoundClbkWeakReference;
    private String quizId, leaderboardType;

    public LeaderboardEntryInsertTask(DaoSession daoSession, String quizId, String leaderboardType,
                                      DaoOperationComplete
                                              daoOperationComplete, OnNewUserFoundClbk onNewUserFoundClbk) {
        if (daoOperationComplete != null)
            daoOperationCompleteWeakReference = new WeakReference<>(daoOperationComplete);
        daoSessionWeakReference = new WeakReference<>(daoSession);
        if (onNewUserFoundClbk != null)
            newUserFoundClbkWeakReference = new WeakReference<>(onNewUserFoundClbk);
        this.quizId = quizId;
        this.leaderboardType = leaderboardType;
    }

    @Override
    protected LeaderboardEntry doInBackground(DataSnapshot... dataSnapshots) {
        Leaderboard leaderboard;
        List<Leaderboard> leaderboardList;
        List<User> userList;
        String userId;
        LeaderboardEntry leaderboardEntry = null;
        List<LeaderboardEntry> leaderboardEntryList;
        daoSessionWeakReference.get()
                .getLeaderboardEntryDao().queryBuilder().where(LeaderboardEntryDao.Properties
                .LeaderboardId.eq(quizId)).buildDelete().executeDeleteWithoutDetachingEntities();
        leaderboardList = daoSessionWeakReference.get().getLeaderboardDao().queryBuilder().where(LeaderboardDao
                .Properties.Id.eq(quizId)).list();
        if (leaderboardList != null && leaderboardList.size() > 0) {
            leaderboard = leaderboardList.get(0);
        } else {
            leaderboard = new Leaderboard(quizId, leaderboardType);
            if (daoSessionWeakReference.get() != null)
                daoSessionWeakReference.get().getLeaderboardDao().insert(leaderboard);
        }
        for (DataSnapshot entrySnapshot : dataSnapshots) {
            userId = entrySnapshot.getKey();//or entrySnapshot.child("userId").getValue(String
            // .class);
            User user;
            userList = daoSessionWeakReference.get().getUserDao().queryBuilder().where
                    (UserDao.Properties.Id.eq(userId)).list();
            if (userList != null && userList.size() > 0) {
                user = userList.get(0);
            } else {
                //try to fetch that user in
                Log.d(TAG, "invalid user found ");
                publishProgress(userId);
                continue;
            }
            //insert
            leaderboardEntry = new LeaderboardEntry();
            leaderboardEntry.setLeaderboard(leaderboard);
            leaderboardEntry.setUser(user);
            if (entrySnapshot.hasChild("totalScore")) leaderboardEntry.setScore
                    (entrySnapshot.child("totalScore").getValue(Float.class));
            if (entrySnapshot.hasChild("totalTime")) leaderboardEntry.setTotalTime
                    (entrySnapshot.child("totalTime").getValue(Long.class));
            if (entrySnapshot.hasChild("correct")) leaderboardEntry.setCorrect
                    (entrySnapshot.hasChild("correct") ? entrySnapshot.child("correct").getValue(Integer.class) : 0);
            if (entrySnapshot.hasChild("incorrect")) leaderboardEntry.setIncorrect
                    (entrySnapshot.hasChild("incorrect") ? entrySnapshot.child("incorrect").getValue(Integer.class) : 0);
            if (entrySnapshot.hasChild("rank"))
                leaderboardEntry.setRank(entrySnapshot.child("rank").getValue(Integer.class));
            daoSessionWeakReference.get().getLeaderboardEntryDao().insert(leaderboardEntry);

        }
        return null;
    }

    @Override
    protected void onPostExecute(LeaderboardEntry leaderboardEntry) {
        if (daoOperationCompleteWeakReference != null && daoOperationCompleteWeakReference.get() != null)
            daoOperationCompleteWeakReference.get().onDaoOperationComplete(leaderboardEntry);
        super.onPostExecute(leaderboardEntry);
    }

    @Override
    protected void onProgressUpdate(String... userIds) {
        if (newUserFoundClbkWeakReference != null && newUserFoundClbkWeakReference.get() != null)
            newUserFoundClbkWeakReference.get().onNewUser(userIds[0], quizId);
        super.onProgressUpdate(userIds);
    }
}
