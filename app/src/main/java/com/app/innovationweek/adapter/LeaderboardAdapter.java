package com.app.innovationweek.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.app.innovationweek.R;
import com.app.innovationweek.model.LeaderboardEntry;
import com.app.innovationweek.model.holder.LeaderboardEntryHolder;

import java.util.List;

/**
 * Created by zeeshan on 3/11/2017.
 */

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardEntryHolder> {
    private List<LeaderboardEntry> leaderboardEntryList;

    public LeaderboardAdapter(List<LeaderboardEntry> leaderboardEntries) {
        this.leaderboardEntryList = leaderboardEntries;
    }

    @Override
    public LeaderboardEntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LeaderboardEntryHolder(LayoutInflater.from(parent.getContext()).inflate(R
                .layout.item_leaderboard, parent, false));
    }

    @Override
    public void onBindViewHolder(LeaderboardEntryHolder holder, int position) {
        holder.setLeaderboardEntry(leaderboardEntryList.get(position));
    }

    @Override
    public int getItemCount() {
        return leaderboardEntryList == null ? 0 : leaderboardEntryList.size();
    }
}
