package com.app.innovationweek.model.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.innovationweek.R;
import com.app.innovationweek.model.LeaderboardEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zeeshan on 3/11/2017.
 */

public class LeaderboardEntryHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.correct_count)
    TextView correct;
    @BindView(R.id.incorrect_count)
    TextView incorrect;
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

    public LeaderboardEntryHolder(View itemView, View.OnClickListener infoToaster) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        team.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        team.setSingleLine(true);
        team.setMarqueeRepeatLimit(5);
        team.setSelected(true);
        name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        name.setSingleLine(true);
        name.setMarqueeRepeatLimit(5);
        name.setSelected(true);
        score.setOnClickListener(infoToaster);
        correct.setOnClickListener(infoToaster);
        incorrect.setOnClickListener(infoToaster);
        time.setOnClickListener(infoToaster);
        team.setOnClickListener(infoToaster);
    }

    public void setLeaderboardEntry(int position, @NonNull LeaderboardEntry leaderboardEntry) {
        name.setText(leaderboardEntry.getUser().getName());
        score.setText(score.getContext().getString(R.string.score, leaderboardEntry.getScore()));
        team.setText(team.getContext().getString(R.string.team, leaderboardEntry.getUser().getTeam().getName()));
        correct.setText(correct.getContext().getString(R.string.correct, leaderboardEntry.getCorrect()));
        incorrect.setText(correct.getContext().getString(R.string.incorrect, leaderboardEntry.getIncorrect()));
        rank.setText(String.valueOf(position + 1));
        switch (position + 1) {
            case 1:
                rank.setBackground(ContextCompat.getDrawable(rank.getContext(), R.drawable.first));
                break;
            case 2:
                rank.setBackground(ContextCompat.getDrawable(rank.getContext(), R.drawable.second));
                break;
            case 3:
                rank.setBackground(ContextCompat.getDrawable(rank.getContext(), R.drawable.third));
                break;
            default:
                rank.setBackground(ContextCompat.getDrawable(rank.getContext(), R.drawable.other));
        }
        time.setText(getTimeString(leaderboardEntry.getTotalTime()));
    }

    private String getTimeString(long totalTime) {
        long minutes = totalTime / 60000;
        //total minutes * 60 * 1000 milliseconds
        double secondsMillis = ((double) (totalTime - minutes * 60 * 1000)) / 1000;
        String msg;
        if (minutes == 0)
            msg = context.getString(R.string.time_seconds, secondsMillis);
        else
            msg = context.getString(R.string.time_minutes_seconds, minutes, secondsMillis);
        return msg;
    }
}
