package com.app.innovationweek.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.innovationweek.R;
import com.app.innovationweek.model.News;
import com.app.innovationweek.model.holder.NewsHolder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by n188851 on 10-03-2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = NewsAdapter.class.getSimpleName();
    private List<News> newsList = new ArrayList<>();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd");
    private Context context;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    public NewsAdapter(List<News> newsList, Context context) {
        this.newsList = newsList;
        this.context = context;
    }

    public NewsAdapter(Context context, List<News> newsList) {
        this.newsList=newsList;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_news, parent, false);
        Log.d(TAG, "Inflated Layout is: " + itemView.getClass());
        return new NewsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        News news = newsList.get(position);

        NewsHolder newsHolder = (NewsHolder) holder;

        if (news.getImgUrl() != null)
            Picasso.with(context).load(news.getImgUrl()).into(newsHolder.image);
        else
            newsHolder.image.setImageBitmap(null);
        newsHolder.content.setText(news.getContent());
        newsHolder.time.setText(simpleDateFormat.format(new Date(news.getTimestamp())));



    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }



}
