package com.mi1.duitku.Tab2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mi1.duitku.Common.DividerItemDecoration;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab2.Adapter.ChatDialogAdapter;
import com.mi1.duitku.Tab2.Holder.QBChatDialogsHolder;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.listeners.QBSystemMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment implements QBSystemMessageListener, QBChatDialogMessageListener {

    private Context _context;
    private ChatDialogAdapter adapter;
    private RecyclerView recycler;
    private ProgressDialog progress;

    public GroupFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_group, container, false);
        _context = getContext();

        progress = new ProgressDialog(_context);
        progress.setMessage(getString(R.string.wait));
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        registerListener();
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

    private void registerListener() {

        QBSystemMessagesManager qbSystemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();
        qbSystemMessagesManager.removeSystemMessageListener(GroupFragment.this);
        qbSystemMessagesManager.addSystemMessageListener(GroupFragment.this);

        //Register listener Incoming Message
        QBIncomingMessagesManager incomingMessage = QBChatService.getInstance().getIncomingMessagesManager();
        incomingMessage.removeDialogMessageListrener(GroupFragment.this);
        incomingMessage.addDialogMessageListener(GroupFragment.this);
    }

    private void loadChatDialogs() {

        QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
        requestBuilder.setLimit(100);

        QBRestChatService.getChatDialogs(null, requestBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> qbChatDialogs, Bundle bundle) {
                QBChatDialogsHolder.getInstance().putDialogs(qbChatDialogs);

                if (progress.isShowing()) {
                    progress.dismiss();
                }

                ArrayList<QBChatDialog> groupChatDialogs = new ArrayList<QBChatDialog>();

                for(QBChatDialog dialog : qbChatDialogs) {
                    if (dialog.getType().equals(QBDialogType.PUBLIC_GROUP) || dialog.getType().equals(QBDialogType.GROUP)) {
                        groupChatDialogs.add(dialog);
                    }
                }
                adapter = new ChatDialogAdapter(_context, groupChatDialogs);
                recycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d("error", e.getMessage());
            }
        });
    }

    @Override
    public void processMessage(QBChatMessage qbChatMessage) {
        QBRestChatService.getChatDialogById(qbChatMessage.getBody()).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                if (qbChatDialog.getType().equals(QBDialogType.PUBLIC_GROUP) || qbChatDialog.getType().equals(QBDialogType.GROUP)) {
                    QBChatDialogsHolder.getInstance().putDialog(qbChatDialog);
                    ArrayList<QBChatDialog> qbChatDialogs = QBChatDialogsHolder.getInstance().getAllChatDialogs();
                    ArrayList<QBChatDialog> groupChatDialogs = new ArrayList<QBChatDialog>();

                    for(QBChatDialog dialog : qbChatDialogs) {
                        if (dialog.getType().equals(QBDialogType.PUBLIC_GROUP) || dialog.getType().equals(QBDialogType.GROUP)) {
                            groupChatDialogs.add(dialog);
                        }
                    }
                    adapter.setData(qbChatDialogs);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

    @Override
    public void processError(QBChatException e, QBChatMessage qbChatMessage) {

    }

    @Override
    public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
        QBDialogType dialogType = QBChatDialogsHolder.getInstance().getChatDialogById(qbChatMessage.getDialogId()).getType();
        if (dialogType.equals(QBDialogType.PUBLIC_GROUP) || dialogType.equals(QBDialogType.GROUP)) {
            loadChatDialogs();
        }
    }

    @Override
    public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {

    }
}
