package com.mi1.duitku.Tab1;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mi1.duitku.R;
import com.mi1.duitku.Tab1.Common.DataModel;
import com.mi1.duitku.Tab1.Common.Tab1Global;
import com.squareup.picasso.Picasso;

/**
 * Created by owner on 3/7/2017.
 */

public class PromovAdapter extends RecyclerView.Adapter<PromovAdapter.ViewHolder> {

    Context context;

    public PromovAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.list_common, parent, false);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        DataModel.Post item = Tab1Global._promovData.get(position);
        holder.tvTitle.setText(item.title);
        if(item.thumbnail_images.thumbnail.url.isEmpty()) {
            holder.ivThumb.setVisibility(View.GONE);
        } else {
            Picasso.with(context).load(item.thumbnail_images.medium.url).fit().into(holder.ivThumb);
        }

        holder.ivThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContentsActivity.class);
                intent.putExtra("tab", 2);
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Tab1Global._promovData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivThumb;

        public ViewHolder(View v) {
            super(v);
            tvTitle = (TextView) v.findViewById(R.id.txt_title);
            ivThumb = (ImageView) v.findViewById(R.id.img_thumb);
        }
    }
}
