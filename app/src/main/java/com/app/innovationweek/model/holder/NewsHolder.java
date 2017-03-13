package com.app.innovationweek.model.holder;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.innovationweek.R;
import com.app.innovationweek.model.News;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Madeyedexter on 11-03-2017.
 */

public class NewsHolder extends RecyclerView.ViewHolder {


    @Nullable
    @BindView(R.id.image)
    public ImageView image;
    @BindView(R.id.content)
    public TextView content;
    @BindView(R.id.time)
    public TextView time;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd");

    public NewsHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setNews(News news) {
        if (news.getImgUrl() != null && !news.getImgUrl().isEmpty() && image != null)
            Picasso.with(image.getContext()).load(news.getImgUrl()).into(image);

        content.setText(news.getContent());
        time.setText(simpleDateFormat.format(new Date(news.getTimestamp())));
    }
}
