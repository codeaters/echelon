package com.app.innovationweek.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.app.innovationweek.EchelonApplication;
import com.app.innovationweek.model.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zeeshan on 3/8/2017.
 */

public class EventAsyncTaskLoader extends AsyncTaskLoader<List<Object>> {
    public EventAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public List<Object> loadInBackground() {
        List<Object> objects = new ArrayList<>();
        List<Event> events = ((EchelonApplication) getContext().getApplicationContext()).getDaoSession()
                .getEventDao().loadAll();
        for (Event event : events)
            objects.add(event);

        Event dummy = new Event();
        dummy.setName("Highlights");
        objects.add(0, dummy);

        return objects;
    }
}
