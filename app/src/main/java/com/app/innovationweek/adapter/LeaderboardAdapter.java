package com.app.innovationweek.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.innovationweek.R;
import com.app.innovationweek.model.LeaderboardEntry;
import com.app.innovationweek.model.holder.LeaderboardEntryHolder;

import java.util.Collections;
import java.util.List;

/**
 * Created by zeeshan on 3/11/2017.
 */

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardEntryHolder> implements
        View.OnClickListener {
    public static final String TAG = LeaderboardAdapter.class.getSimpleName();
    private List<LeaderboardEntry> leaderboardEntryList;
    private String leaderboardType;

    public LeaderboardAdapter(List<LeaderboardEntry> leaderboardEntries, String leaderboardType) {
        this.leaderboardEntryList = leaderboardEntries;
        this.leaderboardType = leaderboardType;
        sort();
    }

    @Override
    public LeaderboardEntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (leaderboardType) {
            case LEADERBOARD_TYPE.RANK:
            case LEADERBOARD_TYPE.SCORE:
            case LEADERBOARD_TYPE.SCORE_TIME:
                return new LeaderboardEntryHolder(LayoutInflater.from(parent.getContext()).inflate(R
                        .layout.item_leaderboard_static, parent, false), this, leaderboardType);
            default:
                return new LeaderboardEntryHolder(LayoutInflater.from(parent.getContext()).inflate(R
                        .layout.item_leaderboard, parent, false), this, leaderboardType);
        }
    }

    @Override
    public void onBindViewHolder(LeaderboardEntryHolder holder, int position) {
        holder.setLeaderboardEntry(position, leaderboardEntryList.get(position));
    }

    @Override
    public int getItemCount() {
        return leaderboardEntryList == null ? 0 : leaderboardEntryList.size();
    }

    public void setLeaderboardEntryList(List<LeaderboardEntry> leaderboardEntryList) {
        this.leaderboardEntryList = leaderboardEntryList;
        sort();
        notifyDataSetChanged();
    }

    public void updateLeaderboardEntry(@NonNull LeaderboardEntry leaderboardEntry) {
        switch (leaderboardType) {
            case LEADERBOARD_TYPE.RANK:
            case LEADERBOARD_TYPE.SCORE:
                updateAndSort(leaderboardEntry);
                break;
            case LEADERBOARD_TYPE.SCORE_TIME:
            default:
                updateBySortTime(leaderboardEntry);
        }
    }

    private void updateAndSort(LeaderboardEntry leaderboardEntry) {
        int index = 0;
        boolean found = false;
        for (LeaderboardEntry le : leaderboardEntryList) {
            if (le.getUserId().equals(leaderboardEntry.getUserId())) {
                found = true;
                break;
            }
            index++;
        }
        if (found) {
            leaderboardEntryList.set(index, leaderboardEntry);
        } else {
            leaderboardEntryList.add(leaderboardEntry);
        }
        sort();
    }

    private void updateBySortTime(LeaderboardEntry leaderboardEntry) {
        int index = 0;
        int oldIndex = 0;
        boolean found = false;
        for (LeaderboardEntry le : leaderboardEntryList) {
            if (le.getUserId().equals(leaderboardEntry.getUserId())) {
                leaderboardEntryList.remove(oldIndex);
                found = true;
                break;
            }
            oldIndex++;
        }
        for (LeaderboardEntry le : leaderboardEntryList) {
            if (leaderboardEntry.getScore() > le.getScore()) {
                leaderboardEntryList.add(index, leaderboardEntry);
                Log.d(TAG, "added at" + index);
                break;
            } else if (leaderboardEntry.getScore() == le.getScore()) {
                if (leaderboardEntry.getTotalTime() < le.getTotalTime()) {
                    leaderboardEntryList.add(index, leaderboardEntry);
                    Log.d(TAG, "added at" + index);
                    break;
                } else if (leaderboardEntry.getTotalTime() == le.getTotalTime()) {
                    int order = leaderboardEntry.getUser().getUsername().compareTo(le.getUser().getUsername());
                    if (order < 0) {
                        leaderboardEntryList.add(index, leaderboardEntry);
                        Log.d(TAG, "added at" + index);
                        break;
                    }
                }
            }
            index++;
        }
        if (index == leaderboardEntryList.size())
            leaderboardEntryList.add(leaderboardEntry);
        Log.d(TAG, "OldIndex=" + oldIndex + ", index=" + index);
        if (!found) {
            notifyItemInserted(index);
            notifyItemRangeChanged(index, getItemCount() - index);
        } else if (oldIndex == index)
            notifyItemChanged(oldIndex);
        else {
            notifyItemMoved(oldIndex, index);
            notifyItemRangeChanged(index < oldIndex ? index : oldIndex, Math.abs(oldIndex -
                    index) + 1);
        }
        Log.d(TAG, "list:" + leaderboardEntryList.toString());
    }

    @Override
    public void onClick(View view) {
        String msg;
        switch (view.getId()) {
            case R.id.team:
                msg = "This is team name";
                break;
            case R.id.time:
                msg = "This is total time taken to answer the questions";
                break;
            case R.id.score:
                msg = "This is total score received";
                break;
            case R.id.correct_count:
                msg = "Number of question answered correctly";
                break;
            case R.id.incorrect_count:
                msg = "Number of question answered incorrectly";
                break;
            default:
                msg = "Click not implmented";
        }
        Toast.makeText(view.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void sort() {
        switch (leaderboardType) {
            case LEADERBOARD_TYPE.SCORE:
                Collections.sort(leaderboardEntryList, LeaderboardEntry.SCORE_DEC);
                break;
            case LEADERBOARD_TYPE.RANK:
                Collections.sort(leaderboardEntryList, LeaderboardEntry.RANK);
                break;
            case LEADERBOARD_TYPE.SCORE_TIME:
            default:
                Collections.sort(leaderboardEntryList, LeaderboardEntry.SCORE_DEC_TIME_ASC);
        }
        notifyDataSetChanged();
    }

    public interface LEADERBOARD_TYPE {
        String SCORE = "score";
        String RANK = "rank";
        String SCORE_TIME = "score_time";
    }


}
