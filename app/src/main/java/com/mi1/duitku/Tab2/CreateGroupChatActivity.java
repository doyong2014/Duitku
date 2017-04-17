package com.mi1.duitku.Tab2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.mi1.duitku.BaseActivity;
import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab2.Adapter.GroupChatAdapter;
import com.mi1.duitku.Tab2.Holder.QBUsersHolder;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;

public class CreateGroupChatActivity extends BaseActivity {

    private ListView listUsers;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);
        
        listUsers = (ListView)findViewById(R.id.list_users);
        listUsers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.wait));
        progress.setCanceledOnTouchOutside(false);

        retrieveAllUser();

    }

    private void createGroupChat(SparseBooleanArray checkedItemPositions) {

        progress.show();

        int countChoice = listUsers.getCount();
        ArrayList<Integer> occupantIdsList = new ArrayList<>();
        for (int i=0; i<countChoice; i++){
            if(checkedItemPositions.get(i)) {
                QBUser user = (QBUser)listUsers.getItemAtPosition(i);
                occupantIdsList.add(user.getId());
            }
        }

        QBChatDialog dialog = new QBChatDialog();
        dialog.setName(CommonFunction.createChatDialogName(occupantIdsList));
        dialog.setType(QBDialogType.GROUP);
        dialog.setOccupantsIds(occupantIdsList);

        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                progress.dismiss();

                //send system message to recipient Id user
                QBSystemMessagesManager qbSystemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();
                QBChatMessage qbChatMessage = new QBChatMessage();
                qbChatMessage.setBody(qbChatDialog.getDialogId());

                for (int i=0; i<qbChatDialog.getOccupants().size(); i++) {
                    qbChatMessage.setRecipientId(qbChatDialog.getOccupants().get(i));
                    try {
                        qbSystemMessagesManager.sendSystemMessage(qbChatMessage);
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getBaseContext(), "create Group chat dialog successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("error", e.getMessage());
            }
        });
    }

    private void retrieveAllUser() {

        progress.show();

        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {

                ArrayList<QBUser>  qbUserWithoutCurret = new ArrayList<QBUser>();

                for(QBUser user: qbUsers) {
                    if (!user.getId().equals(QBChatService.getInstance().getUser().getId())) {
                        qbUserWithoutCurret.add(user);
                    }
                }
                QBUsersHolder.getInstance().putUsers(qbUserWithoutCurret);
                GroupChatAdapter adapter = new GroupChatAdapter(getBaseContext(), qbUserWithoutCurret);
                listUsers.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progress.dismiss();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("error", e.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.actionbar_bg));

        getMenuInflater().inflate(R.menu.menu_add_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_add_chat) {
            if(listUsers.getCheckedItemPositions().size() > 1) {
                createGroupChat(listUsers.getCheckedItemPositions());
            } else{
                Toast.makeText(CreateGroupChatActivity.this, "Please select two or more friends to chat", Toast.LENGTH_SHORT).show();
            }
        } else if (id == android.R.id.home) {
            CreateGroupChatActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
