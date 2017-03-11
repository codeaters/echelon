package com.app.innovationweek;

import android.os.Bundle;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.innovationweek.loader.EventAsyncTaskLoader;
import com.app.innovationweek.model.Event;
import com.app.innovationweek.model.dao.EventDao;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<List<Event>>, View.OnClickListener {
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
    private DatabaseReference eventsRef;
    private ChildEventListener eventListener;

    private EventDao eventDao;

    {
        eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "event added" + dataSnapshot);
                if (dataSnapshot.getValue() != null) {
                    Event event = dataSnapshot.getValue(Event.class);
                    event.setId(dataSnapshot.getKey());
                    eventDao.insertOrReplace(event);
                    MainActivity.this.getSupportLoaderManager().getLoader(0).forceLoad();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "event changed" + dataSnapshot);
                if (dataSnapshot.getValue() != null) {
                    Event event = dataSnapshot.getValue(Event.class);
                    event.setId(dataSnapshot.getKey());
                    eventDao.insertOrReplace(event);
                    MainActivity.this.getSupportLoaderManager().getLoader(0).forceLoad();
                }
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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
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
        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
        eventsRef = FirebaseDatabase.getInstance().getReference("events");
        eventDao = ((EchelonApplication) getApplication()).getDaoSession().getEventDao();
        showProgress(null);
    }


    @Override
    public Loader<List<Event>> onCreateLoader(int id, Bundle args) {
        EventAsyncTaskLoader loader = new EventAsyncTaskLoader(getApplicationContext());
        Log.d(TAG, "loader created");
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> data) {
        Log.d(TAG, "loading finished" + (data == null ? 0 : data.size()));
        mSectionsPagerAdapter.setPages(data);
        hideProgress(false, null);
        eventsRef.addChildEventListener(eventListener);
    }

    @Override
    public void onLoaderReset(Loader<List<Event>> loader) {
        Log.d(TAG, "loader reset");
    }

    private void showProgress(String msg) {
        if (progressMsg != null)
            progressMsg.setText(msg == null || msg.isEmpty() ? getString(R.string.loading_event) : msg);
        mViewPager.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

    }

    private void hideProgress(boolean showError, String errorMsg) {
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
                return NewsFragment.newInstance("!", "2");
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
    }
}
