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
    public EventAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public List<Event> loadInBackground() {
        List<Event> events = ((EchelonApplication) getContext().getApplicationContext()).getDaoSession()
                .getEventDao().loadAll();
        return events;
    }
}
