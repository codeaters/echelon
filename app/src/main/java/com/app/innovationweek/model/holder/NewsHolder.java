package com.app.innovationweek.model.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.innovationweek.R;

/**
 * Created by Madeyedexter on 11-03-2017.
 */

public class NewsHolder extends RecyclerView.ViewHolder {


    public ImageView image;


    public TextView content;


    public TextView time;

    public NewsHolder(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.imageView);
        content = (TextView) itemView.findViewById(R.id.content);
        time = (TextView) itemView.findViewById(R.id.time);
    }

}
