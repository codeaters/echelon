package com.app.innovationweek.model.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.innovationweek.R;
import com.app.innovationweek.model.LeaderboardEntry;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zeeshan on 3/11/2017.
 */

public class LeaderboardEntryHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image)
    CircleImageView image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.score)
    TextView score;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.team)
    TextView team;
    @BindView(R.id.rank)
    TextView rank;

    private Context context;

    public LeaderboardEntryHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
    }

    public void setLeaderboardEntry(int position, @NonNull LeaderboardEntry leaderboardEntry) {
        name.setText(leaderboardEntry.getUser().getName());
        score.setText(String.valueOf(leaderboardEntry.getScore()));
        team.setText(leaderboardEntry.getUser().getTeam().getName());
        rank.setText(String.valueOf(position + 1));
        time.setText(getTimeString(leaderboardEntry.getTotalTime()));
    }

    private String getTimeString(long totalTime) {
        long seconds = totalTime / 1000;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        String msg;
        if (minutes == 0)
            msg = context.getString(R.string.time_seconds, seconds);
        else
            msg = context.getString(R.string.time_minutes_seconds, minutes, seconds);
        return msg;
    }
}
