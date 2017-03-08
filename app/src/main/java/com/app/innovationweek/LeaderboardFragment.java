package com.app.innovationweek;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zeeshan on 3/7/2017.
 */

public class LeaderboardFragment extends Fragment {
    /**
     * A placeholder fragment containing a simple view.
     */

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public LeaderboardFragment() {
    }

    @BindView(R.id.leaderboard_title)
    TextView leaderBoardTitle;

    @BindView(R.id.leaderboard_recycler_view)
    RecyclerView recyclerView;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static LeaderboardFragment newInstance(int sectionNumber) {
        LeaderboardFragment fragment = new LeaderboardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        ButterKnife.bind(this,rootView);
        return rootView;
    }
}
