package com.mi1.duitku.Tab2.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab2.ChatMessageActivity;
import com.quickblox.chat.model.QBChatDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by owner on 3/31/2017.
 */

public class ChatDialogAdapter extends RecyclerView.Adapter<ChatDialogAdapter.ViewHolder> {

    private Context context;
    private ArrayList<QBChatDialog> qbChatDialogs;
    private int chatType;

    public ChatDialogAdapter(Context context, ArrayList<QBChatDialog> qbChatDialogs, int chatType) {
        this.context = context;
        this.qbChatDialogs = qbChatDialogs;
        this.chatType = chatType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.list_chat_dialog, parent, false);
        return new ViewHolder(v);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        QBChatDialog item = qbChatDialogs.get(position);
        holder.tvUserName.setText(item.getName());
        holder.tvMessage.setText(item.getLastMessage());
        if (item.getLastMessageDateSent() != 0) {
            holder.tvTime.setText(CommonFunction.getFormatedDate1(item.getLastMessageDateSent() * 1000));
        } else {
            holder.tvTime.setText(CommonFunction.getFormatedDate1(item.getCreatedAt()));
        }

        JSONObject qbUserPhoto = null;
        String photoUrl;
        if (item.getPhoto() != null) {
            if (chatType == 0) {
                try {
                    qbUserPhoto = new JSONObject(item.getPhoto());
                    photoUrl = qbUserPhoto.getString(item.getName().toLowerCase());
                    if (!photoUrl.isEmpty()) {
                        Picasso.with(context).load(photoUrl.toLowerCase()).fit().into(holder.civPhoto);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Picasso.with(context).load(item.getPhoto().toLowerCase()).fit().into(holder.civPhoto);
            }
        }

        if(item.getUnreadMessageCount() > 0 && !item.getLastMessageUserId().equals(AppGlobal.qbID)) {
            holder.ivNew.setVisibility(View.VISIBLE);
        }

        holder.llDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatMessageActivity.class);
                intent.putExtra(ChatMessageActivity.DIALOG_EXTRA, qbChatDialogs.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return qbChatDialogs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserName, tvMessage, tvTime;
        ImageView ivNew;
        CircleImageView civPhoto;
        LinearLayout llDialog;

        public ViewHolder(View view) {
            super(view);
            tvUserName = (TextView)view.findViewById(R.id.txt_username);
            tvMessage = (TextView)view.findViewById(R.id.txt_message);
            tvTime = (TextView)view.findViewById(R.id.txt_time);
            civPhoto = (CircleImageView)view.findViewById(R.id.civ_photo);
            ivNew = (ImageView)view.findViewById(R.id.img_new);
            ivNew.setVisibility(View.GONE);
            llDialog = (LinearLayout)view.findViewById(R.id.ll_dialog);
            if (chatType == 1) {
                civPhoto.setImageResource(R.drawable.ic_group);
            }
        }
    }

    public void setData(ArrayList<QBChatDialog> qbChatDialogs){
        this.qbChatDialogs = qbChatDialogs;
    }
}
