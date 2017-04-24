package com.mi1.duitku.Tab2.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.R;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

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
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.list_rev_msg, parent, false);
            return new ReceiveViewHolder(v);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        QBChatMessage item = qbChatMessages.get(position);
        if(holder.getItemViewType() == TYPE_SENDER) {
            SendViewHolder viewHolder = (SendViewHolder) holder;
            viewHolder.tvTime.setText(CommonFunction.getFormatedDate1(item.getDateSent()*1000));
            viewHolder.tvMessage.setText(item.getBody());
        } else {
            final ReceiveViewHolder viewHolder = (ReceiveViewHolder) holder;
            viewHolder.tvMessage.setText(item.getBody());
            String name = String.valueOf(item.getProperty("username"));
            viewHolder.tvTime.setText(name + " " + CommonFunction.getFormatedDate1(item.getDateSent()*1000));
            String picUrl = String.valueOf(item.getProperty("picUrl"));
            if (!picUrl.equals("null")) {
                Picasso.with(context).load(picUrl.toLowerCase()).fit().into(viewHolder.civPhoto);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (qbChatMessages.get(position).getSenderId().equals(AppGlobal.qbID)) {
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
        CircleImageView civPhoto;

        public ReceiveViewHolder (View itemView) {
            super (itemView);
            tvTime = (TextView) itemView.findViewById(R.id.txt_time);
            tvMessage = (TextView) itemView.findViewById(R.id.txt_msg);
            civPhoto = (CircleImageView) itemView.findViewById(R.id.civ_user_photo);
        }
    }
}
