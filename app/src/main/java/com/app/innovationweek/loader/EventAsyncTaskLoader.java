package com.app.innovationweek.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.app.innovationweek.EchelonApplication;
import com.app.innovationweek.model.Event;

import java.util.List;

/**
 * Created by zeeshan on 3/8/2017.
 */

public class EventAsyncTaskLoader extends AsyncTaskLoader<List<Event>> {
    private List<Event> events;

    public EventAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public List<Event> loadInBackground() {
        events = ((EchelonApplication) getContext().getApplicationContext()).getDaoSession()
                .getEventDao().loadAll();
        return events;
    }

    @Override
    protected void onStartLoading() {
        if (events != null) {
            deliverResult(events);
        } else {
            // We have no data, so kick off loading it
            forceLoad();
        }
    }
}
