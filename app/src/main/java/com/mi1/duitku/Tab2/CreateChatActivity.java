package com.mi1.duitku.Tab2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import com.mi1.duitku.Common.AppGlobal;
import com.mi1.duitku.Common.CommonFunction;
import com.mi1.duitku.Common.DividerItemDecoration;
import com.mi1.duitku.R;
import com.mi1.duitku.Tab2.Adapter.AddUsersAdapter;
import com.mi1.duitku.Tab2.Common.AvailableQBUser;
import com.mi1.duitku.Tab2.Common.Contacts;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.squareup.picasso.Picasso;

import org.jivesoftware.smack.SmackException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateChatActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private ProgressDialog progress;
    private ArrayList<AvailableQBUser> availableQBUsers = new ArrayList<AvailableQBUser>();
    private EditText etGroupName;
    private CircleImageView civPhoto;
    private int chatType;
    private final int RESULT_LOAD_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        chatType = getIntent().getIntExtra("chat_type", 0);

        etGroupName = (EditText)findViewById(R.id.edt_group_name);
        civPhoto = (CircleImageView)findViewById(R.id.civ_photo);
        civPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, 106);
            }
        });

        if(chatType == 0) {
            etGroupName.setVisibility(View.GONE);
            civPhoto.setVisibility(View.GONE);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(layoutManager);
        recycler.addItemDecoration(new DividerItemDecoration(this));

        progress = new ProgressDialog(this);
        progress.setMessage(getString(R.string.wait));
        progress.setCanceledOnTouchOutside(false);

        retrieveAllUser();
    }

    private void createPrivateChat(final QBUser selectedUser) {

        progress.show();
        JSONObject qbUserPhoto = new JSONObject();
        QBChatDialog dialog = DialogUtils.buildPrivateDialog(selectedUser.getId());
        try {
            qbUserPhoto.put(AppGlobal._userInfo.name.toLowerCase(), AppGlobal._userInfo.picUrl);
            qbUserPhoto.put(selectedUser.getFullName().toLowerCase(), selectedUser.getCustomData());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dialog.setPhoto(qbUserPhoto.toString());
        dialog.setType(QBDialogType.PRIVATE);

        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                progress.dismiss();

                //send system message to recipient Id user
                QBSystemMessagesManager qbSystemMessagesManager = QBChatService.getInstance().getSystemMessagesManager();
                QBChatMessage qbChatMessage = new QBChatMessage();
                qbChatMessage.setRecipientId(selectedUser.getId());
                qbChatMessage.setBody(qbChatDialog.getDialogId());
                try {
                    qbSystemMessagesManager.sendSystemMessage(qbChatMessage);
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getBaseContext(), "create private chat dialog successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("error", e.getMessage());
            }
        });
    }

    private void createGroupChat(ArrayList<QBUser> selectedUsers) {

        progress.show();

        ArrayList<Integer> selectedIds = new ArrayList<>();

        for (QBUser item : selectedUsers) {
            selectedIds.add(item.getId());
        }

        QBChatDialog dialog = new QBChatDialog();
        dialog.setName(etGroupName.getText().toString());
//        dialog.setPhoto("");
        dialog.setType(QBDialogType.GROUP);
        dialog.setOccupantsIds(selectedIds);

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

                for(QBUser user: qbUsers) {
                    if (!user.getId().equals(AppGlobal.qbID)) {
                        if (Contacts.getIntstace().listContact.contains(user.getLogin())) {
                            availableQBUsers.add(new AvailableQBUser(user, false));
                        }
                    }
                }
                AddUsersAdapter adapter = new AddUsersAdapter(getBaseContext(), availableQBUsers);
                recycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                progress.dismiss();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("error", e.getMessage());
            }
        });
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            String mImgURI = CommonFunction.getFilePathFromUri(CreateChatActivity.this, data.getData());
            Picasso.with(CreateChatActivity.this).load(mImgURI.toLowerCase()).fit().into(civPhoto);
        }
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
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.action_add_chat) {

            ArrayList<QBUser> selectedUsers = new ArrayList<>();

            for (AvailableQBUser item : availableQBUsers) {
                if (item.isSelected) {
                    selectedUsers.add(item.qbUser);
                }
            }

            if (chatType == 0) {
                if(selectedUsers.size() == 1) {
                    createPrivateChat(selectedUsers.get(0));
                } else{
                    Toast.makeText(CreateChatActivity.this, "Please select only one of friend to chat", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (etGroupName.getText().toString().isEmpty()) {
                    etGroupName.setError("Type group name.");
                    etGroupName.setFocusable(true);
                } else {
                    hideKeyboard();
                    if (selectedUsers.size() > 1) {
                        createGroupChat(selectedUsers);
                    } else {
                        Toast.makeText(CreateChatActivity.this, "Please select two or more friends to chat", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        } else if (id == android.R.id.home) {
            CreateChatActivity.this.finish();
        }

        return super.onOptionsItemSelected(menuItem);
    }
}
