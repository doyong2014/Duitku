package com.mi1.duitku.Tab2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab2.Adapter.ChatMessageAdapter;
import com.mi1.duitku.Tab2.Holder.QBChatMessagesHolder;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBIncomingMessagesManager;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.exception.QBChatException;
import com.quickblox.chat.listeners.QBChatDialogMessageListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.request.QBMessageGetBuilder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smackx.muc.DiscussionHistory;

import java.util.ArrayList;

public class ChatMessageActivity extends AppCompatActivity implements QBChatDialogMessageListener {

    public static String DIALOG_EXTRA = "dialog";
    private QBChatDialog qbChatDialog;
    private RecyclerView recycler;
    private EditText etMessage;
    private ChatMessageAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_message);

        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.wait));
        progress.setCanceledOnTouchOutside(false);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler = (RecyclerView)findViewById(R.id.recycler_message);
        recycler.setLayoutManager(layoutManager);
        etMessage = (EditText)findViewById(R.id.et_post_msg);

        initChatDialogs();
        retrieveMessage();
        LinearLayout llSendMsg = (LinearLayout)findViewById(R.id.ll_post_msg_go);
        llSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QBChatMessage chatMessage = new QBChatMessage();
                chatMessage.setBody(etMessage.getText().toString());
                chatMessage.setSenderId(AppGlobal._userInfo.qbId);
                chatMessage.setSaveToHistory(true);

                try {
                    qbChatDialog.sendMessage(chatMessage);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
                if (qbChatDialog.getType() == QBDialogType.PRIVATE) {
                    //put message to cache
                    QBChatMessagesHolder.getInstance().putMessage(qbChatDialog.getDialogId(), chatMessage);
                    ArrayList<QBChatMessage> messages = QBChatMessagesHolder.getInstance().getChatMessagesByDialogId(qbChatDialog.getDialogId());
                    adapter.setData(messages);
                    adapter.notifyDataSetChanged();
                }

                //remove test from edittext
                hideKeyboard();
                recycler.smoothScrollToPosition(adapter.getItemCount()-1);
                etMessage.setText("");
                etMessage.setFocusable(true);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        qbChatDialog.removeMessageListrener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        qbChatDialog.removeMessageListrener(this);
    }

    private void retrieveMessage() {
        QBMessageGetBuilder messageGetBuilder = new QBMessageGetBuilder();
        messageGetBuilder.setLimit(100); // get limit 100 messages

        if (qbChatDialog != null) {
            QBRestChatService.getDialogMessages(qbChatDialog, messageGetBuilder).performAsync(new QBEntityCallback<ArrayList<QBChatMessage>>() {
                @Override
                public void onSuccess(ArrayList<QBChatMessage> qbChatMessages, Bundle bundle) {
                    //put messages to cache
                    progress.dismiss();
                    QBChatMessagesHolder.getInstance().putMessages(qbChatDialog.getDialogId(), qbChatMessages);

                    adapter = new ChatMessageAdapter(ChatMessageActivity.this, qbChatMessages);
                    recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if (adapter.getItemCount() > 0) {
                        recycler.smoothScrollToPosition(adapter.getItemCount() - 1);
                    }
//                    adapter.notifyItemInserted(qbChatMessages.size());
                }

                @Override
                public void onError(QBResponseException e) {
                    progress.dismiss();
                }
            });
        }
    }

    private void initChatDialogs() {

        progress.show();

        qbChatDialog = (QBChatDialog)getIntent().getSerializableExtra(DIALOG_EXTRA);
        qbChatDialog.initForChat(QBChatService.getInstance());

        //Register listener Incoming Message
        QBIncomingMessagesManager incomingMessage = QBChatService.getInstance().getIncomingMessagesManager();
        incomingMessage.addDialogMessageListener(ChatMessageActivity.this);

        if (qbChatDialog.getType() == QBDialogType.PUBLIC_GROUP || qbChatDialog.getType() == QBDialogType.GROUP) {
            DiscussionHistory discussionHistory = new DiscussionHistory();
            discussionHistory.setMaxStanzas(0);

            qbChatDialog.join(discussionHistory, new QBEntityCallback() {
                @Override
                public void onSuccess(Object o, Bundle bundle) {
                }

                @Override
                public void onError(QBResponseException e) {
                    Log.d("error", e.getMessage());
                }
            });
        }

        // Add message listener for that particular chat dialog
        qbChatDialog.addMessageListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.actionbar_bg));

        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

        } else if (id == android.R.id.home) {
            hideKeyboard();
            ChatMessageActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void processMessage(String s, QBChatMessage qbChatMessage, Integer integer) {
        //Cache Message
        QBChatMessagesHolder.getInstance().putMessage(qbChatMessage.getDialogId(), qbChatMessage);
        ArrayList<QBChatMessage> messages = QBChatMessagesHolder.getInstance().getChatMessagesByDialogId(qbChatMessage.getDialogId());
        adapter.setData(messages);
        adapter.notifyDataSetChanged();
        recycler.smoothScrollToPosition(adapter.getItemCount()-1);
    }

    @Override
    public void processError(String s, QBChatException e, QBChatMessage qbChatMessage, Integer integer) {
        Log.e("error", e.getMessage());
    }
}
