package com.app.innovationweek.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.app.innovationweek.R;
import com.app.innovationweek.model.Leaderboard;
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
        holder.setLeaderboardEntry(position,leaderboardEntryList.get(position));
    }

    @Override
    public int getItemCount() {
        return leaderboardEntryList == null ? 0 : leaderboardEntryList.size();
    }

    public void setLeaderboardEntryList(List<LeaderboardEntry> leaderboardEntryList) {
        this.leaderboardEntryList = leaderboardEntryList;
        notifyDataSetChanged();
    }
    public void updateLeaderboardEntry(@NonNull LeaderboardEntry leaderboardEntry){
        int index=0;
        int oldIndex=0;
        for(LeaderboardEntry le:leaderboardEntryList){
            if(le.getUserId().equals(leaderboardEntry.getUserId())){
                leaderboardEntryList.remove(oldIndex);
                break;
            }
            oldIndex++;
        }
        for(LeaderboardEntry le:leaderboardEntryList){
            if(leaderboardEntry.getScore()>le.getScore()){
                leaderboardEntryList.add(index,leaderboardEntry);
                break;
            }
            else if(leaderboardEntry.getScore()==le.getScore()){
                if(leaderboardEntry.getTotalTime()<le.getTotalTime()){
                    leaderboardEntryList.add(index,leaderboardEntry);
                    break;
                }else if(leaderboardEntry.getTotalTime()==le.getTotalTime()){
                    int order=leaderboardEntry.getUser().getUsername().compareTo(le.getUser().getUsername());
                    if(order<0){
                        leaderboardEntryList.add(index,leaderboardEntry);
                        break;
                    }
                }
            }
            index++;
        }
        notifyItemMoved(oldIndex,index);
    }
}
