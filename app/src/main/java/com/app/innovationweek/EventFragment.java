package com.app.innovationweek;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.innovationweek.model.Event;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zeeshan on 3/7/2017.
 */

public class EventFragment extends Fragment implements View.OnClickListener {
    /**
     * A placeholder fragment containing a simple view.
     */

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_EVENT_NAME = "event_name";
    private static final String ARG_EVENT_DESC = "event_desc";
    private static final String ARG_EVENT_RULES = "event_rules";
    private static final String ARG_EVENT_START_DATE = "event_date";
    private static final String ARG_EVENT_ID = "event_id";
    private static final String ARG_EVENT_ICON_ULR = "icon_url";

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.desc)
    TextView description;
    @BindView(R.id.rules)
    TextView rules;
    @BindView(R.id.start_date)
    TextView startDate;
    @BindView(R.id.event_icon)
    ImageView icon;
    @BindView(R.id.goto_event)
    Button gotoEvent;
    @BindView(R.id.leaderboard)
    Button leaderboard;

    public EventFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static EventFragment newInstance(Event event) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_NAME, event.getName());
        args.putString(ARG_EVENT_DESC, event.getDescription());
        args.putString(ARG_EVENT_RULES, event.getRule());
        args.putLong(ARG_EVENT_START_DATE, event.getStartDate());
        args.putString(ARG_EVENT_ID, event.getId());
        args.putString(ARG_EVENT_ICON_ULR, event.getImageUrl());
        fragment.setArguments(args);
        //TODO: fetch this info in fragment and populate the data accordingly
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, rootView);
        // extract event details from the bundle set in the static method above
        Bundle bundle = getArguments();
        String eventId, eventName, eventDesc, eventRules, eventIconUrl;
        long eventStartDate;
        eventName = bundle.getString(ARG_EVENT_NAME);
        eventDesc = bundle.getString(ARG_EVENT_DESC);
        eventRules = bundle.getString(ARG_EVENT_RULES);
        eventStartDate = bundle.getLong(ARG_EVENT_START_DATE);
        eventIconUrl = bundle.getString(ARG_EVENT_ICON_ULR);
        eventId = bundle.getString(ARG_EVENT_ID);
        name.setText(eventName);
        description.setText(eventDesc);
        rules.setText(eventRules);
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMM");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(eventStartDate);
        startDate.setText(getString(R.string.event_date, dateFormat.format(cal.getTime())));
        Picasso.with(getActivity().getApplicationContext()).load(eventIconUrl).placeholder
                (ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.event)).into(icon);
        if (eventId.equals("event_id3"))
            gotoEvent.setVisibility(View.VISIBLE);
        else
            gotoEvent.setVisibility(View.GONE);
        gotoEvent.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goto_event:
                Intent intent = new Intent(getActivity(), QuestionActivity.class);
                intent.putExtra("quiz_id", "questions");
                intent.putExtra("question_id", "qid1");
                startActivity(intent);
                break;
        }
    }
}
