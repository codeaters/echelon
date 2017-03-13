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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.innovationweek.model.Event;
import com.app.innovationweek.model.Phase;
import com.app.innovationweek.model.Rule;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    private static final String ARG_EVENT = "event";

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.desc)
    TextView description;
    @BindView(R.id.rules)
    LinearLayout rules;
    @BindView(R.id.phases)
    LinearLayout phases;
    @BindView(R.id.start_date)
    TextView startDate;
    @BindView(R.id.event_icon)
    ImageView icon;
    @BindView(R.id.goto_event)
    Button gotoEvent;
    @BindView(R.id.leaderboard)
    Button leaderboard;

    /**
     * The event model being displayed in this Fragment
     */
    private Event event;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMM");

    public EventFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static EventFragment newInstance(Event event) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_EVENT, event);
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
        event = bundle.getParcelable(ARG_EVENT);

        name.setText(event.getName());
        description.setText(event.getDescription());
        startDate.setText(getString(R.string.event_date, dateFormat.format(new Date(event.getStartDate()))));
        if (event.getImageUrl() != null)
            Picasso.with(getActivity().getApplicationContext()).load(event.getImageUrl()).placeholder
                    (ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.event)).into(icon);
        else
            icon.setImageDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.event));
        if (event.getPhases() != null && event.getPhases().size() > 0) {
            //hide rules show phases
            rules.setVisibility(View.GONE);
            startDate.setVisibility(View.GONE);
            for (Phase phase : event.getPhases()) {
                LinearLayout phaseView = (LinearLayout) inflater.inflate(R.layout.phase, phases, false);
                ((TextView) phaseView.findViewById(R.id.phase_title)).setText(phase.getName());
                /*
                 * TODO: have a phase name and sort order, atleast name is required order might be
                 * maintained
                 */
                ((TextView) phaseView.findViewById(R.id.start_date)).setText(getString(R.string
                                .event_date,
                        dateFormat.format
                                (new Date(phase.getStartDate()))));
                if (phase.getRules() != null && phase.getRules().size() > 0) {
                    LinearLayout rules = (LinearLayout) phaseView.findViewById(R.id.rules);
                    for (Rule rule : phase.getRules()) {
                        TextView ruleView = (TextView) inflater.inflate(R.layout.rule, phaseView,
                                false);
                        ruleView.setText(rule.getRule());
                        rules.addView(ruleView);
                    }
                }
                phases.addView(phaseView);
            }
        } else if (event.getRules() != null && event.getRules().size() > 0) {
            //rules present simple event
            phases.setVisibility(View.GONE);
            for (Rule rule : event.getRules()) {
                TextView ruleView = (TextView) inflater.inflate(R.layout.rule, rules, false);
                ruleView.setText(rule.getRule());
                rules.addView(ruleView);
            }
        }

        if (event.getId().equals("event_id1"))
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
