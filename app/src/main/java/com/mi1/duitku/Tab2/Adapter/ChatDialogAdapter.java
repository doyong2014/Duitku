package com.mi1.duitku.Tab2.Adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab2.ChatMessageActivity;
import com.mi1.duitku.Tab2.Holder.QBUsersHolder;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by owner on 3/31/2017.
 */

public class ChatDialogAdapter extends RecyclerView.Adapter<ChatDialogAdapter.ViewHolder> {

    private Context context;
    private ArrayList<QBChatDialog> qbChatDialogs;

    public ChatDialogAdapter(Context context, ArrayList<QBChatDialog> qbChatDialogs) {
        this.context = context;
        this.qbChatDialogs = qbChatDialogs;
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
        holder.tvTime.setText(CommonFunction.getFormatedDate1(item.getLastMessageDateSent()*1000));

//        QBUser qbUser = QBUsersHolder.getInstance().getUserById(item.getUserId());
//        if (qbUser.getCustomData() != null) {
//            Picasso.with(context).load(qbUser.getCustomData().toLowerCase()).fit().into(holder.civPhoto);
//        } else {
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int randomColor = generator.getRandomColor();

            TextDrawable.IBuilder builder = TextDrawable.builder().beginConfig()
                    .withBorder(4)
                    .endConfig()
                    .round();

            TextDrawable drawable = builder.build(item.getName().substring(0, 1).toUpperCase(), randomColor);
            holder.ivPhoto.setImageDrawable(drawable);
//        }

        if(item.getUnreadMessageCount() > 0) {
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
//        CircleImageView civPhoto;
        ImageView ivPhoto;
        LinearLayout llDialog;

        public ViewHolder(View view) {
            super(view);
            tvUserName = (TextView)view.findViewById(R.id.txt_username);
            tvMessage = (TextView)view.findViewById(R.id.txt_message);
            tvTime = (TextView)view.findViewById(R.id.txt_time);
            ivPhoto = (ImageView)view.findViewById(R.id.iv_photo);
            ivNew = (ImageView)view.findViewById(R.id.img_new);
            ivNew.setVisibility(View.GONE);
            llDialog = (LinearLayout)view.findViewById(R.id.ll_dialog);
        }
    }

    public void setData(ArrayList<QBChatDialog> qbChatDialogs){
        this.qbChatDialogs = qbChatDialogs;
    }

}
