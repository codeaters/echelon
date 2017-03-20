package com.app.iw;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.innovationweek.R;
import com.app.iw.callbacks.DaoOperationComplete;
import com.app.iw.loader.EventAsyncTaskLoader;
import com.app.iw.model.Event;
import com.app.iw.model.Team;
import com.app.iw.model.User;
import com.app.iw.model.dao.DaoSession;
import com.app.iw.model.dao.EventDao;
import com.app.iw.model.dao.TeamDao;
import com.app.iw.service.UserFetchService;
import com.app.iw.util.EventInsertTask;
import com.app.iw.util.EventUpdateTask;
import com.app.iw.util.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
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

public class MainActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<List<Event>>, View.OnClickListener, DaoOperationComplete {
    public static final String TAG = MainActivity.class.getSimpleName();
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.progress)
    View progress;
    @BindView(R.id.progress_msg)
    TextView progressMsg;
    @BindView(R.id.error)
    View error;
    @BindView(R.id.error_msg)
    TextView errorMsg;
    @BindView(R.id.retry)
    Button retry;
    @BindView(R.id.hidden_button)
    Button hiddenButton;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private MainActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private DatabaseReference eventsRef, userCanThinkQuickRef, usersUpdatedRef;
    private ChildEventListener eventChildListener;
    private ValueEventListener eventValueEventListener;
    private List<DataSnapshot> eventSnapShots;
    private DaoSession daoSession;
    private ValueEventListener userCanThinkValueEventListener, usersUpdatedRefValueEventListener;
    private boolean allEventsFetched;
    private GoogleApiClient mGoogleApiClient;
    private int currentPage;

    {
        eventSnapShots = new ArrayList<>();
        eventChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "event added" + dataSnapshot);
                if (allEventsFetched) {
                    new EventUpdateTask(daoSession, MainActivity.this).execute(dataSnapshot);
                    return;
                }
                eventSnapShots.add(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "event changed" + dataSnapshot);
                if (dataSnapshot.getValue() != null) {
                    //dont update using this task it will delete all and insert this one
                    new EventUpdateTask(daoSession, MainActivity.this).execute(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null)
                    daoSession.getEventDao().queryBuilder()
                            .where(EventDao.Properties.Id.eq(dataSnapshot.getKey()))
                            .buildDelete()
                            .executeDeleteWithoutDetachingEntities();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.toString());
            }
        };
        eventValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //this callback is garanteed to be called last so we collect datasnapshots in ChildEventListnere and start inserting them here
                if (eventSnapShots.size() > 0) {
                    Log.d(TAG, "inserting events " + eventSnapShots.size());
                    new EventInsertTask(daoSession, MainActivity.this).execute(eventSnapShots.toArray(new DataSnapshot[eventSnapShots.size()]));
                    allEventsFetched = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.toString());
            }
        };

        userCanThinkValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                saveSingleUser(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "User fetch for canThinkQuick failed.");
            }
        };
        usersUpdatedRefValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "" + dataSnapshot);
                if (Utils.isUsersFetched(getApplication().getApplicationContext())) {
                    if (dataSnapshot.getValue(Boolean.class)) {
                        Log.d(TAG, "fetching users");
                        UserFetchService.startFetchingUser(getApplicationContext());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        retry.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), null);
        // Set up the ViewPager with the sections adapter.

        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getInt("current_page");
        }
        //subscribe to topic for FCM notifications
        Log.d(TAG, ": Subscribing to Topic defaultTopic");
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        eventsRef = dbRef.child("events");
        usersUpdatedRef = dbRef.child("usersUpdated");

        String uid = Utils.getUid(getApplicationContext());
        if (Utils.isLoggedIn(getApplicationContext()) && uid != null && !uid.isEmpty()) {
            userCanThinkQuickRef = FirebaseDatabase.getInstance().getReference("users").child(uid);
            userCanThinkQuickRef.addValueEventListener(userCanThinkValueEventListener);
        }

        daoSession = ((EchelonApplication) getApplication()).getDaoSession();

        getSupportLoaderManager().initLoader(0, null, this);
        hiddenButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "fetching events");
        if (!Utils.isUsersFetched(getApplicationContext())) {
            Log.d(TAG, "fetching users");
            UserFetchService.startFetchingUser(getApplicationContext());
        }
        usersUpdatedRef.addValueEventListener(usersUpdatedRefValueEventListener);
        eventsRef.addChildEventListener(eventChildListener);
        eventsRef.addListenerForSingleValueEvent(eventValueEventListener);
        checkPlayServices();
        super.onStart();
    }

    @Override
    protected void onPause() {
        currentPage = mViewPager.getCurrentItem();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mViewPager.setCurrentItem(currentPage);
        super.onResume();
    }

    @Override
    protected void onStop() {
        eventsRef.removeEventListener(eventChildListener);
        usersUpdatedRef.removeEventListener(usersUpdatedRefValueEventListener);
        if (Utils.isLoggedIn(getApplicationContext()) && userCanThinkQuickRef != null) {
            userCanThinkQuickRef.removeEventListener(userCanThinkValueEventListener);
        }
        allEventsFetched = false;
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (Utils.isSpecialUser(getApplicationContext())) {
            getMenuInflater().inflate(R.menu.menu_activity, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.special:
                String msg = Utils.getSpecialMessage(getApplicationContext());
                if (msg != null)
                    Snackbar.make(mViewPager, msg, Snackbar.LENGTH_INDEFINITE).setAction("OK",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //noinspection SimplifiableIfStatement

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("current_page", currentPage);
        super.onSaveInstanceState(outState);
    }

    private void saveSingleUser(DataSnapshot singleUser) {
        Team team;
        User user;
        String teamName = singleUser.child("team").getValue(String.class);
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
        Log.d(TAG, "UpdatedUser as part of canThinkQuick update:" + user.getName());
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, 1).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public Loader<List<Event>> onCreateLoader(int id, Bundle args) {
        EventAsyncTaskLoader loader = new EventAsyncTaskLoader(getApplicationContext());
        Log.d(TAG, "event loader created");
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> data) {
        Log.d(TAG, "event laded " + (data == null ? 0 : data.size()));
        mSectionsPagerAdapter.setPages(data);
        hideProgress(false, null);
    }

    @Override
    public void onLoaderReset(Loader<List<Event>> loader) {
        Log.d(TAG, "loader reset");
    }

    public void showProgress(String msg) {
        if (progressMsg != null)
            progressMsg.setText(msg == null || msg.isEmpty() ? getString(R.string.loading_event) : msg);
        mViewPager.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

    }

    public void hideProgress(boolean showError, String errorMsg) {
        if (showError) {
            if (error != null) error.setVisibility(View.VISIBLE);
            if (mViewPager != null) mViewPager.setVisibility(View.GONE);
            if (progress != null) progress.setVisibility(View.GONE);
            if (this.errorMsg != null)
                this.errorMsg.setText(errorMsg == null || errorMsg.isEmpty() ?
                        getString(R.string.error_general) : errorMsg);
        } else {
            if (mViewPager != null) mViewPager.setVisibility(View.VISIBLE);
            if (progress != null) progress.setVisibility(View.GONE);
            if (error != null) error.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.retry:
                Log.d(TAG, "trying again");
                break;
            case R.id.hidden_button:
                Toast.makeText(getApplicationContext(), "Codeaters: Zeeshan & Danish", Toast.LENGTH_SHORT).show();
            default:
                Log.d(TAG, "click not implemented");
        }
    }

    @Override
    public void onDaoOperationComplete(Object object) {
        /*
        * saving users fetched state in sharedpreferences so that we don't retreive same data
        * multiple times.
         */
        if (object == null) {
            getSupportLoaderManager().getLoader(0).forceLoad();
            eventSnapShots.clear();
            return;
        } else if (object instanceof Event) {
            if (mSectionsPagerAdapter != null) mSectionsPagerAdapter.updateEvent((Event) object);
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<Event> pages = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm, List<Event> pages) {
            super(fm);
            this.pages = pages;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // The first fragment must be a What's new fragment, which lists all the small events
            if (position == 0)
                return NewsFragment.newInstance();
            else
                return EventFragment.newInstance(pages.get(position - 1));
        }

        @Override
        public int getCount() {
            // Show number of events and a news page
            return pages == null ? 1 : pages.size() + 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0)
                return "News";
            return pages.get(position - 1).getName();
        }

        public void setPages(List<Event> pages) {
            this.pages = pages;
            notifyDataSetChanged();
        }

        void updateEvent(@NonNull Event event) {
            for (int i = 0; i < pages.size(); i++) {
                if (pages.get(i).getId().equals(event.getId())) {
                    pages.set(i, event);
                    notifyDataSetChanged();
                    break;
                }
            }
        }
    }

}
