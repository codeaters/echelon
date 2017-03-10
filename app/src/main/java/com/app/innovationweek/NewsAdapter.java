package com.app.innovationweek;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.app.innovationweek.model.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by n188851 on 10-03-2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<News> newsList = new ArrayList<>();

    public NewsAdapter(List<News> newsList){
        this.newsList=newsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

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
        return 0;
    }


    private static class NewsHolder extends RecyclerView.ViewHolder{



        public NewsHolder(View itemView) {
            super(itemView);
        }
    }
}
