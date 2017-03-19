package com.app.innovationweek.model.holder;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app.innovationweek.R;
import com.app.innovationweek.adapter.LeaderboardAdapter;
import com.app.innovationweek.model.LeaderboardEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zeeshan on 3/11/2017.
 */

public class LeaderboardEntryHolder extends RecyclerView.ViewHolder {
    public static final String TAG = LeaderboardEntryHolder.class.getSimpleName();
    @Nullable
    @BindView(R.id.correct_count)
    TextView correct;
    @Nullable
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
    CardView rootView;
    private Context context;
    private ObjectAnimator colorFade;

    public LeaderboardEntryHolder(View itemView, View.OnClickListener infoToaster, String
            leaderboardType) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        rootView = (CardView) itemView;
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
        if (correct != null) correct.setOnClickListener(infoToaster);
        if (incorrect != null) incorrect.setOnClickListener(infoToaster);
        time.setOnClickListener(infoToaster);
        team.setOnClickListener(infoToaster);
        if (leaderboardType != null)
            switch (leaderboardType) {
                case LeaderboardAdapter.LEADERBOARD_TYPE.RANK:
                    score.setVisibility(View.GONE);
                    time.setVisibility(View.GONE);
                    break;
                case LeaderboardAdapter.LEADERBOARD_TYPE.SCORE:
                    time.setVisibility(View.GONE);
                    break;
                case LeaderboardAdapter.LEADERBOARD_TYPE.SCORE_TIME:
                default:
                    //nothing to hide
            }
    }

    public void setLeaderboardEntry(int position, @NonNull LeaderboardEntry leaderboardEntry,
                                    String hightlightUserId,Animator.AnimatorListener animatorListener) {
        name.setText(leaderboardEntry.getUser().getName());
        score.setText(score.getContext().getString(R.string.score, leaderboardEntry.getScore()));
        team.setText(team.getContext().getString(R.string.team, leaderboardEntry.getUser().getTeam().getName()));
        if (correct != null)
            correct.setText(correct.getContext().getString(R.string.correct, leaderboardEntry.getCorrect()));
        if (incorrect != null) incorrect.setText(correct.getContext().getString(R.string.incorrect,
                leaderboardEntry.getIncorrect()));
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
        if (hightlightUserId != null && hightlightUserId.equals(leaderboardEntry.getUserId())) {
//                rootView.set
            Log.d(TAG, "coloring done");
            if (colorFade != null && colorFade.isRunning()) colorFade.end();
            colorFade = ObjectAnimator.ofObject(rootView, "backgroundColor",
                    new ArgbEvaluator(), Color.parseColor("#80CBC4"), Color.WHITE);
            colorFade.setDuration(3000);
            colorFade.addListener(animatorListener);
            colorFade.start();
        } else {
            if (colorFade != null && colorFade.isRunning()) colorFade.end();
        }
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
