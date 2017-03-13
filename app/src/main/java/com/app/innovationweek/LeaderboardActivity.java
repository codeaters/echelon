package com.app.innovationweek;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.app.innovationweek.adapter.LeaderboardAdapter;
import com.app.innovationweek.callbacks.DaoOperationComplete;
import com.app.innovationweek.loader.LeaderboardEntryTaskLoader;
import com.app.innovationweek.model.LeaderboardEntry;
import com.app.innovationweek.model.dao.DaoSession;
import com.app.innovationweek.util.LeaderboardEntryUpdateTask;
import com.app.innovationweek.util.UpdateUsersTask;
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
        LoaderManager.LoaderCallbacks<List<LeaderboardEntry>> {
    public static final String TAG = LeaderboardActivity.class.getSimpleName();
    @BindView(R.id.leaderboard)
    RecyclerView recyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private String quizId;
    private DatabaseReference leaderboardRef, userRef;
    private ChildEventListener leaderboardEntryChildEventListener;
    private ValueEventListener userValueListener;
    private DaoSession daoSession;

    {
        leaderboardEntryChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                new LeaderboardEntryUpdateTask(daoSession, quizId, dataSnapshot, LeaderboardActivity
                        .this).execute();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                new LeaderboardEntryUpdateTask(daoSession, quizId, dataSnapshot, LeaderboardActivity
                        .this).execute();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        userValueListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                new UpdateUsersTask(daoSession, dataSnapshot, LeaderboardActivity.this).execute();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        leaderboardAdapter = new LeaderboardAdapter(new ArrayList<LeaderboardEntry>());
        recyclerView.setAdapter(leaderboardAdapter);
        if (savedInstanceState == null) {
            if (getIntent() != null) {
                quizId = getIntent().getStringExtra("quiz_id");
            }
        } else {
            quizId = savedInstanceState.getString("quiz_id", "generalQuiz");
        }
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        leaderboardRef = dbRef.child("leaderboard").child(quizId);
        userRef = dbRef.child("users");
        daoSession = ((EchelonApplication) getApplication()).getDaoSession();
        userRef.addListenerForSingleValueEvent(userValueListener);
        getSupportLoaderManager().initLoader(0, null, this);
        setTitle(quizId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.getString("quiz_id", quizId);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        leaderboardRef.addChildEventListener(leaderboardEntryChildEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        leaderboardRef.removeEventListener(leaderboardEntryChildEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_leaderboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDaoOperationComplete() {
        getSupportLoaderManager().getLoader(0).forceLoad();
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
}
