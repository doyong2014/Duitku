package com.mi1.duitku.Tab2.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mi1.duitku.R;
import com.mi1.duitku.Tab2.Common.AvailableQBUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddUsersAdapter extends RecyclerView.Adapter<AddUsersAdapter.ViewHolder> {

    private Context context;
    private ArrayList<AvailableQBUser> qbUsers;

    public AddUsersAdapter(Context context, ArrayList<AvailableQBUser> qbUsers) {
        this.context = context;
        this.qbUsers = qbUsers;
    }

    @Override
    public AddUsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.list_select_users, parent, false);
        return new AddUsersAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final AvailableQBUser item = qbUsers.get(position);
        if (item.qbUser.getCustomData() != null && !item.qbUser.getCustomData().isEmpty()) {
            Picasso.with(context).load(item.qbUser.getCustomData().toLowerCase()).fit().into(holder.civPhoto);
        }
        holder.tvUserName.setText(item.qbUser.getFullName());

        holder.cbAdd.setChecked(item.isSelected);
        holder.cbAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.isSelected = isChecked;
            }
        });
    }

    @Override
    public int getItemCount() {
        return qbUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView civPhoto;
        TextView tvUserName;
        CheckBox cbAdd;

        public ViewHolder(View view) {
            super(view);
            civPhoto = (CircleImageView) view.findViewById(R.id.civ_photo);
            tvUserName = (TextView)view.findViewById(R.id.txt_username);
            cbAdd = (CheckBox)view.findViewById(R.id.cb_add);
        }
    }
}