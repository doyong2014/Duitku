package com.mi1.duitku.Tab2;

import android.app.ProgressDialog;
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

import com.mi1.duitku.R;
import com.mi1.duitku.Tab2.Adapter.ChatDialogAdapter;
import com.mi1.duitku.Common.DividerItemDecoration;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends Fragment {

    private static int ACTIVITY_ADD_USER = 100;
    private Context _context;
    private LinearLayoutManager layoutManager;
    private ChatDialogAdapter adapter;
    private RecyclerView recycler;
    private ProgressDialog progress;

    public InboxFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        loadChatDialogs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_inbox, container, false);
        _context = getContext();

        progress = new ProgressDialog(_context);
        progress.setMessage(getString(R.string.wait));
        progress.setCanceledOnTouchOutside(false);

        layoutManager = new LinearLayoutManager(_context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler = (RecyclerView) view.findViewById(R.id.recycler_private);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new DividerItemDecoration(_context));

        FloatingActionButton fabAdd = (FloatingActionButton)view.findViewById(R.id.fab_add_user);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_context, ListUsersActivity.class);
                startActivityForResult(intent, ACTIVITY_ADD_USER);
            }
        });

        return view;
    }

    private void loadChatDialogs() {

        progress.show();
        QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
        requestBuilder.setLimit(50);

        QBRestChatService.getChatDialogs(null, requestBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
                adapter = new ChatDialogAdapter(_context, qbChatDialogs);
                recycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progress.dismiss();
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

}
