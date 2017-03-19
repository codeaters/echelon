package com.app.innovationweek.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.innovationweek.EchelonApplication;
import com.app.innovationweek.callbacks.DaoOperationComplete;
import com.app.innovationweek.model.Team;
import com.app.innovationweek.model.User;
import com.app.innovationweek.model.dao.DaoSession;
import com.app.innovationweek.model.dao.TeamDao;
import com.app.innovationweek.util.LeaderboardEntryUpdateTask;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserFetchService extends IntentService implements DaoOperationComplete {
    public static final String TAG = UserFetchService.class.getSimpleName();
    User user;
    Team team;
    String teamName;
    ValueEventListener userValueListener, singleUserValueEventListener,
            leaderboardEntryValueListener;
    String leaderboardId;
    String userId;
    private DaoSession daoSession;

    {
        userValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "users datasnap changed");
                saveUser(dataSnapshot);
                //fetch leaderboard entry here
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.toString());
            }
        };
        singleUserValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "user datasnap changed");
                saveSingleUser(dataSnapshot);
                FirebaseDatabase.getInstance().getReference("leaderboard").child(leaderboardId)
                        .child(userId)
                        .addListenerForSingleValueEvent
                                (leaderboardEntryValueListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        leaderboardEntryValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                saveSingleLeaderboardEntry(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    public UserFetchService() {
        super("UserFetchService");
    }

    public static void startFetchingUser(Context context) {
        Intent intent = new Intent(context, UserFetchService.class);
        context.startService(intent);
    }

    public static void startFetchingUserAndLeaderboardEntry(Context context, String userId, String
            leaderboardId) {
        Intent intent = new Intent(context, UserFetchService.class);
        intent.setAction("FETCH_ONE_USER");
        intent.putExtra("USER_ID", userId);
        intent.putExtra("LEADERBOARD_ID", leaderboardId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("FETCH_ONE_USER")) {
            userId = intent.getStringExtra("USER_ID");
            leaderboardId = intent.getStringExtra("LEADERBOARD_ID");
            FirebaseDatabase.getInstance().getReference("users").child(userId)
                    .addListenerForSingleValueEvent
                            (singleUserValueEventListener);
            return;
        }
        FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent
                (userValueListener);
    }

    private void saveSingleUser(DataSnapshot singleUser) {
        daoSession = ((EchelonApplication) getApplication()).getDaoSession();
        teamName = singleUser.child("team").getValue(String.class);
        List<Team> teams = daoSession.getTeamDao().queryBuilder().where(TeamDao
                .Properties.Name.eq(teamName)).build().list();
        if (teams.size() == 0) {
            team = new Team();
            team.setName(teamName);
            daoSession.getTeamDao().insert(team);
        } else {
            team = teams.get(0);
        }
        user = new User();
        user.setId(singleUser.getKey());
        user.setName(singleUser.child("name").getValue(String.class));
        user.setUsername(singleUser.child("username").getValue(String.class));
        user.setTeam(team);
        user.setCanThinkQuick(singleUser.hasChild("canThinkQuick") ? singleUser.child("canThinkQuick").getValue(Boolean.class) : false);
        daoSession.getUserDao().insertOrReplace(user);
        Log.d(TAG, "updatedUser:" + user.getName());
    }

    void saveSingleLeaderboardEntry(DataSnapshot leaderboardEntry) {
        daoSession = ((EchelonApplication) getApplication()).getDaoSession();
        new LeaderboardEntryUpdateTask(daoSession, leaderboardId, this, null).execute(leaderboardEntry);
    }

    private void saveUser(DataSnapshot usersDataSnapshot) {
        for (DataSnapshot userDs : usersDataSnapshot.getChildren()) {
            saveSingleUser(userDs);
        }
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                .putBoolean("is_users_fetched", true).apply();
    }

    @Override
    public void onDaoOperationComplete(Object object) {
        Intent intent = new Intent("leaderboard-updates-broadcasts");
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
}
