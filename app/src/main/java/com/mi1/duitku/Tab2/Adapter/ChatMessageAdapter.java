package com.mi1.duitku.Tab2.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.R;
import com.quickblox.chat.model.QBChatMessage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by owner on 4/3/2017.
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<QBChatMessage> qbChatMessages;
    private static final int TYPE_SENDER = 0;
    private static final int TYPE_RECEIVE = 1;

    public ChatMessageAdapter(Context context, ArrayList<QBChatMessage> qbChatMessages) {

        this.context = context;
        this.qbChatMessages = qbChatMessages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_SENDER) {
            View v = LayoutInflater.from(context).inflate(R.layout.list_send_msg, parent, false);
            return new SendViewHolder(v);
        } else if(viewType == TYPE_RECEIVE) {
            View v = LayoutInflater.from(context).inflate(R.layout.list_rev_msg, parent, false);
            return new ReceiveViewHolder(v);
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        QBChatMessage item = qbChatMessages.get(position);
        if(holder.getItemViewType() == TYPE_SENDER) {
            SendViewHolder viewHolder = (SendViewHolder) holder;
            viewHolder.tvTime.setText(getTime(item.getDateSent()));
            viewHolder.tvMessage.setText(item.getBody());
        } else {
            ReceiveViewHolder viewHolder = (ReceiveViewHolder) holder;
            viewHolder.tvTime.setText(getTime(item.getDateSent()));
            viewHolder.tvMessage.setText(item.getBody());
//            viewHolder.ivPhoto
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (qbChatMessages.get(position).getSenderId() == AppGlobal._userInfo.qbId) {
            return TYPE_SENDER;
        } else {
            return TYPE_RECEIVE;
        }
    }

    @Override
    public int getItemCount() {
        return qbChatMessages.size();
    }

    class SendViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime, tvMessage;

        public SendViewHolder (View itemView) {
            super (itemView);
            tvTime = (TextView) itemView.findViewById(R.id.txt_time);
            tvMessage = (TextView) itemView.findViewById(R.id.txt_msg);
        }
    }

    class ReceiveViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime, tvMessage;
        ImageView ivPhoto;

        public ReceiveViewHolder (View itemView) {
            super (itemView);
            tvTime = (TextView) itemView.findViewById(R.id.txt_time);
            tvMessage = (TextView) itemView.findViewById(R.id.txt_msg);
            ivPhoto = (ImageView) itemView.findViewById(R.id.img_photo);
        }
    }

    private String getTime(long time) {
        String ret = "";
        if (time != 0) {
            time = Calendar.getInstance().getTimeInMillis();
            ret = String.valueOf(DateFormat.format("MM/dd HH:mm", time));
        }
        return ret;
    }

    public void setData(ArrayList<QBChatMessage> qbChatMessages){
        this.qbChatMessages = qbChatMessages;
    }
}
