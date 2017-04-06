package com.mi1.duitku.Tab2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mi1.duitku.Common.DividerItemDecoration;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab2.Adapter.ChatDialogAdapter;
import com.mi1.duitku.Tab2.Holder.QBChatDialogsHolder;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBDialogType;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment {

    private Context _context;
    private ChatDialogAdapter adapter;
    private RecyclerView recycler;

    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadChatDialogs();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_group, container, false);
        _context = getContext();

        LinearLayoutManager layoutManager = new LinearLayoutManager(_context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler = (RecyclerView) view.findViewById(R.id.recycler_group);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new DividerItemDecoration(_context));

        FloatingActionButton fabAdd = (FloatingActionButton)view.findViewById(R.id.fab_add_users);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_context, CreateGroupChatActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void loadChatDialogs() {

        ArrayList<QBChatDialog> qbChatDialogs = QBChatDialogsHolder.getInstance().getAllChatDialogs();
        ArrayList<QBChatDialog> groupChatDialogs = new ArrayList<QBChatDialog>();
        for(QBChatDialog dialog : qbChatDialogs) {
            if (dialog.getType() == QBDialogType.PUBLIC_GROUP || dialog.getType() == QBDialogType.GROUP) {
                groupChatDialogs.add(dialog);
            }
        }
        adapter = new ChatDialogAdapter(_context, groupChatDialogs);
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
