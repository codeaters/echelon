package com.app.innovationweek.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.app.innovationweek.EchelonApplication;
import com.app.innovationweek.model.Team;
import com.app.innovationweek.model.User;
import com.app.innovationweek.model.dao.DaoSession;
import com.app.innovationweek.model.dao.TeamDao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserFetchService extends IntentService {
    public static final String TAG = UserFetchService.class.getSimpleName();
    private DaoSession daoSession;
    ValueEventListener userValueListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Log.d(TAG, "users datasnap changed");
            saveUser(dataSnapshot);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d(TAG, databaseError.toString());
        }
    };

    public UserFetchService() {
        super("UserFetchService");
    }

    public static void startFetchingUser(Context context) {
        Intent intent = new Intent(context, UserFetchService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent
                (userValueListener);
    }

    private void saveUser(DataSnapshot usersDataSnapshot) {
        User user;
        Team team;
        daoSession = ((EchelonApplication) getApplication()).getDaoSession();
        for (DataSnapshot userDs : usersDataSnapshot.getChildren()) {
            String teamName = userDs.child("team").getValue(String.class);
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
            user.setId(userDs.getKey());
            user.setName(userDs.child("name").getValue(String.class));
            user.setUsername(userDs.child("username").getValue(String.class));
            user.setTeam(team);
            daoSession.getUserDao().insertOrReplace(user);
            Log.d(TAG, "updatedUser:" + user.getName());
        }
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                .putBoolean("is_users_fetched", true).apply();
    }
}
