package com.app.innovationweek.model.holder;

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
    @BindView(R.id.team)
    TextView team;

    public LeaderboardEntryHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setLeaderboardEntry(@NonNull LeaderboardEntry leaderboardEntry) {
        name.setText(leaderboardEntry.getUser().getName());
        score.setText(String.valueOf(leaderboardEntry.getScore()));
        team.setText(leaderboardEntry.getUser().getTeam().getName());
    }
}
