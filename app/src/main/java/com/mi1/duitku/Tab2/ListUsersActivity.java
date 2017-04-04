package com.mi1.duitku.Tab2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab2.Adapter.ListUsersAdapter;
import com.mi1.duitku.Tab2.Holder.QBUsersHolder;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;

public class ListUsersActivity extends AppCompatActivity {

    private ListView listUsers;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);
        
        listUsers = (ListView)findViewById(R.id.list_users);
        listUsers.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.wait));
        progress.setCanceledOnTouchOutside(false);

        retrieveAllUser();

    }

    private void createPrivateChat(int position) {

        progress.show();

        QBUser user = (QBUser)listUsers.getItemAtPosition(position);
        QBChatDialog dialog = DialogUtils.buildPrivateDialog(user.getId());

        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                progress.dismiss();
                Toast.makeText(getBaseContext(), "create private chat dialog successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(QBResponseException e) {

            }
        });
    }

    private void retrieveAllUser() {

        progress.show();

        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {

                QBUsersHolder.getInstance().putUsers(qbUsers);

                ArrayList<QBUser>  qbUserWithoutCurret = new ArrayList<QBUser>();

                for(QBUser user: qbUsers) {
                    if (user.getId() != AppGlobal._userInfo.qbId) {
                        qbUserWithoutCurret.add(user);
                    }
                }

                progress.dismiss();
                ListUsersAdapter adapter = new ListUsersAdapter(getBaseContext(), qbUserWithoutCurret);
                listUsers.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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
            if(listUsers.getCheckedItemPositions().size() == 1) {
                createPrivateChat(listUsers.getCheckedItemPosition());
            } else{
                Toast.makeText(ListUsersActivity.this, "Please select friend to chat", Toast.LENGTH_SHORT).show();
            }
        } else if (id == android.R.id.home) {
            ListUsersActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
