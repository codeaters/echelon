package com.app.innovationweek;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.app.innovationweek.adapter.LeaderboardAdapter;
import com.app.innovationweek.callbacks.DaoOperationComplete;
import com.app.innovationweek.callbacks.OnNewUserFoundClbk;
import com.app.innovationweek.loader.LeaderboardEntryTaskLoader;
import com.app.innovationweek.model.LeaderboardEntry;
import com.app.innovationweek.model.dao.DaoSession;
import com.app.innovationweek.service.UserFetchService;
import com.app.innovationweek.util.LeaderboardEntryUpdateTask;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeaderboardActivity extends AppCompatActivity implements DaoOperationComplete,
        LoaderManager.LoaderCallbacks<List<LeaderboardEntry>>, OnNewUserFoundClbk {
    public static final String TAG = LeaderboardActivity.class.getSimpleName();
    @BindView(R.id.leaderboard)
    RecyclerView recyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private String quizId, quizName, leaderboardType;
    private DatabaseReference leaderboardRef, userRef;
    private ChildEventListener leaderboardEntryChildEventListener;
    private ValueEventListener leaderboardEntryValueEventListener;
    private DaoSession daoSession;
    private boolean allLeaderboardItemloaded;
    private List<DataSnapshot> dataSnapshots;
    private BroadcastReceiver localBroadcastReceiver;

    {

        dataSnapshots = new ArrayList<>();
        leaderboardEntryChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "childAdded " + dataSnapshot);
                if (allLeaderboardItemloaded) {
                    new LeaderboardEntryUpdateTask(daoSession, quizId,leaderboardType,
                            LeaderboardActivity
                            .this, LeaderboardActivity.this).execute(dataSnapshot);
                    return;
                }
                dataSnapshots.add(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "childUpdated " + dataSnapshot);
                new LeaderboardEntryUpdateTask(daoSession, quizId,leaderboardType, LeaderboardActivity
                        .this, LeaderboardActivity.this).execute(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //TODO: delete leaderboard entry
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        leaderboardEntryValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //all ileaderboard item loaded insert them in to database
                new LeaderboardEntryUpdateTask(daoSession, quizId,leaderboardType, LeaderboardActivity
                        .this, LeaderboardActivity.this).execute(dataSnapshots.toArray(new
                        DataSnapshot[dataSnapshots
                        .size()]));
                allLeaderboardItemloaded = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        localBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("leaderboard-updates-broadcasts")) {
                    getSupportLoaderManager().getLoader(0).forceLoad();
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            if (getIntent() != null) {
                if (getIntent().hasExtra("quiz_id")) quizId = getIntent().getStringExtra("quiz_id");
                if (getIntent().hasExtra("quiz_name"))
                    quizName = getIntent().getStringExtra("quiz_name");
                if (getIntent().hasExtra("leaderboard_type"))
                    leaderboardType = getIntent().getStringExtra("leaderboard_type");
            }
        } else {
            quizId = savedInstanceState.getString("quiz_id", "");
            quizName = savedInstanceState.getString("quiz_name", "");
            leaderboardType = savedInstanceState.getString("leaderboard_type", "");
            allLeaderboardItemloaded = savedInstanceState.getBoolean("allLeaderboardItemloaded",
                    false);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        leaderboardAdapter = new LeaderboardAdapter(new ArrayList<LeaderboardEntry>(),leaderboardType);
        recyclerView.setAdapter(leaderboardAdapter);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        leaderboardRef = dbRef.child("leaderboard").child(quizId);
//        userRef = dbRef.child("users");
//        userRef.addListenerForSingleValueEvent(userValueListener);
        daoSession = ((EchelonApplication) getApplication()).getDaoSession();
        getSupportLoaderManager().initLoader(0, null, this);
        leaderboardRef.addListenerForSingleValueEvent(leaderboardEntryValueEventListener);
        setTitle(quizName);
        Log.d(TAG, allLeaderboardItemloaded + ":" + quizName + ":" + quizId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("quiz_id", quizId);
        outState.putString("quiz_name", quizName);
        outState.putBoolean("allLeaderboardItemloaded", allLeaderboardItemloaded);
        outState.putString("leaderboard_type", leaderboardType);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        leaderboardRef.addChildEventListener(leaderboardEntryChildEventListener);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver
                (localBroadcastReceiver, new IntentFilter("leaderboard-updates-broadcasts"));
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "activity stopped, removing listeners");
        leaderboardRef.removeEventListener(leaderboardEntryChildEventListener);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(localBroadcastReceiver);
        super.onStop();
    }


    @Override
    public void onDaoOperationComplete(Object object) {
        if (object != null && object instanceof LeaderboardEntry) {
            //logic to update specific leaderboard entity
            Log.d(TAG, "updated/inserted: " + object.toString());
            leaderboardAdapter.updateLeaderboardEntry((LeaderboardEntry) object);
        } else {
            //leaderboard entities bulk update or1
            //users bulk update
            Log.d(TAG, "loader reset");
            getSupportLoaderManager().getLoader(0).forceLoad();
            dataSnapshots.clear();
        }
    }

    @Override
    public Loader<List<LeaderboardEntry>> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "loader created");
        return new LeaderboardEntryTaskLoader(getApplicationContext(), quizId);
    }

    @Override
    public void onLoadFinished(Loader<List<LeaderboardEntry>> loader, List<LeaderboardEntry> data) {
        Log.d(TAG, "loading finish" + data.size());
        leaderboardAdapter.setLeaderboardEntryList(data);
    }

    @Override
    public void onLoaderReset(Loader<List<LeaderboardEntry>> loader) {
        Log.d(TAG, "loading reset");
    }

    @Override
    public void onNewUser(String userId, String leaderboardId) {
        UserFetchService.startFetchingUserAndLeaderboardEntry(getApplicationContext(), userId, leaderboardId);
    }

}
