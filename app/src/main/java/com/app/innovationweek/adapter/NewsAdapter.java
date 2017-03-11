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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by n188851 on 10-03-2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsHolder> {

    private static final String TAG = NewsAdapter.class.getSimpleName();
    private List<News> newsList = new ArrayList<>();
    private Context context;

    public NewsAdapter(Context context, List<News> newsList) {
        this.newsList = newsList;
        this.context = context;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_news, parent, false);
        Log.d(TAG, "Inflated Layout is: " + itemView.getClass());
        return new NewsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        News news = newsList.get(position);
        holder.setNews(news);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public List<News> getNewsList() {
        return newsList;
    }
}
