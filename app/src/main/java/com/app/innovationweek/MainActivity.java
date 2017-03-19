package com.app.innovationweek;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.app.innovationweek.callbacks.DaoOperationComplete;
import com.app.innovationweek.loader.EventAsyncTaskLoader;
import com.app.innovationweek.model.Event;
import com.app.innovationweek.model.dao.DaoSession;
import com.app.innovationweek.model.dao.EventDao;
import com.app.innovationweek.service.UserFetchService;
import com.app.innovationweek.util.EventInsertTask;
import com.app.innovationweek.util.EventUpdateTask;
import com.app.innovationweek.util.Utils;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<List<Event>>, View.OnClickListener, DaoOperationComplete, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
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
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private MainActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    private DatabaseReference eventsRef, userRef;
    private ChildEventListener eventChildListener;
    private ValueEventListener eventValueEventListener;
    private Map<String, String> currentQuestions;
    private List<DataSnapshot> eventSnapShots;
    private DaoSession daoSession;
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
        if(savedInstanceState!=null){
            currentPage=savedInstanceState.getInt("current_page");
        }
        //subscribe to topic for FCM notifications
        Log.d(TAG, ": Subscribing to Topic defaultTopic");
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        eventsRef = FirebaseDatabase.getInstance().getReference("events");
        daoSession = ((EchelonApplication) getApplication()).getDaoSession();
        userRef = dbRef.child("users");
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "fetching events");
        if (!Utils.isUsersFetched(getApplicationContext())) {
            Log.d(TAG, "fetching users");
            UserFetchService.startFetchingUser(getApplicationContext());
        }
        eventsRef.addChildEventListener(eventChildListener);
        eventsRef.addListenerForSingleValueEvent(eventValueEventListener);
        checkPlayServices();
        super.onStart();
    }

    @Override
    protected void onPause() {
        currentPage=mViewPager.getCurrentItem();
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
        outState.putInt("current_page",currentPage);
        super.onSaveInstanceState(outState);
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (!connectionResult.isSuccess()) {

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
