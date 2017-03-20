package com.app.iw.model.holder;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.innovationweek.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zeeshan on 3/17/2017.
 */

public class EmptyHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.msg)
    TextView msg;

    public EmptyHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void set(int stringId, int drawableId) {
        msg.setText(msg.getContext().getString(stringId == -1 ? R.string.empty_general : stringId));
        image.setImageDrawable(ContextCompat.getDrawable(image.getContext(), drawableId));
    }
}
