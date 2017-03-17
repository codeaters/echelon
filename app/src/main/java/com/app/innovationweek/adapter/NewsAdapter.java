package com.app.innovationweek.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.innovationweek.R;
import com.app.innovationweek.model.News;
import com.app.innovationweek.model.holder.EmptyHolder;
import com.app.innovationweek.model.holder.NewsHolder;

import java.util.List;


/**
 * Created by n188851 on 10-03-2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = NewsAdapter.class.getSimpleName();
    private List<News> newsList;

    public NewsAdapter(List<News> newsList) {
        this.newsList = newsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case Type.IMAGE_NEWS:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_imagenews, parent, false);
                return new NewsHolder(itemView);
            case Type.NORMAL_NEWS:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_news, parent, false);
                return new NewsHolder(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_empty, parent, false);
                return new EmptyHolder(itemView);

        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case Type.EMPTY:
                ((EmptyHolder) holder).set(R.string.empty_news, R.drawable.photograph);
                break;
            case Type.IMAGE_NEWS:
            case Type.NORMAL_NEWS:
                News news = newsList.get(position);
                ((NewsHolder) holder).setNews(news);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (newsList == null || newsList.size() == 0) return Type.EMPTY;
        else if (newsList.get(position).getImgUrl() == null
                || newsList.get(position).getImgUrl().isEmpty()) return Type.NORMAL_NEWS;
        return Type.IMAGE_NEWS;
    }

    @Override
    public int getItemCount() {
        int size = newsList == null ? 0 : newsList.size();
        if (size == 0)
            size++; //for empty item
        return size;
    }

    public void addNewsAtTop(@NonNull News news) {
        if (newsList != null) {
            newsList.add(0, news);
            if (newsList.size() == 1)
                notifyItemChanged(0);
            else
                notifyItemInserted(0);
        }
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }

    public interface Type {
        int EMPTY = 2;
        int NORMAL_NEWS = 0;
        int IMAGE_NEWS = 1;
    }
}
