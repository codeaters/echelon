package com.app.innovationweek;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.app.innovationweek.loader.EventAsyncTaskLoader;
import com.app.innovationweek.model.Event;

import java.util.List;

public class EventsActivity extends AppCompatActivity implements LoaderManager
        .LoaderCallbacks<List<Event>> {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private EventsActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), null);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<List<Event>> onCreateLoader(int id, Bundle args) {
        EventAsyncTaskLoader loader = new EventAsyncTaskLoader(getApplicationContext());
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> data) {
        mSectionsPagerAdapter.setEvents(data);
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Event>> loader) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private List<Event> events;

        public SectionsPagerAdapter(FragmentManager fm, List<Event> events) {
            super(fm);
            this.events = events;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return EventFragment.newInstance(events.get(position));
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return events == null ? 0 : events.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }

        public void setEvents(List<Event> events) {
            this.events = events;
        }
    }
}
