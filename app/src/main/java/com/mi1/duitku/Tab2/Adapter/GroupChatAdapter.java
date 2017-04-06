package com.mi1.duitku.Tab2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

/**
 * Created by owner on 3/31/2017.
 */

public class GroupChatAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<QBUser> qbUserArrayList;

    public GroupChatAdapter(Context context, ArrayList<QBUser> qbUserArrayList) {
        this.context = context;
        this.qbUserArrayList = qbUserArrayList;
    }

    @Override
    public int getCount() {
        return qbUserArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return qbUserArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view  = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_list_item_multiple_choice, null);

            TextView tvUser = (TextView)view.findViewById(android.R.id.text1);
            tvUser.setText(qbUserArrayList.get(position).getFullName());
        }

        return view;
    }
}
